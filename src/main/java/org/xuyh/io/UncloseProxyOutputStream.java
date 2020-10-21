/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An {@link OutputStream} proxy on a source one but close action has no effect.
 * Used on some partial writing block who need do close action but the origin
 * output stream would do write action then.
 * <p>
 * There exists a {@link #counter} to count the write bytes from this stream.
 * Use {@link #count()} to get it.
 * 
 * @author XuYanhang
 * @since 2020-10-18
 *
 */
public class UncloseProxyOutputStream extends OutputStream {

	/** Source output stream for this proxy */
	private final OutputStream src;

	/** Counter to count write bytes */
	private final AtomicLong counter;

	/**
	 * Initialize this proxy output stream
	 * 
	 * @param source required source output stream for this proxy and
	 *               <code>null</code> not allowed
	 */
	public UncloseProxyOutputStream(OutputStream source) {
		super();
		if (source == null)
			throw new NullPointerException();
		this.src = source;
		this.counter = new AtomicLong(0);
	}

	/**
	 * Returns the source output stream.
	 * 
	 * @return the {@link #src}
	 */
	public OutputStream source() {
		return src;
	}

	/**
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		src.write(b);
		counter.getAndAdd(1);
	}

	/**
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		src.write(b);
		counter.getAndAdd(b.length);
	}

	/**
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		src.write(b, off, len);
		counter.getAndAdd(len);
	}

	/**
	 * @see java.io.OutputStream#flush()
	 */
	@Override
	public void flush() throws IOException {
		src.flush();
	}

	/**
	 * Close action do only flush action but doesn't close the source output stream.
	 * 
	 * @see java.io.OutputStream# close()
	 */
	@Override
	public void close() throws IOException {
		src.flush();
	}

	/**
	 * Return the write bytes' count from this output stream.
	 * 
	 * @return the write bytes' count from this output stream
	 */
	public long count() {
		return counter.get();
	}

}
