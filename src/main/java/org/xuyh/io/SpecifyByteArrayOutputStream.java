/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.io;

import java.io.OutputStream;

/**
 * A {@link SpecifyByteArrayOutputStream} is an {@link OutputStream} contains an
 * internal buffer that contains bytes that may be written into the stream. An
 * internal counter keeps track of the next byte to be reset by the
 * <code>write</code> method.
 * <p>
 * Write methods might be failed when there are not more bytes available to
 * write into the output stream. If write a byte array, the action ends on an
 * <code>RuntimeException</code> when required space not enough, that is has
 * reach the end. For a byte array writing, the pre-data write into the
 * {@link #buf}.
 * <p>
 * Closing a {@link SpecifyByteArrayOutputStream} has no effect. The methods in
 * this class can be called after the stream has been closed without generating
 * an {@link java.io.IOException}.
 *
 * @author XuYanhang
 * @see java.io.ByteArrayInputStream
 * @since 2020-10-18
 */
public class SpecifyByteArrayOutputStream extends OutputStream implements java.io.Serializable {

    /**
     * This {@link OutputStream} can be serialized
     */
    private static final long serialVersionUID = -5545363895085450297L;

    /**
     * An array of bytes that was operated on writing actions
     */
    private final byte[] buf;
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
     * Creates {@link SpecifyByteArrayOutputStream} that uses <code>buf</code> as
     * its {@link #buf}. The initial value of {@link #off} is 0, The initial value
     * of {@link #cursor} is 0 and the initial value of {@link #end} is
     * <code>buf.length</code>. The buffer array is not copied.
     *
     * @param buf the operated buffer
     */
    public SpecifyByteArrayOutputStream(byte[] buf) {
        super();
        this.buf = buf;
        this.off = 0;
        this.end = buf.length;
        this.cursor = 0;
    }

    /**
     * Creates {@link SpecifyByteArrayOutputStream} that uses <code>buf</code> as
     * its {@link #buf}. The initial value of {@link #off} is <code>off</code>, The
     * initial value of {@link #cursor} is <code>off</code> and the initial value of
     * {@link #end} is <code>off+len</code>. The buffer array is not copied.
     *
     * @param buf the operated buffer
     * @param off the position in the buffer of the first byte to write
     * @param len the maximum number of bytes to write into the buffer
     * @throws IndexOutOfBoundsException if the given range is not in the buffer
     */
    public SpecifyByteArrayOutputStream(byte[] buf, int off, int len) {
        super();
        int end = off + len;
        if (off < 0 || end > buf.length || off > end) throw new IndexOutOfBoundsException();
        this.buf = buf;
        this.off = off;
        this.end = end;
        this.cursor = off;
    }

    /**
     * Writes the specified byte to this output stream. The general contract for
     * <code>write</code> is that one byte is written to the output stream. The byte
     * to be written is the eight low-order bits of the argument <code>b</code>. The
     * 24 high-order bits of <code>b</code> are ignored.
     *
     * @throws IllegalStateException if the stream reaches the endF
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public synchronized void write(int b) {
        if (cursor >= end) throw new IllegalStateException("EOF");
        buf[cursor++] = (byte) b;
    }

    /**
     * Writes most <code>b.length</code> bytes from the specified byte array to this
     * output stream. The general contract for <code>write(b)</code> is that it
     * should have exactly the same effect as the call
     * {@link #write(byte[], int, int)}.
     * <p>
     * If there is no more bytes can be written in the buffer, the action has nothing
     * effect. If the remaining bytes is less than the needed bytes of
     * <code>b.length</code>, then only the remaining bytes can be written
     * successfully.
     *
     * @throws IllegalStateException if the stream space not enough
     * @see java.io.OutputStream#write(byte[])
     */
    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    /**
     * Writes most <code>len</code> bytes from the specified byte array starting at
     * offset <code>off</code> to this output stream. The general contract for
     * <code>write(b, off, len)</code> is that some bytes in the array
     * <code>b</code> are written to the output stream in order; element
     * <code>b[off]</code> is the first byte written and <code>b[off+len-1]</code>
     * is the last byte written by this operation.
     *
     * @throws IndexOutOfBoundsException if the input array range is out
     * @throws IllegalStateException     if the stream space not enough
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    @Override
    public synchronized void write(byte[] b, int off, int len) {
        if (off < 0 || (off + len) > b.length || (off + len) < off) throw new IndexOutOfBoundsException();
        if (len <= 0) return;
        boolean error = false;
        if (len > end - cursor) {
            len = end - cursor;
            error = true;
        }
        System.arraycopy(b, off, buf, cursor, len);
        cursor += len;
        if (error) throw new IllegalStateException("EOF");
    }

    /**
     * Flush a {@link SpecifyByteArrayOutputStream} has no effect. The methods in
     * this class can be called after the stream has been closed without generating
     * an <tt>IOException</tt>.
     *
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() {
        // Do nothing
    }

    /**
     * Close a {@link SpecifyByteArrayOutputStream} has no effect. The methods in
     * this class can be called after the stream has been closed without generating
     * an <tt>IOException</tt>.
     *
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() {
        // Do nothing
    }

    /**
     * Reset the {@link #cursor} to {@link #off} so that next write position will be
     * at the {@link #off} of the {@link #buf}.
     */
    public synchronized void reset() {
        this.cursor = off;
    }

    /**
     * Returns the next write position who ranges from {@link #off} to {@link #end}.
     *
     * @return the {@link #cursor} of next write position
     */
    public int tell() {
        return this.cursor;
    }

    /**
     * Returns bytes count can be written into the {@link #buf}.
     *
     * @return how many bytes remaining on write action
     */
    public int available() {
        return this.end - this.cursor;
    }

    /**
     * Returns the completed write bytes in the operated buffer.
     *
     * @return the successfully write bytes count
     */
    public int completed() {
        return this.cursor - this.off;
    }

    /**
     * Returns the beginning write offset of the output stream at operated buffer.
     *
     * @return the {@link #off}
     */
    public int offset() {
        return this.off;
    }

    /**
     * Returns the origin operated byte array, not copied.
     *
     * @return the {@link #buf}
     */
    public byte[] source() {
        return this.buf;
    }

    /**
     * Converts the buffer's contents into a string decoding bytes using the
     * platform's default character set. The length of the new <tt>String</tt> is a
     * function of the character set, and hence may not be equal to the size of the
     * buffer.
     *
     * <p>
     * This method always replaces malformed-input and unmappable-character
     * sequences with the default replacement string for the platform's default
     * character set. The {@linkplain java.nio.charset.CharsetDecoder} class should
     * be used when more control over the decoding process is required.
     *
     * @return String decoded from the buffer's contents.
     */
    @Override
    public synchronized String toString() {
        return new String(buf, off, cursor);
    }

    /**
     * Converts the buffer's contents into a string by decoding the bytes using the
     * named {@link java.nio.charset.Charset charset}. The length of the new
     * <tt>String</tt> is a function of the charset, and hence may not be equal to
     * the length of the byte array.
     *
     * <p>
     * This method always replaces malformed-input and unmappable-character
     * sequences with this charset's default replacement string. The
     * {@link java.nio.charset.CharsetDecoder} class should be used when more
     * control over the decoding process is required.
     *
     * @param charset the name of a supported {@link java.nio.charset.Charset charset}
     * @return String decoded from the buffer's contents.
     * @throws java.io.UnsupportedEncodingException If the named charset is not
     *                                              supported
     */
    public synchronized String toString(String charset) throws java.io.UnsupportedEncodingException {
        return new String(buf, off, cursor, charset);
    }
}
