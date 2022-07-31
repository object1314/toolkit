/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.sql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Operations mostly SQL file converts into database, CSV file converts into database
 * and database converts into SQL file.
 *
 * @see MysqlDumper
 * @author XuYanhang
 * @since 2022-08-01
 *
 */
public interface DatabaseDumper extends AutoCloseable {
	/**
	 * Returns a database dumper instance. The instance type depends on the protocol of the URL.
	 *
	 * @param url URL connects to MySQL, exist and must start with {@value #PREFIX}
	 * @param user user name connects to MySQL
	 * @param password password connects to MySQL
	 * @throws SQLException if a database access error occurs
	 */
	public static DatabaseDumper of(String url, String user, String password) throws SQLException {
		Properties prop = new Properties();
		if (user != null) {
			prop.put("user", user);
		}
		if (password != null) {
			prop.put("password", password);
		}
		return of(url, prop);
	}

	/**
	 * Returns a database dumper instance. The instance type depends on the protocol of the URL.
	 *
	 * @param url URL connects to MySQL, exist and must start with {@value #PREFIX}
	 * @param prop properties to connect to MySQL server, not null
	 * @throws SQLException if a database access error occurs
	 */
	public static DatabaseDumper of(String url, Properties prop) throws SQLException {
		int prefixEnd = url.indexOf('/');
		prefixEnd = prefixEnd > 0 ? prefixEnd : url.length();
		String prefix = url.substring(0, prefixEnd).toLowerCase(Locale.ROOT);
		if (prefix.startsWith(MysqlDumper.PREFIX)) {
			return new MysqlDumper(url, prop);
		}
		throw new SQLException("Protocol not found");
	}

	/**
	 * Load SQL file into the database.
	 *
	 * @param sqlFilePath file path to a SQL file
	 * @throws SQLException if a database access error occurs
	 * @throws IOException If an I/O error occurs
	 */
	public void loadSqlFile(String sqlFilePath) throws SQLException, IOException;

	/**
	 * Load CSV file into the database.
	 *
	 * @param csvFilePath file path to a CSV file
	 * @param tableName table to load the CSV file into
	 * @param clearOrigin if clear all data in the table
	 * @throws SQLException if a database access error occurs
	 * @throws IOException If an I/O error occurs
	 */
	public void loadCsvFileIntoTable(String csvFilePath, String tableName, boolean clearOrigin)
			throws SQLException, IOException;


	/**
	 * Returns the database name.
	 *
	 * @return the name of current database
	 * @throws SQLException if a database access error occurs
	 */
	public String getDatabaseName() throws SQLException;


	/**
	 * Returns the table names in the database.
	 *
	 * @return the table names of current database
	 * @throws SQLException if a database access error occurs
	 */
	public List<String> getTableNames() throws SQLException;

	/**
	 * Returns the procedure names in the database.
	 *
	 * @return the procedure names of current database
	 * @throws SQLException if a database access error occurs
	 */
	public List<String> getProcedureNames() throws SQLException;

	/**
	 * Returns the CREATE SQL of the table. It doesn't end with ';'.
	 *
	 * @param tableName the table to query
	 * @return the CREATE SQL of the table
	 * @throws SQLException if a database access error occurs
	 */
	public String getTableCreateSql(String tableName) throws SQLException;

	/**
	 * Returns the CREATE SQL of the procedure.
	 *
	 * @param procedureName the procedure to query
	 * @return the CREATE SQL of the procedure
	 * @throws SQLException if a database access error occurs
	 */
	public String getProcedureCreateSql(String procedureName) throws SQLException;

	/**
	 * Output the full database into a SQL file.
	 *
	 * @param filePath path of the SQL file to dump into
	 * @throws SQLException if a database access error occurs
	 * @throws FileNotFoundException if the file not exists
	 * @throws IOException If an I/O error occurs
	 */
	public void dumpDatabaseSqlFile(String filePath) throws SQLException, FileNotFoundException, IOException;

	/**
	 * Output the database structure with table structures, procedure structures into a SQL file.
	 *
	 * @param filePath path of the SQL file to dump into
	 * @throws SQLException if a database access error occurs
	 * @throws FileNotFoundException if the file not exists
	 * @throws IOException If an I/O error occurs
	 */
	public void dumpDatabaseStructureSqlFile(String filePath)
			throws SQLException, FileNotFoundException, IOException;

	/**
	 * Output all procedures in the database in a SQL file.
	 *
	 * @param filePath path of the SQL file to dump into
	 * @throws SQLException if a database access error occurs
	 * @throws FileNotFoundException if the file not exists
	 * @throws IOException If an I/O error occurs
	 */
	public void dumpProcedureSqlFile(String filePath) throws SQLException, FileNotFoundException, IOException;

	/**
	 * Dump all structure and data of a table into a SQL file.
	 *
	 * @param tableName the table to dump from
	 * @param filePath path of the SQL file to dump into
	 * @throws SQLException if a database access error occurs
	 * @throws FileNotFoundException if the file not exists
	 * @throws IOException If an I/O error occurs
	 */
	public void dumpTableSqlFile(String tableName, String filePath)
			throws SQLException, FileNotFoundException, IOException;

	/**
	 * Dump only insert SQLs with all data of a table into a SQL file.
	 *
	 * @param tableName the table to dump from
	 * @param filePath path of the SQL file to dump into
	 * @throws SQLException if a database access error occurs
	 * @throws FileNotFoundException if the file not exists
	 * @throws IOException If an I/O error occurs
	 */
	public void dumpTableDataSqlFile(String tableName, String filePath)
			throws SQLException, FileNotFoundException, IOException;

	/**
	 * Releases this <code>DatabaseDumper</code> object's database and JDBC resources
	 * immediately instead of waiting for them to be automatically released.
	 *
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	public void close() throws SQLException;
}
