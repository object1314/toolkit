/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * An operator on GZIP format data for GZIP action and GUNZIP action.
 * 
 * @see GZIPOutputStream
 * @see GZIPInputStream
 * @author XuYanhang
 * @since 2020-10-18
 *
 */
public class GZips {

	/**
	 * GZIP a source bytes array as target GZIP bytes.
	 * 
	 * @param source bytes to GZIP from, null not allowed
	 * @return bytes GZIP
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] gzip(byte[] source) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream zipOut = null;
		try {
			zipOut = new GZIPOutputStream(out);
			zipOut.write(source);
			zipOut.flush();
		} finally {
			if (null != zipOut)
				try {
					zipOut.close();
				} catch (Exception e) {
				}
		}
		return out.toByteArray();
	}

	/**
	 * GZIP a source file as a GZIP file.
	 * 
	 * @param source file to GZIP from, null not allowed
	 * @param target file to GZIP as, null not allowed
	 * @throws IOException if an I/O error occurs
	 */
	public static void gzip(File source, File target) throws IOException {
		if (!target.getParentFile().exists())
			target.getParentFile().mkdirs();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(source));
			out = new BufferedOutputStream(new FileOutputStream(target));
			gzip(in, out, true);
		} finally {
			if (null != in)
				try {
					in.close();
				} catch (Exception e) {
				}
			if (null != out)
				try {
					out.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * GZIP a input stream and output it into an output stream.
	 * 
	 * @param source input stream to GZIP from, null not allowed
	 * @param target output stream to GZIP into, null not allowed
	 * @throws IOException if an I/O error occurs
	 */
	public static void gzip(InputStream source, OutputStream target) throws IOException {
		gzip(source, target, true);
	}

	/**
	 * GZIP a input stream and output it into an output stream.
	 * 
	 * @param source     input stream to GZIP from, null not allowed
	 * @param target     output stream to GZIP into, null not allowed
	 * @param closingOut if close the target output stream automatically
	 * @throws IOException if an I/O error occurs
	 */
	public static void gzip(InputStream source, OutputStream target, boolean closingOut) throws IOException {
		if (!closingOut)
			target = new UncloseProxyOutputStream(target);
		GZIPOutputStream zipOut = null;
		try {
			zipOut = new GZIPOutputStream(target);
			byte[] buffer = new byte[256];
			int len;
			while ((len = source.read(buffer)) >= 0)
				zipOut.write(buffer, 0, len);
			zipOut.flush();
		} finally {
			if (null != zipOut)
				try {
					zipOut.close();
				} catch (Exception e) {
				}
			if (null != target)
				try {
					target.close();
				} catch (Exception e) {
				}
			if (null != source)
				try {
					source.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * GUNZIP a GZIP bytes a result target bytes.
	 * 
	 * @param source bytes to GUNZIP from, null not allowed
	 * @return bytes result after GUNZIP
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] gunzip(byte[] source) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(source);
		gunzip(in, out, true);
		return out.toByteArray();
	}

	/**
	 * GUNZIP a GZIP file as a result target file.
	 * 
	 * @param source file to GUNZIP from, null not allowed
	 * @param target file to GUNZIP as, null not allowed
	 * @throws IOException if an I/O error occurs
	 */
	public static void gunzip(File source, File target) throws IOException {
		if (!target.getParentFile().exists())
			target.getParentFile().mkdirs();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(source));
			out = new BufferedOutputStream(new FileOutputStream(target));
			gunzip(in, out, true);
		} finally {
			if (null != in)
				try {
					in.close();
				} catch (Exception e) {
				}
			if (null != out)
				try {
					out.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * GUNZIP a input stream and output it into an output stream.
	 * 
	 * @param source input stream to GUNZIP from, null not allowed
	 * @param target output stream to GUNZIP into, null not allowed
	 * @throws IOException if an I/O error occurs
	 */
	public static void gunzip(InputStream source, OutputStream target) throws IOException {
		gunzip(source, target, true);
	}

	/**
	 * GUNZIP a input stream and output it into an output stream.
	 * 
	 * @param source     input stream to GUNZIP from, null not allowed
	 * @param target     output stream to GUNZIP into, null not allowed
	 * @param closingOut flag if close the target output stream automatically
	 * @throws IOException if an I/O error occurs
	 */
	public static void gunzip(InputStream source, OutputStream target, boolean closingOut) throws IOException {
		if (!closingOut)
			target = new UncloseProxyOutputStream(target);
		GZIPInputStream zipIn = null;
		try {
			byte[] buffer = new byte[256];
			zipIn = new GZIPInputStream(source);
			int len;
			while ((len = zipIn.read(buffer)) >= 0)
				target.write(buffer, 0, len);
			target.flush();
		} finally {
			if (null != zipIn)
				try {
					zipIn.close();
				} catch (Exception e) {
				}
			if (null != source)
				try {
					source.close();
				} catch (Exception e) {
				}
			if (null != target)
				try {
					target.close();
				} catch (Exception e) {
				}
		}
	}

}
