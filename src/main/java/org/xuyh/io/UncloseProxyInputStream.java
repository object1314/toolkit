/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An {@link InputStream} proxy on a source one but close action has no effect.
 * Used on some partial reading block who need do close action but the origin
 * input stream would do read action then.
 * <p>
 * There exists a {@link #counter} to count the read bytes from this stream. Use
 * {@link #count()} to get it.
 * 
 * @author XuYanhang
 * @since 2020-10-21
 *
 */
public class UncloseProxyInputStream extends InputStream {

	/** Source input stream for this proxy */
	private final InputStream src;

	/** Counter to count write bytes */
	private final AtomicLong counter;

	/**
	 * Initialize this proxy input stream
	 * 
	 * @param source required source input stream for this proxy and
	 *               <code>null</code> not allowed
	 */
	public UncloseProxyInputStream(InputStream source) {
		super();
		if (null == source)
			throw new NullPointerException();
		this.src = source;
		this.counter = new AtomicLong();
	}

	/**
	 * Returns the source input stream.
	 * 
	 * @return the {@link #src}
	 */
	public InputStream source() {
		return src;
	}

	/**
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		int v = src.read();
		if (v > -1)
			counter.getAndAdd(1);
		return v;
	}

	/**
	 * @see java.io.InputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		int v = src.read(b);
		if (v > -1)
			counter.getAndAdd(v);
		return v;
	}

	/**
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int v = src.read(b, off, len);
		if (v > -1)
			counter.getAndAdd(v);
		return v;
	}

	/**
	 * @see java.io.InputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		long v = src.skip(n);
		counter.getAndAdd(v);
		return v;
	}

	/**
	 * @see java.io.InputStream#available()
	 */
	@Override
	public int available() throws IOException {
		return src.available();
	}

	/**
	 * Close action has no effects.
	 * 
	 * @see java.io.InputStream#close()
	 */
	@Override
	public void close() {
		// Do nothing
	}

	/**
	 * @see java.io.InputStream#mark(int)
	 */
	@Override
	public void mark(int readlimit) {
		src.mark(readlimit);
	}

	/**
	 * @see java.io.InputStream#reset()
	 */
	@Override
	public void reset() throws IOException {
		src.reset();
	}

	/**
	 * @see java.io.InputStream#markSupported()
	 */
	@Override
	public boolean markSupported() {
		return src.markSupported();
	}

	/**
	 * Return the read bytes' count from this input stream.
	 * 
	 * @return the read bytes' count from this input stream
	 */
	public long count() {
		return counter.get();
	}

}
