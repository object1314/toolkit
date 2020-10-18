/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * An stream to ZIP or UNZIP in IO stream way. The partial cached memory.
 * 
 * @see ZipOutputStream
 * @see ZipInputStream
 * @author XuYanhang
 * @since 2020-10-18
 *
 */
public class Zips {

	/**
	 * UnZIP a ZIP stream in custom choice way. According a inner name create an
	 * output stream.
	 * 
	 * @param in       The source ZIP input stream. The input stream will be closed
	 *                 auto.
	 * @param supplier To supply the out put stream for each entry. The output
	 *                 stream will be closed auto. If supply is a <code>null</code>
	 *                 output stream then jump the entry.
	 */
	public static void unzip(InputStream in, Function<String, OutputStream> supplier) {
		if (in == null)
			throw new NullPointerException();
		ZipInputStream zipIn = null;
		try {
			zipIn = new ZipInputStream(in);
			ZipEntry entry;
			while ((entry = zipIn.getNextEntry()) != null) {
				OutputStream out = null;
				try {
					if (entry.isDirectory())
						continue;
					String name = checkFileName(entry.getName());
					if (name.isEmpty())
						continue;
					if (null != supplier)
						out = supplier.apply(name);
					if (null == out)
						continue;
					byte[] cache = new byte[256];
					int len;
					while ((len = zipIn.read(cache)) >= 0)
						out.write(cache, 0, len);
					out.flush();
				} catch (Exception e) {
				} finally {
					try {
						zipIn.closeEntry();
					} catch (Exception e) {
					}
					if (null != out) {
						try {
							out.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
		} finally {
			if (null != zipIn)
				try {
					zipIn.close();
				} catch (Exception e) {
				}
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * UnZIP the ZIP stream into a directory.
	 * 
	 * @param in  The source ZIP input stream. The input stream will be closed auto.
	 * @param dir The target UNZIP files directory.
	 */
	public static void unzip(InputStream in, File dir) {
		unzip(in, name -> {
			File file = new File(dir, name);
			File parentFile = file.getParentFile();
			if (!parentFile.exists())
				parentFile.mkdirs();
			try {
				return new BufferedOutputStream(new FileOutputStream(file));
			} catch (Exception e) {
				return null;
			}
		});
	}

	/**
	 * Create the builder on ZIPs in an OS file. The file's directory would be auto
	 * created if it doesn't exist.
	 * <p>
	 * Sample:
	 * 
	 * <pre>
	 * File file = new File("/dir/test.zip"));
	 * try (Zips.ZipBuilder builder = Zips.newZipBuilder(file)) {
	 * 	// codes here
	 * }
	 * </pre>
	 * 
	 * @param out the output stream to write ZIP data, null not allowed
	 * @return the {@link Zips.ZipBuilder}
	 */
	public static Zips.ZipBuilder newZipBuilder(File file) throws java.io.IOException {
		File parentFile = file.getParentFile();
		if (!parentFile.exists())
			parentFile.mkdirs();
		return new ZipBuilder(new BufferedOutputStream(new FileOutputStream(file)));
	}

	/**
	 * Create the builder on ZIPs in an out stream.
	 * <p>
	 * Sample:
	 * 
	 * <pre>
	 * OutputStream out = new BufferedOutputStream(new FileOutputStream("/dir/test.zip"));
	 * try (Zips.ZipBuilder builder = Zips.newZipBuilder(out)) {
	 * 	// codes here
	 * }
	 * </pre>
	 * 
	 * @param out the output stream to write ZIP data, null not allowed
	 * @return the {@link Zips.ZipBuilder}
	 */
	public static Zips.ZipBuilder newZipBuilder(OutputStream out) {
		return new ZipBuilder(out);
	}

	/**
	 * Create the builder on ZIPs in an out stream but doesn't close the output
	 * stream in {@link #close()} action. It's same
	 * <code>newZipBuilder(new UncloseProxyOutputStream(out))</code>.
	 * <p>
	 * Sample:
	 * 
	 * <pre>
	 * OutputStream out = new BufferedOutputStream(new FileOutputStream("/dir/test.zip"));
	 * try (Zips.ZipBuilder builder = Zips.newUncloseZipBuilder(out)) {
	 * 	// codes here
	 * }
	 * </pre>
	 * 
	 * @param out the output stream to write ZIP data, null not allowed
	 * @return the {@link Zips.ZipBuilder}
	 */
	public static Zips.ZipBuilder newUncloseZipBuilder(OutputStream out) {
		return new ZipBuilder(new UncloseProxyOutputStream(out));
	}

	/*
	 * Filter parse the file name so that the returns file name would be legal
	 */
	private static String checkFileName(String name) {
		if (null == name || name.isEmpty())
			return "";
		// Rule file separator as /
		name = name.replace('\\', '/');
		// Remove all chars as next.
		name = name.replaceAll("(\ufffe|\uffff)+", "");
		// Split the name as sub names.
		String[] subNames = name.split("/");
		LinkedList<String> trimedSubNames = new LinkedList<>();
		for (int i = 0; i < subNames.length; i++) {
			String subName = subNames[i];
			// When combined by some chars.
			if (subName.matches("^(\\.|\\s)*$")) {
				// When point to the parent.
				if (subName.matches("^\\s*\\.\\s*\\.\\s*$") && !trimedSubNames.isEmpty())
					trimedSubNames.pollLast();
				continue;
			}
			// Replace all invalid chars.
			char[] chars = subName.toCharArray();
			for (int j = 0; j < chars.length; j++) {
				char c = chars[j];
				if (c < ' ' || c == '|' || c == ':' || c == '?' || c == '*' || c == '\"' || c == '<' || c == '>')
					chars[j] = '_';
			}
			if (chars.length > 0)
				trimedSubNames.addLast(new String(chars));
		}
		StringBuilder nameBuilder = new StringBuilder();
		int index = 0;
		for (String trimedSubName : trimedSubNames) {
			nameBuilder.append(trimedSubName);
			if (index != trimedSubNames.size() - 1)
				nameBuilder.append('/');
			index++;
		}
		return nameBuilder.toString();
	}

	/**
	 * ZipBuilder is a builder to ZIP files, who is allowed to ZIP
	 * 
	 * @author XuYanhang
	 *
	 */
	public static class ZipBuilder implements java.io.Closeable, java.io.Flushable {

		/**
		 * The outPut stream.
		 */
		private final ZipOutputStream zipOut;

		/**
		 * Initialize this ZipBuilder
		 * 
		 * @param out output stream
		 */
		private ZipBuilder(OutputStream out) {
			super();
			this.zipOut = new ZipOutputStream(out);
		}

		/**
		 * ZIP the files. The ZIP entry name will include the director path if the file
		 * is a directory.
		 * 
		 * @param basePath The base path of the ZIP file entry
		 * @param file     The source file or directory
		 * @return this
		 */
		public ZipBuilder zip(String basePath, File file) {
			zip(basePath, file, (FileFilter) null);
			return this;
		}

		/**
		 * ZIP the files pass the filter. The ZIP entry name will include the director
		 * path if the file is a directory.
		 * 
		 * @param basePath The base path of the ZIP file entry
		 * @param file     The source file or directory
		 * @param filter   Filter on the files
		 * @return this
		 */
		public ZipBuilder zip(String basePath, File file, FileFilter filter) {
			basePath = basePath.replace('\\', '/');
			while (basePath.endsWith("/"))
				basePath.substring(0, basePath.length() - 1);
			String name = basePath.isEmpty() ? file.getName() : (basePath + "/" + file.getName());
			if (file.isFile() && file.canRead()) {
				if (null == filter || filter.accept(file))
					try (InputStream in = new FileInputStream(file)) {
						zip(name, in);
					} catch (Exception e) {
					}
			} else if (file.isDirectory()) {
				File[] subFiles = file.listFiles();
				if (null != subFiles)
					for (int i = 0; i < subFiles.length; i++)
						zip(name, subFiles[i], filter);
			}
			return this;
		}

		/**
		 * ZIP the files in a directory. The ZIP entry name does not include the
		 * director path.
		 * 
		 * @param basePath The base path of the ZIP file entry.
		 * @param dir      The source directory
		 * @return this
		 */
		public ZipBuilder zipContents(String basePath, File dir) {
			zipContents(basePath, dir, (FileFilter) null);
			return this;
		}

		/**
		 * ZIP the files in a directory those pass the filter. The ZIP entry name does
		 * not include the director path.
		 * 
		 * @param basePath The base path of the ZIP file entry.
		 * @param dir      The source directory
		 * @param filter   Filter on the files
		 * @return this
		 */
		public ZipBuilder zipContents(String basePath, File dir, FileFilter filter) {
			File[] files = dir.listFiles(filter);
			if (null == files)
				return this;
			for (int index = 0; index < files.length; index++)
				if (null != files[index])
					zip(basePath, files[index], filter);
			return this;
		}

		/**
		 * ZIP a input stream as ZIP. The input stream will be closed auto.
		 * 
		 * @param name ZIP file entry name
		 * @param in   ZIP file entry stream
		 * @return this
		 */
		public ZipBuilder zip(String name, InputStream in) {
			if (null == in)
				return this;
			name = checkFileName(name);
			if (name.isEmpty()) {
				try {
					in.close();
				} catch (Exception e) {
				}
				return this;
			}
			BufferedInputStream bin = new BufferedInputStream(in);
			try {
				zipOut.putNextEntry(new ZipEntry(name));
				byte[] cache = new byte[256];
				int len = -1;
				while ((len = bin.read(cache)) >= 0)
					zipOut.write(cache, 0, len);
			} catch (Exception e) {
			} finally {
				try {
					zipOut.closeEntry();
				} catch (Exception e) {
				}
				try {
					bin.close();
				} catch (Exception e) {
				}
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			return this;
		}

		/**
		 * ZIP a file data as ZIP Entry
		 * 
		 * @param name ZIP file entry name
		 * @param data ZIP file entry data
		 * @return this
		 */
		public ZipBuilder zip(String name, byte[] data) {
			if (null == data)
				return this;
			name = checkFileName(name);
			if (name.isEmpty())
				return this;
			try {
				zipOut.putNextEntry(new ZipEntry(name));
				zipOut.write(data);
			} catch (Exception e) {
			} finally {
				try {
					zipOut.closeEntry();
				} catch (Exception e) {
				}
			}
			return this;
		}

		/**
		 * Flushes the compressed build stream.
		 */
		@Override
		public void flush() {
			try {
				zipOut.flush();
			} catch (Exception e) {
			}
		}

		/**
		 * Close the build stream
		 */
		@Override
		public void close() {
			try {
				zipOut.close();
			} catch (Exception e) {
			}
		}

	}

}
