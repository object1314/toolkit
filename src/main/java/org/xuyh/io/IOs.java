/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Tool to action operations on IO Stream and also OS files. There throws
 * exceptions as less as possible.
 * 
 * @author XuYanhang
 * @since 2020-10-18
 *
 */
public class IOs {

	/*
	 * Methods for read methods on InputStream.
	 */

	/**
	 * Close all resources one by one.
	 */
	public static void closes(AutoCloseable... resources) {
		if (null == resources)
			return;
		for (int i = 0; i < resources.length; i++)
			if (null != resources[i])
				try {
					resources[i].close();
				} catch (Throwable t) {
					// Silence failed
				}
	}

	/**
	 * Read a file as bytes. If failed read like when file not exists or an
	 * IOException happens, <code>null</code> returns.
	 */
	public static byte[] read(String filePath) {
		if (null == filePath)
			return null;
		return read(new File(filePath));
	}

	/**
	 * Read a file as bytes. If failed read like when file not exists or an
	 * IOException happens, <code>null</code> returns.
	 */
	public static byte[] read(File file) {
		if (null == file || !file.exists() || !file.isFile())
			return null;
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
		} catch (Exception e) {
			return null;
		}
		return read(inputStream);
	}

	/**
	 * Cast an input stream as bytes. If failed read like when stream not
	 * exists(<code>null</code>) or an IOException happens, <code>null</code>
	 * returns. Notice that the input stream closes here.
	 */
	public static byte[] read(InputStream inputStream) {
		if (null == inputStream)
			return null;
		try {
			int initSize = 1024;
			try {
				initSize = Math.max(inputStream.available(), initSize);
			} catch (Exception e) {
			}
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(initSize);
			byte[] buffer = new byte[256];
			int len;
			while ((len = inputStream.read(buffer)) >= 0)
				outputStream.write(buffer, 0, len);
			return outputStream.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}

	}

	/**
	 * Read the text on file. If failed read like when file not exists or an
	 * IOException happens, <code>null</code> returns.
	 */
	public static String readText(String filePath, String charset) {
		if (null == filePath)
			return null;
		return readText(new File(filePath), charset);
	}

	/**
	 * Read the text on file. If failed read like when file not exists or an
	 * IOException happens, <code>null</code> returns.
	 */
	public static String readText(File file, String charset) {
		if (null == file || !file.exists() || !file.isFile())
			return null;
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
		} catch (Exception e) {
			return null;
		}
		return readText(inputStream, charset);
	}

	/**
	 * Read the text. If failed read like when inputStream not exists or an
	 * IOException happens, <code>null</code> returns. Notice that the input stream
	 * closes here.
	 */
	public static String readText(InputStream inputStream, String charset) {
		if (null == inputStream)
			return null;
		BufferedReader reader = null;
		try {
			StringBuilder stringBuilder = new StringBuilder(1024);
			if (null != charset)
				try {
					reader = new BufferedReader(new InputStreamReader(inputStream, charset));
				} catch (java.io.UnsupportedEncodingException e) {
				}
			if (null == reader)
				reader = new BufferedReader(new InputStreamReader(inputStream));
			char[] buffer = new char[256];
			int len;
			while ((len = reader.read(buffer)) >= 0)
				stringBuilder.append(buffer, 0, len);
			return stringBuilder.toString();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Load an input stream on input stream in ClassPath. Returns the opened input
	 * stream or <code>null</code> if failed.
	 */
	public static InputStream loadClassPathStream(String path) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (null == classLoader)
			classLoader = IOs.class.getClassLoader();
		if (null == classLoader)
			classLoader = ClassLoader.getSystemClassLoader();
		return classLoader.getResourceAsStream(path);
	}

	/**
	 * Read the resource data in ClassPath. If failed read like stream load failed
	 * or an IOException happens, <code>null</code> returns.
	 */
	public static byte[] readClassPathBytes(String path) {
		InputStream inputStream = loadClassPathStream(path);
		if (null == inputStream)
			return null;
		return read(inputStream);
	}

	/**
	 * Read the text in ClassPath. If failed read like stream load failed or an
	 * IOException happens, <code>null</code> returns.
	 */
	public static String readClassPathText(String path, String charset) {
		InputStream inputStream = loadClassPathStream(path);
		if (null == inputStream)
			return null;
		return readText(inputStream, charset);
	}

	/*
	 * Methods for write methods on OutputStream.
	 */

	/**
	 * Write a data to file. Returns <code>false</code> when write failed like an
	 * IOException happens on write action.
	 */
	public static boolean write(byte[] data, String filePath) {
		if (null == filePath)
			return false;
		return write(data, new File(filePath));
	}

	/**
	 * Write a data to file. Returns <code>false</code> when write failed like an
	 * IOException happens on write action.
	 */
	public static boolean write(byte[] data, File file) {
		if (null == file || file.isDirectory())
			return false;
		File dir = file.getParentFile();
		if (null == dir)
			return false;
		if (!dir.exists()) {
			dir.mkdirs();
		} else {
			if (dir.isFile())
				return false;
		}
		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
			return write(data, out);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Write a data to output stream. Returns <code>false</code> when write failed
	 * like an IOException happens on write action. Notice that the output stream
	 * doesn't close here.
	 */
	public static boolean write(byte[] data, OutputStream out) {
		if (null == data)
			return false;
		try {
			out.write(data);
			out.flush();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Write a text to file. Returns <code>false</code> when write failed like an
	 * IOException happens on write action.
	 */
	public static boolean writeText(String text, String filePath, String charset) {
		if (null == filePath)
			return false;
		return writeText(text, new File(filePath), charset);
	}

	/**
	 * Write a text to file. Returns <code>false</code> when write failed like an
	 * IOException happens on write action.
	 */
	public static boolean writeText(String text, File file, String charset) {
		if (null == file || file.isDirectory())
			return false;
		File dir = file.getParentFile();
		if (null == dir)
			return false;
		if (!dir.exists()) {
			dir.mkdirs();
		} else {
			if (dir.isFile()) {
				return false;
			}
		}
		try (OutputStream out = new FileOutputStream(file)) {
			return writeText(text, out, charset);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Write a text to output stream. Returns <code>false</code> when write failed
	 * like an IOException happens on write action. Notice that the output stream
	 * doesn't close here.
	 */
	public static boolean writeText(String text, OutputStream out, String charset) {
		if (null == text)
			return false;
		out = new UncloseProxyOutputStream(out);
		BufferedWriter writer = null;
		if (null != charset)
			try {
				writer = new BufferedWriter(new OutputStreamWriter(out, charset));
			} catch (java.io.UnsupportedEncodingException e1) {
			}
		if (null == writer)
			writer = new BufferedWriter(new OutputStreamWriter(out));
		try {
			writer.write(text);
			writer.flush();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}

		return true;
	}

	/*
	 * Methods for operation on OS file system.
	 */

	/**
	 * Copy a file to another File.
	 * 
	 * @throws Exception when any exception happens
	 */
	public static void copyValidFile(File fromFile, File toFile) throws Exception {
		if (!toFile.getParentFile().exists())
			toFile.getParentFile().mkdirs();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(fromFile));
			out = new BufferedOutputStream(new FileOutputStream(toFile));
			byte[] buffer = new byte[512];
			for (int len = -1; (len = in.read(buffer)) != -1;) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (Exception e) {
				}
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * Copy a directory to another directory.
	 * 
	 * @throws Exception when any exception happens
	 */
	public static void copyDir(File fromDir, File toDir) throws Exception {
		if (!toDir.exists())
			toDir.mkdirs();
		File[] fromFiles = fromDir.listFiles();
		for (File fromFile : fromFiles) {
			if (!fromFile.canRead())
				continue;
			File toFile = new File(toDir, fromFile.getName());
			if (fromFile.isDirectory()) {
				copyDir(fromFile, toFile);
			} else if (fromFile.isFile()) {
				copyValidFile(fromFile, toFile);
			}
		}
	}

	/**
	 * Copy file-file file-directory directory-directory.
	 * 
	 * @throws Exception when any exception happens
	 */
	public static void copyFile(File fromFile, File toFile) throws Exception {
		if (fromFile.isDirectory()) {
			copyDir(fromFile, toFile);
		} else if (fromFile.isFile()) {
			File targetFile = toFile.isDirectory() ? new File(toFile, fromFile.getName()) : toFile;
			copyValidFile(fromFile, targetFile);
		}
	}

	/**
	 * Copy a file or directory into another file or directory.
	 * 
	 * @throws Exception when any exception happens
	 */
	public static void copyFileInto(File fromFile, File toDir) throws Exception {
		if (!toDir.exists())
			toDir.mkdirs();
		File toFile = new File(toDir, fromFile.getName());
		if (fromFile.isDirectory()) {
			copyDir(fromFile, toFile);
		} else {
			copyValidFile(fromFile, toFile);
		}
	}

	/**
	 * Delete a file or directory. Returns <code>true</code> only when delete
	 * finish.
	 *
	 * @throws SecurityException    If a security manager exists and its
	 *                              <code>{@link
	 *          java.lang.SecurityManager#checkWrite(java.lang.String)}</code>
	 *                              method denies write access to either the old or
	 *                              new pathnames
	 * @throws NullPointerException If any parameter is <code>null</code>
	 */
	public static boolean deleteFile(File file) {
		if (null == file || !file.exists())
			return false;
		if (file.isDirectory())
			deleteContents(file, (FileFilter) null);
		return file.delete();
	}

	/**
	 * Delete all contents of directory, but not the directory itself.
	 * 
	 *
	 * @throws SecurityException    If a security manager exists and its
	 *                              <code>{@link
	 *          java.lang.SecurityManager#checkWrite(java.lang.String)}</code>
	 *                              method denies write access to either the old or
	 *                              new pathnames
	 * @throws NullPointerException If any parameter is <code>null</code>
	 */
	public static int deleteContents(File dir) {
		return deleteContents(dir, (FileFilter) null);
	}

	/**
	 * Delete some contents of directory, but not the directory itself. Returns the
	 * deleted files(not directory) count.
	 *
	 * @throws SecurityException    If a security manager exists and its
	 *                              <code>{@link
	 *          java.lang.SecurityManager#checkWrite(java.lang.String)}</code>
	 *                              method denies write access to either the old or
	 *                              new pathnames
	 * @throws NullPointerException If any parameter is <code>null</code>
	 */
	public static int deleteContents(File dir, FileFilter filter) {
		if (null == dir || !dir.exists())
			return 0;
		if (!dir.isDirectory())
			return dir.delete() ? 1 : 0;
		File[] subFiles = dir.listFiles();
		if (null == subFiles || subFiles.length == 0)
			return 0;
		int result = 0;
		for (int i = 0; i < subFiles.length; i++) {
			File file = subFiles[i];
			if (null == filter || filter.accept(file)) {
				if (file.isDirectory()) {
					result += deleteContents(file, filter);
					String[] fileContent = file.list();
					if (null == fileContent || fileContent.length == 0)
						file.delete();
				} else {
					result += file.delete() ? 1 : 0;
				}
			}
		}
		return result;
	}

	/**
	 * Move a file as another one. Returns <code>true</code> only when move action
	 * successes.
	 *
	 * @throws SecurityException    If a security manager exists and its
	 *                              <code>{@link
	 *          java.lang.SecurityManager#checkWrite(java.lang.String)}</code>
	 *                              method denies write access to either the old or
	 *                              new pathnames
	 * @throws NullPointerException If any parameter is <code>null</code>
	 */
	public static boolean moveFile(File fromFile, File toFile) {
		if (!fromFile.exists())
			return false;
		if (fromFile.renameTo(toFile))
			return true;
		// The rename method may be failed then use the copy method.
		try {
			copyFile(fromFile, toFile);
			deleteFile(fromFile);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private IOs() {
		super();
	}

}
