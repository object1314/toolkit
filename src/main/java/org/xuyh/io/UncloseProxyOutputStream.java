/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An {@link OutputStream} proxy on a source one but close action has no effect.
 * Used on some partial writing block who need do close action but the origin
 * output stream would do write action then.
 * 
 * @author XuYanhang
 * @since 2020-10-18
 *
 */
public class UncloseProxyOutputStream extends OutputStream {

	/** Source output stream for this proxy */
	private final OutputStream source;

	/**
	 * Initialize this proxy output stream
	 * 
	 * @param source required source out put stream for this proxy and
	 *               <code>null</code> not allowed
	 */
	public UncloseProxyOutputStream(OutputStream source) {
		super();
		if (source == null)
			throw new NullPointerException();
		this.source = source;
	}

	/**
	 * Returns the source output stream.
	 * 
	 * @return the {@link #source}
	 */
	public OutputStream getSource() {
		return source;
	}

	/**
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		source.write(b);
	}

	/**
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		source.write(b);
	}

	/**
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		source.write(b, off, len);
	}

	/**
	 * @see java.io.OutputStream#flush()
	 */
	@Override
	public void flush() throws IOException {
		source.flush();
	}

	/**
	 * Close action do only flush action but doesn't close the source output stream.
	 * 
	 * @see java.io.OutputStream# close()
	 */
	@Override
	public void close() throws IOException {
		source.flush();
	}

}
