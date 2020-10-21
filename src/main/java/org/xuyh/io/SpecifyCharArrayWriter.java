/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.Writer;

/**
 * A {@link SpecifyCharArrayWriter} is an {@link Writer} contains an internal
 * buffer that contains chars that may be write into the stream. An internal
 * counter keeps track of the next byte to be reset by the <code>write</code>
 * method.
 * <p>
 * Write methods might be failed when there are not more chars available to
 * write into the writer. If write or append a char sequence, the action ends on
 * an <code>RuntimeException</code> when required space not enough, that is has
 * reach the end. For a char array writing, the pre-data write into the
 * {@link #buf}.
 * <p>
 * Closing a {@link SpecifyCharArrayWriter} has no effect. The methods in this
 * class can be called after the stream has been closed without generating an
 * {@link java.io.IOException}.
 *
 * @see SpecifyByteArrayOutputStream
 * @author XuYanhang
 * @since 2020-10-20
 */
public class SpecifyCharArrayWriter extends Writer implements java.io.Serializable {

	/**
	 * This {@link Writer} can be serialized
	 */
	private static final long serialVersionUID = 5439205907392550891L;

	/**
	 * An array of chars that was operated on writing actions
	 */
	private final char[] buf;
	/**
	 * Begin position in the {@link #buf} to do write, included
	 */
	private final int off;
	/**
	 * End position in the {@link #buf} to do write, excluded
	 */
	private final int end;

	/**
	 * Position on next write in the {@link #buf}
	 */
	private volatile int cursor;

	/**
	 * Creates {@link SpecifyCharArrayWriter} that uses <code>buf</code> as its
	 * {@link #buf}. The initial value of {@link #off} is 0, The initial value of
	 * {@link #cursor} is 0 and the initial value of {@link #end} is
	 * <code>buf.length</code>. The buffer array is not copied.
	 * 
	 * @param buf the operated buffer
	 */
	public SpecifyCharArrayWriter(char[] buf) {
		super();
		this.buf = buf;
		this.off = 0;
		this.end = buf.length;
		this.cursor = 0;
	}

	/**
	 * Creates {@link SpecifyCharArrayWriter} that uses <code>buf</code> as its
	 * {@link #buf}. The initial value of {@link #off} is <code>off</code>, The
	 * initial value of {@link #cursor} is <code>off</code> and the initial value of
	 * {@link #end} is <code>off+len</code>. The buffer array is not copied.
	 * 
	 * @param buf the operated buffer
	 * @param off the position in the buffer of the first char to write
	 * @param len the maximum number of chars to write into the buffer
	 * @throws IndexOutOfBoundsException if the given range is not in the buffer
	 */
	public SpecifyCharArrayWriter(char[] buf, int off, int len) {
		super();
		int end = off + len;
		if (off < 0 || end > buf.length || off > end)
			throw new IndexOutOfBoundsException();
		this.buf = buf;
		this.off = off;
		this.end = end;
		this.cursor = off;
	}

	/**
	 * Writes the specified byte to this writer. The general contract for
	 * <code>write</code> is that one byte is written to writer.
	 * 
	 * @see java.io.Writer#write(int)
	 */
	@Override
	public void write(int c) {
		synchronized (lock) {
			if (cursor >= end)
				throw new IllegalStateException("EOF");
			buf[cursor++] = (char) c;
		}
	}

	/**
	 * @see java.io.Writer#write(char[])
	 */
	@Override
	public void write(char[] cbuf) {
		write(cbuf, 0, cbuf.length);
	}

	/**
	 * @see java.io.Writer#write(char[], int, int)
	 */
	@Override
	public void write(char[] cbuf, int off, int len) {
		if (off < 0 || (off + len) > cbuf.length || (off + len) < off)
			throw new IndexOutOfBoundsException();
		if (len <= 0)
			return;
		boolean error = false;
		synchronized (lock) {
			if (end - cursor < len) {
				len = end - cursor;
				error = true;
			}
			System.arraycopy(cbuf, off, buf, cursor, len);
			cursor += len;
		}
		if (error)
			throw new IllegalStateException("EOF");
	}

	/**
	 * @see java.io.Writer#write(java.lang.String)
	 */
	@Override
	public void write(String str) {
		write(str, 0, str.length());
	}

	/**
	 * @see java.io.Writer#write(java.lang.String, int, int)
	 */
	@Override
	public void write(String str, int off, int len) {
		if (off < 0 || (off + len) > str.length() || (off + len) < off)
			throw new IndexOutOfBoundsException();
		if (len <= 0)
			return;
		boolean error = false;
		synchronized (lock) {
			if (end - cursor < len) {
				len = end - cursor;
				error = true;
			}
			str.getChars(off, off + len, buf, cursor);
			cursor += len;
		}
		if (error)
			throw new IllegalStateException("EOF");
	}

	/**
	 * @see java.io.Writer#append(java.lang.CharSequence)
	 */
	@Override
	public SpecifyCharArrayWriter append(CharSequence csq) {
		if (null == csq)
			csq = "null";
		append(csq, 0, csq.length());
		return this;
	}

	/**
	 * @see java.io.Writer#append(java.lang.CharSequence, int, int)
	 */
	@Override
	public SpecifyCharArrayWriter append(CharSequence csq, int start, int end) {
		if (null == csq)
			csq = "null";
		if (start < 0 || end > csq.length() || end < start)
			throw new IndexOutOfBoundsException();
		int index = start;
		synchronized (lock) {
			while (cursor < this.end && index < end)
				buf[cursor++] = csq.charAt(index++);
		}
		if (index < end)
			throw new IllegalStateException("EOF");
		return this;
	}

	/**
	 * @see java.io.Writer#append(char)
	 */
	@Override
	public synchronized SpecifyCharArrayWriter append(char c) {
		synchronized (lock) {
			if (cursor >= end)
				throw new IllegalStateException("EOF");
			buf[cursor++] = (char) c;
		}
		return this;
	}

	/**
	 * @see java.io.Writer#flush()
	 */
	@Override
	public void flush() {
		// Do nothing
	}

	/**
	 * 
	 * @see java.io.Writer#close()
	 */
	@Override
	public void close() {
		// Do nothing
	}

	/**
	 * Reset the {@link #cursor} to {@link #off} so that next write position will be
	 * at the {@link #off} of the {@link #buf}.
	 */
	public void reset() {
		synchronized (lock) {
			cursor = off;
		}
	}

	/**
	 * Returns the next write position who ranges from {@link #off} to {@link #end}.
	 * 
	 * @return the {@link #cursor} of next write position
	 */
	public int tell() {
		return cursor;
	}

	/**
	 * Returns chars count can be write into the {@link #buf}.
	 * 
	 * @return how many bytes remaining on write action
	 */
	public int available() {
		return end - cursor;
	}

	/**
	 * Returns the completed write chars in the operated buffer.
	 * 
	 * @return the successfully write bytes count
	 */
	public int completed() {
		return cursor - off;
	}

	/**
	 * Returns the begin write offset of the output stream at operated buffer.
	 * 
	 * @return the {@link #off}
	 */
	public int offset() {
		return off;
	}

	/**
	 * Returns the origin operated char array, not copied.
	 * 
	 * @return the {@link #buf}
	 */
	public char[] source() {
		return buf;
	}

	/**
	 * Converts the buffer's contents into a string. The length of the new
	 * <tt>String</tt> is a function of the character set, and hence may not be
	 * equal to the size of the buffer.
	 *
	 * @return String from the buffer's contents.
	 */
	@Override
	public String toString() {
		synchronized (lock) {
			return new String(buf, off, cursor);
		}
	}

}
