/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.sql;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Operations mostly SQL file converts into database, CSV file converts into database
 * and database converts into SQL file on MySQL platform.
 *
 * @author XuYanhang
 * @since 2022-08-01
 *
 */
public class MysqlDumper implements DatabaseDumper {
	/**
	 * MySQL URL should starts with this, case insensitive, lower case value here
	 */
	public static final String PREFIX = "jdbc:mysql:";
	private static final char DB_LINE_CHAR = '\n';
	private static final Pattern DB_TABLE_NAME_REGEX = Pattern.compile("^[0-9a-zA-Z_$]{1,64}$");

	private Connection conn;

	/**
	 * Creates a dumper on connection based on MySQL driver.
	 *
	 * @see DriverManager#getConnection(String, String, String)
	 * @param url URL connects to MySQL, exist and must start with {@value #PREFIX}
	 * @param prop properties to connect to MySQL server, not null
	 * @throws SQLException if a database access error occurs
	 */
	public MysqlDumper(String url, Properties prop) throws SQLException {
		super();
		tryLoadDriver();
		String urlPrefix = url.substring(0, Math.min(url.length(), PREFIX.length()))
				.toLowerCase(Locale.ROOT);
		if (!urlPrefix.equals(PREFIX)) {
			throw new SQLException(String.format("%s prefix expected but %s...",
					PREFIX, urlPrefix));
		}
		conn = DriverManager.getConnection(url, prop);
	}

	@Override
	public void loadSqlFile(String sqlFilePath) throws SQLException, IOException {
		String sql = String.format(Locale.ROOT, "SOURCE \"%s\"", validateFilePath(sqlFilePath));
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		}
	}

	@Override
	public void loadCsvFileIntoTable(String csvFilePath, String tableName, boolean clearOrigin)
			throws SQLException, IOException {
		String sql = String.format(Locale.ROOT, "LOAD DATA INFILE \"%s\" INTO TABLE `%s`",
				validateFilePath(csvFilePath), validateTableName(tableName));
		try (Statement stmt = conn.createStatement()) {
			for (String headSql : getLoadHeadSqls()) {
				stmt.addBatch(headSql);
			}
			if (clearOrigin) {
				stmt.addBatch(String.format("DELETE FROM `%s`", validateTableName(tableName)));
			}
			stmt.addBatch(sql);
			for (String tailSql : getLoadTailSqls()) {
				stmt.addBatch(tailSql);
			}
			stmt.executeBatch();
		}
	}

	@Override
	public String getDatabaseName() throws SQLException {
		return conn.getCatalog();
	}

	@Override
	public List<String> getTableNames() throws SQLException {
		try (Statement stmt = createQueryStatement()) {
			return queryTableNames(stmt);
		}
	}

	private List<String> queryTableNames(Statement stmt) throws SQLException {
		String sql = String.format(Locale.ROOT, "SHOW FULL TABLES FROM `%s` WHERE TABLE_TYPE = 'BASE TABLE'",
				getDatabaseName());
		List<String> tableNames = new ArrayList<>();
		try (ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				tableNames.add(rs.getString(1));
			}
		}
		return tableNames;
	}

	@Override
	public List<String> getProcedureNames() throws SQLException {
		try (Statement stmt = createQueryStatement()) {
			return queryProcedureNames(stmt);
		}
	}

	private List<String> queryProcedureNames(Statement stmt) throws SQLException {
		String sql = String.format(Locale.ROOT, "SELECT `SPECIFIC_NAME` from `INFORMATION_SCHEMA`.`ROUTINES`"
				+ " WHERE `ROUTINE_SCHEMA` = '%s' AND ROUTINE_TYPE = 'PROCEDURE'", getDatabaseName());
		List<String> procedureNames = new ArrayList<>();
		try (ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				procedureNames.add(rs.getString(1));
			}
		}
		return procedureNames;
	}

	@Override
	public String getTableCreateSql(String tableName) throws SQLException {
		try (Statement stmt = createQueryStatement()) {
			return queryTableCreateSql(stmt, validateTableName(tableName));
		}
	}

	private String queryTableCreateSql(Statement stmt, String tableName) throws SQLException {
		String sql = String.format(Locale.ROOT, "SHOW CREATE TABLE `%s`", tableName);
		try (ResultSet rs = stmt.executeQuery(sql)) {
			return rs.next() ? rs.getString(2) : "";
		}
	}

	@Override
	public String getProcedureCreateSql(String procedureName) throws SQLException {
		try (Statement stmt = createQueryStatement()) {
			return queryProcedureCreateSql(stmt, validateTableName(procedureName));
		}
	}

	private String queryProcedureCreateSql(Statement stmt, String procedureName) throws SQLException {
		String sql = String.format(Locale.ROOT, "SHOW CREATE PROCEDURE `%s`", procedureName);
		try (ResultSet rs = stmt.executeQuery(sql)) {
			return rs.next() ? rs.getString(3) : "";
		}
	}

	@Override
	public void dumpDatabaseSqlFile(String filePath) throws SQLException, FileNotFoundException, IOException {
		callDumpSqlFile(filePath, (stmt, writer) -> {
			for (String tableName : queryTableNames(stmt)) {
				dumpTableStructure(stmt, tableName, writer);
				dumpTableData(stmt, tableName, writer, false);
			}
			dumpProcedures(stmt, writer);
		});
	}

	@Override
	public void dumpDatabaseStructureSqlFile(String filePath)
			throws SQLException, FileNotFoundException, IOException {
		callDumpSqlFile(filePath, (stmt, writer) -> {
			for (String tableName : queryTableNames(stmt)) {
				dumpTableStructure(stmt, tableName, writer);
			}
			dumpProcedures(stmt, writer);
		});
	}

	@Override
	public void dumpProcedureSqlFile(String filePath) throws SQLException, FileNotFoundException, IOException {
		callDumpSqlFile(filePath, this::dumpProcedures);
	}

	private void dumpProcedures(Statement stmt, Writer writer) throws SQLException, IOException {
		for (String procedureName : queryProcedureNames(stmt)) {
			String createSql = queryProcedureCreateSql(stmt, procedureName);
			if (createSql.isEmpty()) {
				continue;
			}
			println(writer);
			printfln(writer, "/* Procedure structure for procedure `%s` */", procedureName);
			printfln(writer, "/*!50003 DROP PROCEDURE IF EXISTS `%s` */;", procedureName);
			println(writer, "DELIMITER$$");
			printfln(writer, "/*!50003 %s */$$", createSql);
			println(writer, "DELIMITER;");
		}
	}

	@Override
	public void dumpTableSqlFile(String tableName, String filePath)
			throws SQLException, FileNotFoundException, IOException {
		callDumpSqlFile(filePath, (stmt, writer) -> {
			dumpTableStructure(stmt, tableName, writer);
			dumpTableData(stmt, tableName, writer, false);
		});
	}

	@Override
	public void dumpTableDataSqlFile(String tableName, String filePath)
			throws SQLException, FileNotFoundException, IOException {
		callDumpSqlFile(filePath, (stmt, writer) -> {
			dumpTableData(stmt, tableName, writer, false);
		});
	}

	private void dumpTableStructure(Statement stmt, String tableName, Writer writer)
			throws SQLException, IOException {
		String createSql = queryTableCreateSql(stmt, tableName);
		println(writer);
		if (createSql.isEmpty()) {
			printfln(writer, "/* Table structure EMPTY for table `%s` */", tableName);
			return;
		}
		printfln(writer, "/* Table structure for table `%s` */", tableName);
		printfln(writer, "DROP TABLE IF EXISTS `%s`;", tableName);
		println(writer, createSql, ";");
	}

	private void dumpTableData(Statement stmt, String tableName, Writer writer,
			boolean mergeInsertFlag) throws SQLException, IOException {
		ResultSet rs = stmt.executeQuery(String.format(Locale.ROOT, "SELECT * FROM `%s`", tableName));
		boolean hasNext = rs.next();
		int colCount = rs.getMetaData().getColumnCount();
		println(writer);
		if (!hasNext || colCount <= 0) {
			printfln(writer, "/* Table data EMPTY for table `%s` */", tableName);
			return;
		}
		printfln(writer, "/* Table datas for table `%s` */", tableName);
		String insertPrefix = createInsertPrefixSql(tableName, colCount, rs.getMetaData());
		if (mergeInsertFlag) {
			println(writer, insertPrefix);
			do {
				String insertValues = createInsertValuesSql(rs, colCount);
				hasNext = rs.next();
				if (hasNext) {
					println(writer, insertValues, ",");
				} else {
					println(writer, insertValues, ";");
					break;
				}
			} while (true);
		} else {
			do {
				println(writer, insertPrefix, " ", createInsertValuesSql(rs, colCount), ";");
			} while (hasNext = rs.next());
		}
	}

	private void callDumpSqlFile(String filePath, SqlFileBodyDumper bodyDumper)
			throws SQLException, FileNotFoundException, IOException {
		File file = new File(validateFilePath(filePath));
		file.getParentFile().mkdirs();
		boolean success = false;
		Writer writer = createWriter(file);
		try {
			for (String headSql : getLoadHeadSqls()) {
				println(writer, headSql);
			}
			try (Statement stmt = createQueryStatement()) {
				bodyDumper.call(stmt, writer);
			}
			for (String tailSql : getLoadTailSqls()) {
				println(writer, tailSql);
			}
			writer.flush();
			success = true;
		} finally {
			writer.close();
			if (!success) {
				file.delete();
			}
		}
	}

	@Override
	public void close() throws SQLException {
		conn.close();
	}

	@Override
	protected void finalize() throws Throwable {
		// Releases on GC automatically
		close();
	}

	private Statement createQueryStatement() throws SQLException {
		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		try {
			stmt.setFetchSize(Integer.MIN_VALUE);
			stmt.setFetchDirection(ResultSet.FETCH_REVERSE);
		} catch (SQLException e) {
			// Ignore these exceptions
		}
		return stmt;
	}

	private static Writer createWriter(File file) throws FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
	}

	private static List<String> getLoadHeadSqls() {
		return Arrays.asList("/*!40101 SET NAMES utf8 */;",
				"/*!40101 SET SQL_MODE=''*/;",
				"/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;",
				"/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;",
				"/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;",
				"/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;");
	}

	private static List<String> getLoadTailSqls() {
		return Arrays.asList("/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;",
				"/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;",
				"/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;",
				"/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;");
	}

	private static String createInsertPrefixSql(String tableName, int colCount, ResultSetMetaData metaData)
			throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO `").append(tableName).append("` (");
		for (int i = 1; i <= colCount; ++i) {
			sb.append('`').append(metaData.getColumnLabel(i)).append('`').append(',');
		}
		sb.setCharAt(sb.length() - 1, ')');
		sb.append(" VALUES");
		return sb.toString();
	}

	private static String createInsertValuesSql(ResultSet rs, int colCount)throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for (int i = 1; i <= colCount; ++i) {
			appendInsertValue(sb, rs.getObject(i)).append(',');
		}
		sb.setCharAt(sb.length() - 1, ')');
		return sb.toString();
	}

	private static StringBuilder appendInsertValue(StringBuilder sb, Object value) {
		if (value == null) {
			sb.append("NULL");
		} else if (value instanceof Number) {
			sb.append(value.toString());
		} else if (value instanceof Boolean) {
			sb.append(((Boolean) value) ? '1' : '0');
		} else {
			sb.append('\'');
			String result = value.toString();
			for (int i = 0; i < result.length(); ++i) {
				sb.append(result.charAt(i));
				if (result.charAt(i) == '\'') {
					sb.append('\'');
				}
			}
			sb.append('\'');
		}
		return sb;
	}

	private static void println(Writer writer, String... str) throws IOException {
		for (int i = 0; i < str.length; ++i) {
			writer.write(str[i]);
		}
		writer.write(DB_LINE_CHAR);
	}

	private static void printfln(Writer writer, String format, Object...args) throws IOException {
		writer.write(String.format(Locale.ROOT, format, args));
		writer.write(DB_LINE_CHAR);
	}

	private static String validateFilePath(String filePath) throws IOException {
		return new File(filePath).getCanonicalPath();
	}

	private static String validateTableName(String tableName) throws SQLException {
		if (!DB_TABLE_NAME_REGEX.matcher(tableName).matches()) {
			throw new SQLException("Illegal table name: `" + tableName + "`");
		}
		return tableName;
	}

	private static Class<?> tryLoadDriver() {
		try {
			// mysql-connector-java 6.x and after
			return Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
		try {
			// mysql-connector-java 5.x and before
			return Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	@FunctionalInterface
	private static interface SqlFileBodyDumper {
		/**
		 * Function as short codes to do dump operation in this dumper
		 */
		public void call(Statement stmt, Writer writer) throws SQLException, IOException;
	}
}
