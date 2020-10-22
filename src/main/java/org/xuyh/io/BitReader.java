package org.xuyh.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 * This <code>BitReader</code> contains an internal buffer that contains bytes
 * that may be read from the reader. An internal counter keeps track of the next
 * byte to be supplied by the reads method. If the counter reaches the end, Any
 * read methods may cause a <tt>IndexOutOfBoundsException</tt>.
 *
 * @see java.io.ByteArrayInputStream
 * @author XuYanhang
 * @since 2020-10-22
 *
 */
public class BitReader implements java.io.Serializable {

	/**
	 * Serialize this reader on buffer bytes and its region
	 */
	private static final long serialVersionUID = -2504214987158029954L;

	/**
	 * Source data to read
	 */
	private byte[] buf;

	/**
	 * The offset index to read the data(inclusive)
	 */
	private int off;

	/**
	 * The end index to read the data(exclusive)
	 */
	private int end;

	/**
	 * Cursor on the reader
	 */
	private int pos;

	/**
	 * Create the reader in a byte array as buffer. The buffer array is not copied.
	 * 
	 * @param buf the bytes to read, <code>null</code> not allowed.
	 */
	public BitReader(byte... buf) {
		super();
		this.buf = buf;
		this.off = 0;
		this.end = buf.length;
		this.pos = 0;
	}

	/**
	 * Creates the reader that uses <code>buf</code> as its buffer array. The
	 * initial value of <code>pos</code> is <code>off</code> and the initial value
	 * of <code>end</code> is the <code>off+len</code>. The buffer array is not
	 * copied.
	 *
	 * @param buf the input buffer.
	 * @param off the offset in the buffer of the first byte to read
	 * @param len the number of bytes to read from the buffer
	 * 
	 * @throws IndexOutOfBoundsException of the given data range out of the range of
	 *                                   <code>[0, buf.length)</code>
	 */
	public BitReader(byte[] buf, int off, int len) {
		super();
		this.buf = buf;
		this.off = off;
		this.end = off + len;
		this.pos = off;
		if (off < 0 || end > buf.length || end < off)
			throw new IndexOutOfBoundsException();
	}

	/**
	 * Returns the available bytes to read
	 * 
	 * @return available bytes
	 */
	public int available() {
		return end - pos;
	}

	/**
	 * Returns the readable size in the buffer data.
	 * 
	 * @return the readable size
	 */
	public int size() {
		return end - off;
	}

	/**
	 * Returns current position in the buffer data
	 * 
	 * @returns the {@link #pos position} in the buffer data
	 */
	public int position() {
		return this.pos;
	}

	/**
	 * Reset the position as specified position, whose value must be in
	 * <code>[{@link #off}, {@link #end}]</code>.
	 * 
	 * @param position this {@link #pos position} to set
	 * @return this
	 */
	public BitReader position(int position) {
		if (position < off || position > end)
			throw new IllegalArgumentException();
		this.pos = position;
		return this;
	}

	/**
	 * Skip <code>size</code> bytes on the reader. Negative <code>size</code> value
	 * points the direction as backward, positive value points forward and zero
	 * value has no effect.
	 * 
	 * @param size absolute value is skip read bytes count and sign is the direction
	 * @return this
	 */
	public BitReader skip(int size) {
		ensureRequire(size, true);
		pos += size;
		return this;
	}

	/**
	 * Return bit value of 0 or 1 at specified position from lower to higher mapped
	 * on 0 to 7. Position never moved here.
	 * 
	 * @param bitPos the bit position in current bit
	 * @return the bit value
	 */
	public int getBit(int bitPos) {
		ensureRequire(1, false);
		return Bits.getBit(buf[pos] & 0Xff, bitPos);
	}

	/**
	 * Returns next byte on boolean and position moved 1.
	 * 
	 * @return next boolean value
	 */
	public boolean nextBool() {
		ensureRequire(1, false);
		return (buf[pos++] & 0X1) != 0;
	}

	/**
	 * Returns next byte on value and position moved 1.
	 * 
	 * @return next byte value
	 */
	public byte nextByte() {
		ensureRequire(1, false);
		return buf[pos++];
	}

	/**
	 * Returns next byte on unsigned value from 0 to 255 and position moved 1.
	 * 
	 * @return next unsigned byte value
	 */
	public int nextUnsignedByte() {
		ensureRequire(1, false);
		return buf[pos++] & 0Xff;
	}

	/**
	 * Returns next bytes on short value and position moved 2.
	 * 
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * @return next short value
	 */
	public short nextShort(boolean bigEndian) {
		ensureRequire(2, false);
		pos += 2;
		return bigEndian ? Bits.getShort(buf, pos - 2) : Bits.getRShort(buf, pos - 2);
	}

	/**
	 * Returns next bytes on unsigned short value and position moved 2.
	 * 
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * 
	 * @return next unsigned short value
	 */
	public int nextUnsignedShort(boolean bigEndian) {
		ensureRequire(2, false);
		pos += 2;
		return bigEndian ? Bits.getUShort(buf, pos - 2) : Bits.getRUShort(buf, pos - 2);
	}

	/**
	 * Returns next bytes on char value and position moved 2.
	 * 
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * @return next char value
	 */
	public char nextChar(boolean bigEndian) {
		ensureRequire(2, false);
		pos += 2;
		return bigEndian ? Bits.getChar(buf, pos - 2) : Bits.getRChar(buf, pos - 2);
	}

	/**
	 * Returns next bytes on integer value and position moved 4.
	 * 
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * 
	 * @return next integer value
	 */
	public int nextInt(boolean bigEndian) {
		ensureRequire(4, false);
		pos += 4;
		return bigEndian ? Bits.getInt(buf, pos - 4) : Bits.getRInt(buf, pos - 4);
	}

	/**
	 * Returns next bytes on unsigned integer value and position moved 4.
	 * 
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * 
	 * @return next unsigned integer value
	 */
	public long nextUnsignedInt(boolean bigEndian) {
		ensureRequire(4, false);
		pos += 4;
		return bigEndian ? Bits.getUInt(buf, pos - 4) : Bits.getRUInt(buf, pos - 4);
	}

	/**
	 * Returns next bytes on float value and position moved 4.
	 * 
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * 
	 * @return next float value
	 */
	public float nextFloat(boolean bigEndian) {
		ensureRequire(4, false);
		pos += 4;
		return bigEndian ? Bits.getFloat(buf, pos - 4) : Bits.getRFloat(buf, pos - 4);
	}

	/**
	 * Returns next bytes on long value and position moved 8.
	 * 
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * 
	 * @return next long value
	 */
	public long nextLong(boolean bigEndian) {
		ensureRequire(8, false);
		pos += 8;
		return bigEndian ? Bits.getLong(buf, pos - 8) : Bits.getRLong(buf, pos - 8);
	}

	/**
	 * Returns next bytes on double value and position moved 8.
	 * 
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * 
	 * @return next double value
	 */
	public double nextDouble(boolean bigEndian) {
		ensureRequire(8, false);
		pos += 8;
		return bigEndian ? Bits.getDouble(buf, pos - 8) : Bits.getRDouble(buf, pos - 8);
	}

	/**
	 * Returns next bytes on integer value and position moved specified size.
	 * 
	 * @param size      specified the size of the bytes to read
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * 
	 * @return next integer value
	 */
	public int nextValOnInt(int size, boolean bigEndian) {
		ensureRequire(size, false);
		pos += size;
		return bigEndian ? Bits.getInt(buf, pos - size, size) : Bits.getRInt(buf, pos - size, size);
	}

	/**
	 * Returns next bytes on long value and position moved specified size.
	 * 
	 * @param size      specified the size of the bytes to read
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * 
	 * @return next long value
	 */
	public long nextValOnLong(int size, boolean bigEndian) {
		ensureRequire(size, false);
		pos += size;
		return bigEndian ? Bits.getLong(buf, pos - size, size) : Bits.getRLong(buf, pos - size, size);
	}

	/**
	 * Returns next bytes on bytes array and position moved specified size.
	 * 
	 * @param size specified the size of the bytes to read
	 * 
	 * @return next bytes
	 */
	public byte[] nextBytes(int size) {
		ensureRequire(size, false);
		pos += size;
		return Bits.getBytes(buf, pos - size, size);
	}

	/**
	 * Returns next bytes on string value in UTF-8 encoding and position moved
	 * specified size.
	 * 
	 * @param size specified the size of the bytes to read
	 * 
	 * @return next string in UTF-8
	 */
	public String nextUTF(int size) {
		ensureRequire(size, false);
		pos += size;
		return Bits.getUTF(buf, pos - size, size);
	}

	/**
	 * Returns next bytes on Object value encoding by JDK serialization and position
	 * moved specified size.
	 * 
	 * @param size specified the size of the bytes to read
	 * 
	 * @return next Object
	 */
	public <T> T nextObject(int size) {
		ensureRequire(size, false);
		int toPos = pos + size;
		T obj;
		try (BitReaderInputStream in = new BitReaderInputStream(this, pos, toPos)) {
			obj = IOBits.getObject(in);
		} catch (RuntimeException e) {
			pos = toPos;
			throw e;
		}
		pos = toPos;
		return obj;
	}

	/**
	 * Read next bytes on integer value and position moved specified size.
	 * 
	 * @param size      specified the size of the bytes to read
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * @param consumer  consumer to cost this value
	 * 
	 * @return this
	 */
	public BitReader readOnInt(int size, boolean bigEndian, IntConsumer consumer) {
		consumer.accept(nextValOnInt(size, bigEndian));
		return this;
	}

	/**
	 * Read next bytes on long integer value and position moved specified size.
	 * 
	 * @param size      specified the size of the bytes to read
	 * @param bigEndian flag to specify the read method used big endian mode or
	 *                  small endian mode
	 * @param consumer  consumer to cost this value
	 * 
	 * @return this
	 */
	public BitReader readOnLong(int size, boolean bigEndian, LongConsumer consumer) {
		consumer.accept(nextValOnLong(size, bigEndian));
		return this;
	}

	/**
	 * Read next bytes on bytes array value and position moved specified size.
	 * 
	 * @param size     specified the size of the bytes to read
	 * @param consumer consumer to cost this value
	 * 
	 * @return this
	 */
	public BitReader readBytes(int size, Consumer<byte[]> consumer) {
		consumer.accept(nextBytes(size));
		return this;
	}

	/**
	 * Read next bytes on String value encoding by UTF-8 and position moved
	 * specified size.
	 * 
	 * @param size     specified the size of the bytes to read
	 * @param consumer consumer to cost this value
	 * 
	 * @return this
	 */
	public BitReader readUTF(int size, Consumer<String> consumer) {
		consumer.accept(nextUTF(size));
		return this;
	}

	/**
	 * Read next bytes on Object value encoding by JDK serialization and position
	 * moved specified size.
	 * 
	 * @param size     specified the size of the bytes to read
	 * @param consumer consumer to cost this value
	 * 
	 * @return this
	 */
	public <T> BitReader readObject(int size, Consumer<T> consumer) {
		consumer.accept(nextObject(size));
		return this;
	}

	/** Ensure there is enough bytes to read next bytes in size */
	private void ensureRequire(int requireSize, boolean allowBack) {
		if (requireSize == 0)
			return;
		if (requireSize > 0) {
			if (end - pos < requireSize)
				throw new IndexOutOfBoundsException();
		} else {
			if (!allowBack || off - pos > requireSize)
				throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * This <code>InputStream</code> does the operation on the reader but thread
	 * safe for each. However, concurrent update on reader and the
	 * <tt>InputStream</tt> might cause state invalid. Besides, the input stream
	 * support {@link InputStream#skip(long) skip} operation on negative skip value
	 * and mark operation.
	 * 
	 * @return input stream on this reader
	 */
	public InputStream openInputStream() {
		return new BitReaderInputStream(this, off, end);
	}

	/**
	 * InputStream on reader
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class BitReaderInputStream extends InputStream {

		/**
		 * Reader on the input stream
		 */
		BitReader reader;

		/**
		 * The offset index to read the data(inclusive), not smaller than the offset in
		 * reader
		 */
		int off;

		/**
		 * The end index to read the data(exclusive), not larger than the end in the
		 * reader
		 */
		int end;

		/**
		 * Mark on the reader
		 */
		int mark;

		/**
		 * Initialize this input stream
		 * 
		 * @param reader the {@link #reader}
		 * @param off    offset between {@link BitReader#off} and {@link BitReader#pos}
		 * @param end    end between {@link BitReader#pos} and {@link BitReader#end}
		 */
		BitReaderInputStream(BitReader reader, int off, int end) {
			super();
			this.reader = reader;
			this.off = off;
			this.end = end;
			this.mark = reader.pos;
		}

		/**
		 * Reads the next byte of data from this input stream. The value byte is
		 * returned as an <code>int</code> in the range <code>0</code> to
		 * <code>255</code>. If no byte is available because the end of the stream has
		 * been reached, the value <code>-1</code> is returned.
		 * <p>
		 * This <code>read</code> method cannot block.
		 *
		 * @return the next byte of data, or <code>-1</code> if the end of the stream
		 *         has been reached.
		 */
		@Override
		public synchronized int read() throws IOException {
			ensureOpen();
			int pos = reader.pos;
			int b = pos < end ? reader.buf[pos++] & 0Xff : -1;
			reader.pos = pos;
			return b;
		}

		/**
		 * Reads up to <code>len</code> bytes of data into an array of bytes from this
		 * input stream. If <code>available</code> is zero, then <code>-1</code> is
		 * returned to indicate end of file. Otherwise, the number <code>k</code> of
		 * bytes read is equal to the smaller of <code>len</code> and
		 * <code>available</code>. If <code>k</code> is positive, then next
		 * <code>k-1</code> bytes are copied into <code>b[off]</code> through
		 * <code>b[off+k-1]</code>. The value <code>k</code> is added into
		 * <code>pos</code> and <code>k</code> is returned.
		 * <p>
		 * This <code>read</code> method cannot block.
		 *
		 * @param b   the buffer into which the data is read.
		 * @param off the start offset in the destination array <code>b</code>
		 * @param len the maximum number of bytes read.
		 * @return the total number of bytes read into the buffer, or <code>-1</code> if
		 *         there is no more data because the end of the stream has been reached.
		 * @exception NullPointerException      If <code>b</code> is <code>null</code>.
		 * @exception IndexOutOfBoundsException If the giving range of the bytes out of
		 *                                      range
		 */
		@Override
		public synchronized int read(byte[] b, int off, int len) throws IOException {
			ensureOpen();
			if (off < 0 || off + len > b.length || off + len < off)
				throw new IndexOutOfBoundsException();
			int pos = reader.pos;
			if (end <= pos)
				return -1;
			len = Math.min(end - pos, len);
			if (len == 0)
				return 0;
			System.arraycopy(reader.buf, pos, b, off, len);
			reader.pos = pos + len;
			return len;
		}

		/**
		 * Skips <code>n</code> bytes of input from this input stream. Fewer bytes might
		 * be skipped if the end of the input stream is reached. The actual number
		 * <code>k</code> of bytes to be skipped is equal to the smaller of
		 * <code>n</code> and <code>count-pos</code>. The value <code>k</code> is added
		 * into <code>pos</code> and <code>k</code> is returned. If the giving
		 * <code>n</code> is a negative value, then the skip backward.
		 *
		 * @param n the number of bytes to be skipped
		 * @return the actual number of bytes skipped.
		 */
		@Override
		public synchronized long skip(long n) throws IOException {
			ensureOpen();
			if (0 == n)
				return 0;
			int pos = reader.pos;
			if (n > 0) {
				if (end > pos) {
					n = Math.min(end - pos, n);
				} else {
					return 0;
				}
			} else {
				if (pos > off) {
					n = Math.max(off - pos, n);
				} else {
					return 0;
				}
			}
			reader.pos = pos + (int) n;
			return n;
		}

		/**
		 * @see java.io.InputStream#available()
		 */
		@Override
		public synchronized int available() throws IOException {
			ensureOpen();
			return end - reader.pos;
		}

		/**
		 * Set the current marked position in the stream. The input stream objects are
		 * marked at position zero by default when constructed. They may be marked at
		 * another position within the buffer by this method.
		 * <p>
		 * If no mark has been set, then the value of the mark is the offset passed to
		 * the constructor (or 0 if the offset was not supplied).
		 *
		 * <p>
		 * Note: The <code>readAheadLimit</code> for this class has no meaning.
		 */
		@Override
		public synchronized void mark(int readlimit) {
			if (null == reader)
				return;
			mark = reader.pos;
		}

		/**
		 * Resets the buffer to the marked position. The marked position is 0 unless
		 * another position was marked or an offset was specified in the constructor.
		 */
		@Override
		public synchronized void reset() throws IOException {
			ensureOpen();
			reader.pos = mark;
		}

		/**
		 * Tests if this <code>InputStream</code> supports mark/reset. The
		 * <code>markSupported</code> method of this <code>InputStream</code> always
		 * returns <code>true</code>.
		 */
		@Override
		public boolean markSupported() {
			return true;
		}

		/**
		 * Closing this <tt>InputStream</tt>. The methods in this class can be called
		 * after the stream has been closed without generating an <tt>IOException</tt>.
		 */
		@Override
		public synchronized void close() {
			if (null == reader)
				return;
			reader = null;
		}

		/** Checks to make sure that the stream has not been closed */
		private void ensureOpen() throws IOException {
			if (null == reader)
				throw new IOException("Closed");
		}

	}

}
