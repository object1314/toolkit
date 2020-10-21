package org.xuyh.io;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An {@link Reader} proxy on a source one but close action has no effect. Used
 * on some partial reading block who need do close action but the origin reader
 * would do read action then.
 * <p>
 * There exists a {@link #counter} to count the read chars from this writer. Use
 * {@link #count()} to get it.
 * 
 * @author XuYanhang
 * @since 2020-10-21
 *
 */
public class UncloseProxyReader extends Reader {

	/** Source reader for this proxy */
	private final Reader src;

	/** Counter to count write bytes */
	private final AtomicLong counter;

	/**
	 * Initialize this proxy reader
	 * 
	 * @param source required source reader for this proxy and <code>null</code> not
	 *               allowed
	 */
	public UncloseProxyReader(Reader source) {
		super();
		if (source == null)
			throw new NullPointerException();
		this.src = source;
		this.counter = new AtomicLong(0);
	}

	/**
	 * Returns the source reader.
	 * 
	 * @return the {@link #src}
	 */
	public Reader source() {
		return src;
	}

	/**
	 * @see java.io.Reader#read(java.nio.CharBuffer)
	 */
	@Override
	public int read(CharBuffer target) throws IOException {
		int v = src.read(target);
		if (v > -1)
			counter.getAndAdd(v);
		return v;
	}

	/**
	 * @see java.io.Reader#read()
	 */
	@Override
	public int read() throws IOException {
		int v = src.read();
		if (v > -1)
			counter.getAndAdd(1);
		return v;
	}

	/**
	 * @see java.io.Reader#read(char[])
	 */
	@Override
	public int read(char[] cbuf) throws IOException {
		int v = src.read(cbuf);
		if (v > -1)
			counter.getAndAdd(v);
		return v;
	}

	/**
	 * @see java.io.Reader#read(char[], int, int)
	 */
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int v = src.read(cbuf, off, len);
		if (v > -1)
			counter.getAndAdd(v);
		return v;
	}

	/**
	 * @see java.io.Reader#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		long v = src.skip(n);
		counter.getAndAdd(v);
		return v;
	}

	/**
	 * @see java.io.Reader#ready()
	 */
	@Override
	public boolean ready() throws IOException {
		return src.ready();
	}

	/**
	 * @see java.io.Reader#markSupported()
	 */
	@Override
	public boolean markSupported() {
		return src.markSupported();
	}

	/**
	 * @see java.io.Reader#mark(int)
	 */
	@Override
	public void mark(int readAheadLimit) throws IOException {
		src.mark(readAheadLimit);
	}

	/**
	 * @see java.io.Reader#reset()
	 */
	@Override
	public void reset() throws IOException {
		src.reset();
	}

	/**
	 * Close action has no effects.
	 * 
	 * @see java.io.Reader#close()
	 */
	@Override
	public void close() throws IOException {
		// Do nothing here
	}

	/**
	 * Return the read chars' count from this reader.
	 * 
	 * @return the read chars' count from this reader
	 */
	public long count() {
		return counter.get();
	}

}
