package org.xuyh.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This <code>BitWriter</code> contains an internal buffer that contains bytes
 * that storage the data into the writer. An internal counter keeps track of the
 * next byte to be supplied by the writes method.
 *
 * @see java.io.ByteArrayOutputStream
 * @author XuYanhang
 * @since 2020-10-22
 */
public class BitWriter implements java.io.Serializable {

	/**
	 * Serialize this writer on buffer bytes and its region
	 */
	private static final long serialVersionUID = -2147972387810744900L;

	/**
	 * Buffer data to write. This array might be changed when grow.
	 */
	private byte[] buf;

	/**
	 * Written size of the writer.
	 */
	private int size;

	/**
	 * Current position of the writer.
	 */
	private int pos;

	/**
	 * Create this default writer in a specified capacity
	 */
	public BitWriter() {
		super();
		buf = new byte[32];
	}

	/**
	 * Create this writer in a specified capacity
	 * 
	 * @param capacity the specified capacity value
	 */
	public BitWriter(int capacity) {
		super();
		buf = new byte[capacity];
	}

	/**
	 * Create this writer in a specified byte array. The seeds array not copied
	 * until the write methods cause the grow of this writer.
	 * 
	 * @param seeds the seed bytes array
	 */
	public BitWriter(byte[] seeds) {
		super();
		if (null == seeds)
			throw new NullPointerException();
		buf = seeds;
	}

	/**
	 * Ensure the minimum capacity of this writer. Grow up the buffer of the writer,
	 * if necessary, to ensure that it can hold at least the number of bytes
	 * specified by the minimum capacity argument.
	 * 
	 * @param minCapacity the minimum capacity of this writer
	 * @return this
	 */
	public BitWriter ensureCapacity(int minCapacity) {
		int maxCapacity = Integer.MAX_VALUE - 8;
		if (minCapacity < 0 || minCapacity > maxCapacity)
			throw new InternalError();
		int oldCapacity = buf.length;
		if (minCapacity > oldCapacity) {
			// Grown
			int newCapacity;
			if (minCapacity < 32) {
				newCapacity = 32;
			} else {
				if (oldCapacity <= 1024)
					newCapacity = oldCapacity << 1;
				else
					newCapacity = oldCapacity + (oldCapacity >> 1);
				if (newCapacity > maxCapacity)
					newCapacity = maxCapacity;
				if (newCapacity < minCapacity)
					newCapacity = minCapacity;
			}
			byte[] newBuf = new byte[newCapacity];
			System.arraycopy(buf, 0, newBuf, 0, size);
			buf = newBuf;
		}
		return this;
	}

	/**
	 * Returns the size of the writer. It's the total range size in this writer.
	 * 
	 * @return written size of the writer
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns current position of the writer. It's the place where the put method
	 * happens.
	 * 
	 * @return current position
	 */
	public int position() {
		return pos;
	}

	/**
	 * Reset current position as specified of the writer.
	 * 
	 * @param position position to set
	 * @return this
	 */
	public BitWriter position(int position) {
		if (position < 0)
			throw new IndexOutOfBoundsException();
		ensureRequire(position - pos, true);
		checkSize(pos = position);
		return this;
	}

	/**
	 * Skip specified size on the position. Positive means forward and negative
	 * means backward. On skip bytes, value not changed here.
	 * 
	 * @param size the bytes size to skip
	 * @return this
	 */
	public BitWriter skip(int size) {
		ensureRequire(size, true);
		checkSize(pos += size);
		return this;
	}

	/**
	 * Reset the size of the writer on current position. The {@link #size} of the
	 * writer might be changed only when current position is smaller than the size.
	 * 
	 * @return this
	 */
	public BitWriter markEnd() {
		size = pos;
		return this;
	}

	/**
	 * Fill all bytes in specified value.
	 * 
	 * @param b    the byte value to fill
	 * @param size the bytes size to fill
	 * @return this
	 */
	public BitWriter fillBytes(int b, int size) {
		ensureRequire(size, false);
		checkSize(pos += Bits.fill(buf, pos, b, size));
		return this;
	}

	/**
	 * Set the bit at specified bit position in current byte position. The position
	 * never move here.
	 * 
	 * @param bitPos bit position to set from 0 to 7
	 * @param bitVal bit value to set of 0 or 1
	 * @return this
	 */
	public BitWriter setBit(int bitPos, int bitVal) {
		ensureRequire(1, false);
		Bits.putBit(buf, pos, bitPos, bitVal);
		return this;
	}

	/**
	 * Write a byte into this write. The position moves forward <code>1</code> byte.
	 * 
	 * @param val value to write
	 * @return this
	 */
	public BitWriter putByte(int b) {
		ensureRequire(1, false);
		buf[pos++] = (byte) b;
		checkSize(pos);
		return this;
	}

	/**
	 * Write a boolean into this write. The position moves forward <code>1</code>
	 * byte.
	 * 
	 * @param val value to write
	 * @return this
	 */
	public BitWriter putBool(boolean b) {
		ensureRequire(1, false);
		buf[pos++] = b ? (byte) 1 : (byte) 0;
		checkSize(pos);
		return this;
	}

	/**
	 * Write a short into this write. The position moves forward <code>2</code>
	 * bytes.
	 * 
	 * @param val       value to write
	 * @param bigEndian flag to specify the write method used big endian mode or
	 *                  small endian mode
	 * @return this
	 */
	public BitWriter putShort(int val, boolean bigEndian) {
		ensureRequire(2, false);
		pos += bigEndian ? Bits.putShort(buf, pos, val) : Bits.putRShort(buf, pos, val);
		checkSize(pos);
		return this;
	}

	/**
	 * Write a char into this write. The position moves forward <code>2</code>
	 * bytes.
	 * 
	 * @param val       value to write
	 * @param bigEndian flag to specify the write method used big endian mode or
	 *                  small endian mode
	 * @return this
	 */
	public BitWriter putChar(int c, boolean bigEndian) {
		ensureRequire(2, false);
		pos += bigEndian ? Bits.putChar(buf, pos, c) : Bits.putRChar(buf, pos, c);
		checkSize(pos);
		return this;
	}

	/**
	 * Write a integer into this write. The position moves forward <code>4</code>
	 * bytes.
	 * 
	 * @param val       value to write
	 * @param bigEndian flag to specify the write method used big endian mode or
	 *                  small endian mode
	 * @return this
	 */
	public BitWriter putInt(int val, boolean bigEndian) {
		ensureRequire(4, false);
		pos += bigEndian ? Bits.putInt(buf, pos, val) : Bits.putRInt(buf, pos, val);
		checkSize(pos);
		return this;
	}

	/**
	 * Write a float into this write. The position moves forward <code>4</code>
	 * bytes.
	 * 
	 * @param val       value to write
	 * @param bigEndian flag to specify the write method used big endian mode or
	 *                  small endian mode
	 * @return this
	 */
	public BitWriter putFloat(float val, boolean bigEndian) {
		ensureRequire(4, false);
		pos += bigEndian ? Bits.putFloat(buf, pos, val) : Bits.putRFloat(buf, pos, val);
		checkSize(pos);
		return this;
	}

	/**
	 * Write a long into this write. The position moves forward <code>8</code>
	 * bytes.
	 * 
	 * @param val       value to write
	 * @param bigEndian flag to specify the write method used big endian mode or
	 *                  small endian mode
	 * @return this
	 */
	public BitWriter putLong(long val, boolean bigEndian) {
		ensureRequire(8, false);
		pos += bigEndian ? Bits.putLong(buf, pos, val) : Bits.putRLong(buf, pos, val);
		checkSize(pos);
		return this;
	}

	/**
	 * Write a double into this write. The position moves forward <code>8</code>
	 * bytes.
	 * 
	 * @param val       value to write
	 * @param bigEndian flag to specify the write method used big endian mode or
	 *                  small endian mode
	 * @return this
	 */
	public BitWriter putDouble(double val, boolean bigEndian) {
		ensureRequire(8, false);
		pos += bigEndian ? Bits.putDouble(buf, pos, val) : Bits.putRDouble(buf, pos, val);
		checkSize(pos);
		return this;
	}

	/**
	 * Write a value on integer type into this write. The position moves forward
	 * <code>size</code> bytes.
	 * 
	 * @param val       value to write
	 * @param size      byte size to write of the value(bit size is the 8 times of
	 *                  the size)
	 * @param bigEndian flag to specify the write method used big endian mode or
	 *                  small endian mode
	 * @return this
	 */
	public BitWriter putOnInt(int val, int size, boolean bigEndian) {
		ensureRequire(size, false);
		pos += bigEndian ? Bits.putInt(buf, pos, val, size) : Bits.putRInt(buf, pos, val, size);
		checkSize(pos);
		return this;
	}

	/**
	 * Write a value on long type into this write. The position moves forward
	 * <code>size</code> bytes.
	 * 
	 * @param val       value to write
	 * @param size      byte size to write of the value(bit size is the 8 times of
	 *                  the size)
	 * @param bigEndian flag to specify the write method used big endian mode or
	 *                  small endian mode
	 * @return this
	 */
	public BitWriter putOnLong(long val, int size, boolean bigEndian) {
		ensureRequire(size, false);
		pos += bigEndian ? Bits.putLong(buf, pos, val, size) : Bits.putRLong(buf, pos, val, size);
		checkSize(pos);
		return this;
	}

	/**
	 * Write some bytes into this writer. The position moves forward
	 * <code>b.length</code> bytes.
	 * 
	 * @param b   source bytes to write
	 * @param off write offset in the source bytes
	 * @param len write length in the source bytes
	 * @return this
	 */
	public BitWriter putBytes(byte[] b) {
		ensureRequire(b.length, false);
		System.arraycopy(b, 0, buf, pos, b.length);
		checkSize(pos += b.length);
		return this;
	}

	/**
	 * Write some bytes into this writer. The position moves forward
	 * <code>len</code> bytes.
	 * 
	 * @param b   source bytes to write
	 * @param off write offset in the source bytes
	 * @param len write length in the source bytes
	 * @return this
	 */
	public BitWriter putBytes(byte[] b, int off, int len) {
		if (off < 0 || off + len > b.length || off + len < off)
			throw new IndexOutOfBoundsException();
		ensureRequire(len, false);
		System.arraycopy(b, off, buf, pos, len);
		checkSize(pos += len);
		return this;
	}

	/**
	 * Write a char sequence as bytes into this writer. After write action, the
	 * write size can be got on distance of the position.
	 * 
	 * @see #putUTF(CharSequence, int, int)
	 * @param cs char sequence to write
	 * @return this
	 */
	public BitWriter putUTF(CharSequence cs) {
		ensureRequire(Bits.measureUTFBytes(cs), false);
		checkSize(pos += Bits.putUTF(buf, pos, cs));
		return this;
	}

	/**
	 * Write a char sequence as bytes into this writer. After write action, the
	 * write size can be got on distance of the position.
	 * 
	 * @param cs   char sequence to write
	 * @param coff char offset to write
	 * @param clen char length to write
	 * @return this
	 */
	public BitWriter putUTF(CharSequence cs, int coff, int clen) {
		ensureRequire(Bits.measureUTFBytes(cs, coff, clen), false);
		checkSize(pos += Bits.putUTF(buf, pos, cs, coff, clen));
		return this;
	}

	/**
	 * Write an object as bytes on JDK serialization into this writer. After write
	 * action, the write size can get on distance of the position.
	 * 
	 * @param obj the object to write
	 * @return this
	 * @throws IllegalStateException when the object can not be serialized
	 */
	public BitWriter putObject(Object obj) {
		try (BitWriterOutputStream out = new BitWriterOutputStream(this)) {
			IOBits.putObject(out, obj);
		}
		checkSize(pos);
		return this;
	}

	/**
	 * Output the written byte array in {@link #size} length. Original byte buffer
	 * returned only when the {@link #size} is same with the buffer length.
	 * 
	 * @return bytes array in {@link #size} length where written all data in the
	 *         writer
	 */
	public byte[] out() {
		int len = size;
		if (len == buf.length)
			return buf;
		byte[] data = new byte[len];
		System.arraycopy(buf, 0, data, 0, len);
		return data;
	}

	/** Ensure there is enough bytes to write next bytes in size */
	private void ensureRequire(int requireSize, boolean allowBack) {
		if (requireSize == 0)
			return;
		if (requireSize < 0) {
			if (!allowBack || -pos > requireSize)
				throw new IndexOutOfBoundsException();
		} else {
			ensureCapacity(pos + requireSize);
		}
	}

	/** Check the {@link #size} value, shouldn't be smaller than {@link #pos} */
	private void checkSize(int ignore) {
		if (pos > size)
			size = pos;
	}

	/**
	 * This <code>OutputStream</code> does the operation on the writer but thread
	 * safe for each. However, concurrent update on writer and the
	 * <tt>OutputStream</tt> might cause state invalid.
	 * 
	 * @return output stream on this writer
	 */
	public OutputStream openOutputStream() {
		return new BitWriterOutputStream(this);
	}

	/**
	 * OutputStream on writer
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class BitWriterOutputStream extends OutputStream {

		/**
		 * Writer on the output stream
		 */
		private BitWriter writer;

		/**
		 * Initialize this output stream
		 * 
		 * @param writer the {@link #writer}
		 */
		BitWriterOutputStream(BitWriter writer) {
			super();
			this.writer = writer;
		}

		/**
		 * Writes the specified byte to this <tt>OutputStream</tt>.
		 * 
		 * @param b the byte to be written
		 */
		@Override
		public synchronized void write(int b) throws IOException {
			ensureOpen();
			writer.putByte(b);
		}

		/**
		 * Writes <code>len</code> bytes from the specified byte array starting at
		 * offset <code>off</code> to this byte array buffer.
		 *
		 * @param b   the data.
		 * @param off the start offset in the data.
		 * @param len the number of bytes to write.
		 */
		@Override
		public synchronized void write(byte[] b, int off, int len) throws IOException {
			ensureOpen();
			if (off < 0 || off > off + len || off + len > b.length)
				throw new IndexOutOfBoundsException();
			writer.putBytes(b, off, len);
		}

		/**
		 * Flushing this <tt>OutputStream</tt> has no effect. The methods in this class
		 * can be called after the stream has been flush without generating an
		 * <tt>IOException</tt>.
		 */
		@Override
		public void flush() {
			// Do Nothing;
		}

		/**
		 * Closing this <tt>OutputStream</tt>. The methods in this class can be called
		 * after the stream has been closed without generating an <tt>IOException</tt>.
		 */
		@Override
		public synchronized void close() {
			if (null == writer)
				return;
			writer = null;
		}

		/** Checks to make sure that the stream has not been closed */
		private void ensureOpen() throws IOException {
			if (null == writer)
				throw new IOException("Closed");
		}

	}

}
