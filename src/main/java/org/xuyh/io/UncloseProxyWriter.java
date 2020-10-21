/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An {@link Writer} proxy on a source one but close action has no effect. Used
 * on some partial writing block who need do close action but the origin writer
 * would do write action then.
 * <p>
 * There exists a {@link #counter} to count the write chars from this writer.
 * Use {@link #count()} to get it.
 * 
 * @author XuYanhang
 * @since 2020-10-21
 *
 */
public class UncloseProxyWriter extends Writer {

	/** Source writer for this proxy */
	private final Writer src;

	/** Counter to count write bytes */
	private final AtomicLong counter;

	/**
	 * Initialize this proxy writer
	 * 
	 * @param source required source writer for this proxy and <code>null</code> not
	 *               allowed
	 */
	public UncloseProxyWriter(Writer source) {
		super();
		if (source == null)
			throw new NullPointerException();
		this.src = source;
		this.counter = new AtomicLong(0);
	}

	/**
	 * Returns the source writer.
	 * 
	 * @return the {@link #src}
	 */
	public Writer source() {
		return src;
	}

	/**
	 * @see java.io.Writer#write(int)
	 */
	@Override
	public void write(int c) throws IOException {
		src.write(c);
		counter.getAndAdd(1);
	}

	/**
	 * @see java.io.Writer#write(char[])
	 */
	@Override
	public void write(char[] cbuf) throws IOException {
		src.write(cbuf);
		counter.getAndAdd(cbuf.length);
	}

	/**
	 * @see java.io.Writer#write(char[], int, int)
	 */
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		src.write(cbuf, off, len);
		counter.getAndAdd(len);
	}

	/**
	 * @see java.io.Writer#write(java.lang.String)
	 */
	@Override
	public void write(String str) throws IOException {
		src.write(str);
		counter.getAndAdd(str.length());
	}

	/**
	 * @see java.io.Writer#write(java.lang.String, int, int)
	 */
	@Override
	public void write(String str, int off, int len) throws IOException {
		src.write(str, off, len);
		counter.getAndAdd(len);
	}

	/**
	 * @see java.io.Writer#append(java.lang.CharSequence)
	 */
	@Override
	public UncloseProxyWriter append(CharSequence csq) throws IOException {
		src.append(csq);
		counter.getAndAdd(csq.length());
		return this;
	}

	/**
	 * @see java.io.Writer#append(java.lang.CharSequence, int, int)
	 */
	@Override
	public UncloseProxyWriter append(CharSequence csq, int start, int end) throws IOException {
		src.append(csq, start, end);
		counter.getAndAdd(Math.abs(end - start));
		return this;
	}

	/**
	 * @see java.io.Writer#append(char)
	 */
	@Override
	public UncloseProxyWriter append(char c) throws IOException {
		src.append(c);
		counter.getAndAdd(1);
		return this;
	}

	/**
	 * @see java.io.Writer#flush()
	 */
	@Override
	public void flush() throws IOException {
		src.flush();
	}

	/**
	 * Close action do only flush action but doesn't close the source writer.
	 * 
	 * @see java.io.Writer#close()
	 */
	@Override
	public void close() throws IOException {
		src.flush();
	}

	/**
	 * Return the write characters' count from this writer.
	 * 
	 * @return the write characters' count from this writer
	 */
	public long count() {
		return counter.get();
	}

}
