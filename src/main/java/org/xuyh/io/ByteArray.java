/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.xuyh.util.Codecs;

/**
 * {@link ByteArray} is a class manages on a byte array. The array
 * {@link #value} field is allowed to change in elements and the change is
 * shared when the {@link #value} is not copy for {@link #subArray(int, int)}
 * and {@link #rootArray()}.
 * 
 * @author XuYanhang
 * @since 2020-10-24
 *
 */
public final class ByteArray implements Cloneable, java.io.Serializable, Iterable<Byte>, Comparable<ByteArray> {

	/** The value is used for byte storage */
	private final byte[] value;
	/** offset of the byte array to manage, never set here */
	private final int off;
	/** length of the byte array to manage, never set here */
	private final int len;

	/**
	 * Creates {@link ByteArray} that uses <code>value</code> as its {@link #value}.
	 * The initial value of {@link #off} is 0, The initial value of {@link #len} is
	 * <code>value.length</code>. The value array is not copied.
	 * 
	 * @param value byte array to manage
	 */
	public ByteArray(byte... value) {
		super();
		this.value = value;
		this.off = 0;
		this.len = value.length;
	}

	/**
	 * Creates {@link ByteArray} that uses <code>value</code> as its {@link #value}.
	 * The initial value of {@link #off} is <code>off</code>, The initial value of
	 * {@link #len} is <code>len</code>. The value array is not copied.
	 * 
	 * @param value byte array to manage
	 * @param off   offset of the byte array to manage
	 * @param len   length of the byte array to manage
	 */
	public ByteArray(byte[] value, int off, int len) {
		super();
		if (off < 0 || off + len > value.length || off > off + len)
			throw new IndexOutOfBoundsException();
		this.value = value;
		this.off = off;
		this.len = len;
	}

	/**
	 * Returns the managed origin byte array in this {@link ByteArray}.
	 * 
	 * @return the {@link #value}
	 */
	public byte[] source() {
		return value;
	}

	/**
	 * Returns the managed origin byte array's offset in this {@link ByteArray}.
	 * 
	 * @return the {@link #off offset}
	 */
	public int offset() {
		return off;
	}

	/**
	 * Returns the managed origin byte array's length in this {@link ByteArray}.
	 * 
	 * @return the {@link #len}
	 */
	public int length() {
		return len;
	}

	/**
	 * Returns if the byte array is empty.
	 * 
	 * @return true if the array is empty or false if not
	 */
	public boolean isEmpty() {
		return len == 0;
	}

	/**
	 * Returns the <code>byte</code> value at the specified index. An index ranges
	 * from zero to <tt>length() - 1</tt>. The first <code>byte</code> value of the
	 * array is at index zero, the next at index one, and so on, as for array
	 * indexing.
	 *
	 * @param index the index of the <code>byte</code> value to be returned
	 * @return the specified <code>byte</code> value
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or not less than <tt>length()</tt>
	 */
	public byte getByte(int index) {
		if (index < 0 || index >= len)
			throw new IndexOutOfBoundsException();
		return value[index + off];
	}

	/**
	 * Set the byte as a specified value at specified position.
	 * 
	 * @param index specified position to set
	 * @param b     specified byte value to set
	 * @return this
	 */
	public ByteArray setByte(int index, byte b) {
		if (index < 0 || index >= len)
			throw new IndexOutOfBoundsException();
		value[index + off] = b;
		return this;
	}

	/**
	 * Returns the <code>short</code> value at the specified index. An index ranges
	 * from zero to <tt>length() - 2</tt>.
	 *
	 * @param index the index of the <code>short</code> value to be returned
	 * @return the specified <code>short</code> value
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 2</tt>
	 */
	public short getShort(int index) {
		if (index < 0 || index > len - 2)
			throw new IndexOutOfBoundsException();
		return (short) (((value[index + off] & 0Xff) << 8) | (value[index + off + 1] & 0Xff));
	}

	/**
	 * Set the short as a specified value at specified position.
	 * 
	 * @param index specified position to set
	 * @param val   specified value to set
	 * @return this
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 2</tt>
	 */
	public ByteArray setShort(int index, short val) {
		if (index < 0 || index > len - 2)
			throw new IndexOutOfBoundsException();
		value[index + off] = (byte) (val >> 8);
		value[index + off + 1] = (byte) (val & 0Xff);
		return this;
	}

	/**
	 * Returns the <code>char</code> value at the specified index. An index ranges
	 * from zero to <tt>length() - 2</tt>.
	 *
	 * @param index the index of the <code>char</code> value to be returned
	 * @return the specified <code>char</code> value
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 2</tt>
	 */
	public char getChar(int index) {
		if (index < 0 || index > len - 2)
			throw new IndexOutOfBoundsException();
		return (char) (((value[index + off] & 0Xff) << 8) | (value[index + off + 1] & 0Xff));
	}

	/**
	 * Set the char as a specified value at specified position.
	 * 
	 * @param index specified position to set
	 * @param val   specified value to set
	 * @return this
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 2</tt>
	 */
	public ByteArray setChar(int index, char val) {
		if (index < 0 || index > len - 2)
			throw new IndexOutOfBoundsException();
		value[index + off] = (byte) (val >> 8);
		value[index + off + 1] = (byte) (val & 0Xff);
		return this;
	}

	/**
	 * Returns the <code>int</code> value at the specified index. An index ranges
	 * from zero to <tt>length() - 4</tt>.
	 *
	 * @param index the index of the <code>int</code> value to be returned
	 * @return the specified <code>int</code> value
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 4</tt>
	 */
	public int getInt(int index) {
		if (index < 0 || index > len - 4)
			throw new IndexOutOfBoundsException();
		return ((value[index + off] & 0Xff) << 24) //
				| ((value[index + off + 1] & 0Xff) << 16) //
				| ((value[index + off + 2] & 0Xff) << 8) //
				| (value[index + off + 3] & 0Xff);
	}

	/**
	 * Set the int as a specified value at specified position.
	 * 
	 * @param index specified position to set
	 * @param val   specified value to set
	 * @return this
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 4</tt>
	 */
	public ByteArray setInt(int index, int val) {
		if (index < 0 || index > len - 4)
			throw new IndexOutOfBoundsException();
		value[index + off] = (byte) (val >> 56);
		value[index + off + 1] = (byte) (val >> 48);
		value[index + off + 2] = (byte) (val >> 40);
		value[index + off + 3] = (byte) val;
		return this;
	}

	/**
	 * Returns the <code>long</code> value at the specified index. An index ranges
	 * from zero to <tt>length() - 8</tt>.
	 *
	 * @param index the index of the <code>long</code> value to be returned
	 * @return the specified <code>long</code> value
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 8</tt>
	 */
	public long getLong(int index) {
		if (index < 0 || index > len - 8)
			throw new IndexOutOfBoundsException();
		return ((value[index + off] & 0XffL) << 56) //
				| ((value[index + off + 1] & 0XffL) << 48) //
				| ((value[index + off + 2] & 0XffL) << 40) //
				| ((value[index + off + 3] & 0XffL) << 32) //
				| ((value[index + off + 4] & 0XffL) << 24) //
				| ((value[index + off + 5] & 0XffL) << 16) //
				| ((value[index + off + 6] & 0XffL) << 8) //
				| (value[index + off + 7] & 0XffL);
	}

	/**
	 * Set the long as a specified value at specified position.
	 * 
	 * @param index specified position to set
	 * @param val   specified value to set
	 * @return this
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 8</tt>
	 */
	public ByteArray setLong(int index, long val) {
		if (index < 0 || index > len - 8)
			throw new IndexOutOfBoundsException();
		value[index + off] = (byte) (val >> 56);
		value[index + off + 1] = (byte) (val >> 48);
		value[index + off + 2] = (byte) (val >> 40);
		value[index + off + 3] = (byte) (val >> 32);
		value[index + off + 4] = (byte) (val >> 24);
		value[index + off + 5] = (byte) (val >> 16);
		value[index + off + 6] = (byte) (val >> 8);
		value[index + off + 7] = (byte) val;
		return this;
	}

	/**
	 * Returns the <code>float</code> value at the specified index. An index ranges
	 * from zero to <tt>length() - 4</tt>.
	 *
	 * @param index the index of the <code>float</code> value to be returned
	 * @return the specified <code>float</code> value
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 4</tt>
	 */
	public float getFloat(int index) {
		return Float.intBitsToFloat(getInt(index));
	}

	/**
	 * Set the float as a specified value at specified position.
	 * 
	 * @param index specified position to set
	 * @param val   specified value to set
	 * @return this
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 4</tt>
	 */
	public ByteArray setFloat(int index, float val) {
		return setInt(index, Float.floatToIntBits(val));
	}

	/**
	 * Returns the <code>double</code> value at the specified index. An index ranges
	 * from zero to <tt>length() - 8</tt>.
	 *
	 * @param index the index of the <code>double</code> value to be returned
	 * @return the specified <code>double</code> value
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 8</tt>
	 */
	public double getDouble(int index) {
		return Double.longBitsToDouble(getLong(index));
	}

	/**
	 * Set the double as a specified value at specified position.
	 * 
	 * @param index specified position to set
	 * @param val   specified value to set
	 * @return this
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> argument is negative
	 *                                   or larger than <tt>length() - 8</tt>
	 */
	public ByteArray setDouble(int index, double val) {
		return setLong(index, Double.doubleToLongBits(val));
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Byte> iterator() {

		return new Iterator<Byte>() {

			int cur = off;
			int end = off + len;

			@Override
			public boolean hasNext() {
				return cur < end;
			}

			@Override
			public Byte next() {
				if (cur >= end)
					throw new java.util.NoSuchElementException();
				return Byte.valueOf(value[cur++]);
			}

		};
	}

	/**
	 * Reverse the order of this byte array where the origin end becomes start and
	 * the origin start becomes end.
	 * 
	 * @return this
	 */
	public ByteArray reverse() {
		for (int i = off, j = off + len - 1; i < j; i++, j--) {
			byte tmp = value[i];
			value[i] = value[j];
			value[j] = tmp;
		}
		return this;
	}

	/**
	 * Fill this byte array in a specified byte.
	 * 
	 * @param b specified byte
	 * @return this
	 */
	public ByteArray fills(byte b) {
		int end = off + len;
		for (int i = off; i < end; i++)
			value[i] = b;
		return this;
	}

	/**
	 * Converts this byte array to a new byte array.
	 *
	 * @return a newly allocated byte array whose length is the length of this array
	 *         and whose contents are initialized to contain the byte sequence
	 *         represented by this array.
	 */
	public byte[] toByteArray() {
		byte[] copyValue = value;
		if (value.length > 0) {
			copyValue = new byte[len];
			System.arraycopy(value, off, copyValue, 0, len);
		}
		return copyValue;
	}

	/**
	 * Returns a <code>ByteArray</code> that is a root of this array. The root array
	 * has the public access like {@link #getByte(int)} and
	 * {@link #setByte(int, byte)} to visit on the whole {@link #value} from
	 * <code>0</code> index to <code>value.length-1</code>.
	 * <p>
	 * Updates on bytes of root-array has effect on this array while so does on root
	 * array if updates the this array.
	 *
	 * @return the specified array
	 *
	 */
	public ByteArray rootArray() {
		return ((0 == off) && (value.length == len)) ? this : new ByteArray(value);
	}

	/**
	 * Returns a <code>ByteArray</code> that is a subArray of this array. The
	 * subArray starts with the <code>byte</code> value at the specified index and
	 * ends with the <code>byte</code> value at index <tt>end - 1</tt>. The length
	 * (in <code>byte</code>s) of the returned array is <tt>end - start</tt>, so if
	 * <tt>start == end</tt> then an empty array is returned.
	 * <p>
	 * Updates on bytes of sub-array has effect on parent-array while so does on
	 * sub-array if updates the parent-array.
	 *
	 * @param start the start index, inclusive
	 * @param end   the end index, exclusive
	 * @return the specified array
	 * @throws IndexOutOfBoundsException if <tt>start</tt> or <tt>end</tt> are
	 *                                   negative, if <tt>end</tt> is greater than
	 *                                   <tt>length()</tt>, or if <tt>start</tt> is
	 *                                   greater than <tt>end</tt>
	 */
	public ByteArray subArray(int start, int end) {
		int subLen = end - start;
		if (start < 0 || end > len || subLen < 0)
			throw new IndexOutOfBoundsException();
		return ((start == off) && (subLen == len)) ? this : new ByteArray(value, start + off, subLen);
	}

	/**
	 * Create hex dump string from this byte array.
	 * 
	 * @return hex string of the byte array
	 */
	public String hex() {
		return Codecs.hex(value, off, len);
	}

	/**
	 * Create base64 dump string from this byte array.
	 * 
	 * @return hex string of the byte array
	 */
	public String base64() {
		return Codecs.base64(value, off, len);
	}

	/**
	 * Returns the input stream on this byte array. All operation has the effect on
	 * this array.
	 * 
	 * @return input stream on this byte array from <code>off</code> to
	 *         <code>off + end - 1</code>
	 */
	public InputStream openInputStream() {
		return new ByteArrayInputStream(value, off, len);
	}

	/**
	 * Returns the output stream on this byte array. All operation has the effect on
	 * this array.
	 * 
	 * @return output stream on this byte array from <code>off</code> to
	 *         <code>off + end - 1</code>
	 */
	public OutputStream openOutputStream() {
		return new SpecifyByteArrayOutputStream(value, off, len);
	}

	/**
	 * Writes all contents of this byte array to the specified output stream
	 * argument, as if by calling the output stream's write method using
	 * <code>out.write(value, off, len)</code>.
	 *
	 * @param out the output stream to which to write the value
	 * @return this
	 * @throws IOException if an I/O error occurs
	 */
	public ByteArray writeTo(OutputStream out) throws IOException {
		out.write(value, off, len);
		return this;
	}

	/**
	 * Read some contents into this byte array from the specified input stream
	 * argument, as if by calling the input stream's read method using
	 * <code>in.read(value, off, len)</code>, but fully read. If the bytes from the
	 * input stream are not enough, an <code>EOFException</code> occurs.
	 *
	 * @param in the input stream from which to read the value from
	 * @return this
	 * @throws IOException if an I/O error occurs
	 */
	public ByteArray readFrom(InputStream in) throws IOException {
		int end = off + len;
		int begin = off;
		int s = -1;
		while (begin < end && (s = in.read(value, begin, len)) >= 0)
			begin += s;
		if (begin < end)
			throw new java.io.EOFException();
		return this;
	}

	/**
	 * Clone a copy of the {@link ByteArray}. Clone has the same values in specified
	 * sequence while changes might happen on {@link #value} and {@link #offset()}
	 * when only the specified range in the {@link #value} is copied.
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ByteArray clone() {
		return new ByteArray(toByteArray());
	}

	/**
	 * Compares two byte arrays lexicographically. <br>
	 * The comparison is based on the Unicode value of each byte in the byte arrays.
	 * The bytes represented by this {@code ByteArray} object is compared
	 * lexicographically to the bytes represented by the argument array. The result
	 * is a negative integer if this {@code ByteArray} object lexicographically
	 * precedes the argument byte array. The result is a positive integer if this
	 * {@code ByteArray} object lexicographically follows the argument byte array.
	 * The result is zero if the byte arrays are equal; {@code compareTo} returns
	 * {@code 0} exactly when the {@link #equals(Object)} method would return
	 * {@code true}.
	 * <p>
	 * This is the definition of unsigned ASC ordering. If two byte arrays are
	 * different, then either they have different bytes at some index that is a
	 * valid index for both arrays, or their lengths are different, or both. If they
	 * have different bytes at one or more index positions, let <i>k</i> be the
	 * smallest such index; then the array whose byte at position <i>k</i> has the
	 * smaller value, as determined by using the &lt; operator, lexicographically
	 * precedes the other array. In this case, {@code compareTo} returns the
	 * difference of the two array values at position {@code k} in the two array --
	 * that is, the value: <blockquote>
	 * 
	 * <pre>
	 * this.getByte(k) - other.getByte(k)
	 * </pre>
	 * 
	 * </blockquote> If there is no index position at which they differ, then the
	 * shorter array precedes the longer array. In this case, {@code compareTo}
	 * returns the difference of the lengths of the arrays -- that is, the value:
	 * <blockquote>
	 * 
	 * <pre>
	 * this.length() - other.length()
	 * </pre>
	 * 
	 * </blockquote>
	 *
	 * @param other the {@code ByteArray} to be compared.
	 * @return the value {@code 0} if the argument array is equal to this byte
	 *         array; a value less than {@code 0} if this byte array is less than
	 *         the byte array argument; and a unsigned value greater than {@code 0}
	 *         if this byte array is unsigned value greater than the byte array
	 *         argument.
	 */
	@Override
	public int compareTo(ByteArray other) {
		int lim = Math.min(this.len, other.len);
		for (int k = 0; k < lim; k++) {
			byte c1 = this.value[k + this.off];
			byte c2 = other.value[k + other.off];
			if (c1 != c2)
				return (c1 & 0Xff) - (c2 & 0Xff);
		}
		return this.len - other.len;
	}

	/**
	 * Compare this with an {@link Object} and they are equal only when there
	 * classes are same and they are match on the managed region in array.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (null == obj || obj.getClass() != getClass())
			return false;
		ByteArray other = (ByteArray) obj;
		if (other.len != this.len)
			return false;
		for (int i = 0; i < len; i++)
			if (this.value[i + this.off] != other.value[i + other.off])
				return false;
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int h = 0;
		for (int i = 0; i < len; i++)
			h = 31 * h + value[i];
		return h;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Codecs.base64(value, off, len);
	}

	/**
	 * Returns the serialized object of the array to resolve the read and write
	 * method.
	 * 
	 * @return the serialized object
	 */
	private Object writeReplace() {
		return new SerialData(this);
	}

	/**
	 * Serialize this byte array. Changes might happen on {@link #value} and
	 * {@link #offset()} when only the specified range in the {@link #value} is
	 * serialized.
	 */
	private static final long serialVersionUID = 5779337175929692546L;

	/**
	 * To serialize or deserialize a {@link ByteArray}
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class SerialData implements java.io.Externalizable {

		/**
		 * Serialize the byte array.
		 */
		private static final long serialVersionUID = ByteArray.serialVersionUID + 16L;

		/**
		 * Byte array to serialize
		 */
		transient ByteArray array;

		/**
		 * Initialize the data
		 * 
		 * @param array the {@link ByteArray} to write
		 */
		SerialData(ByteArray array) {
			super();
			this.array = array;
		}

		/**
		 * Save the state of the {@link ByteArray} instance to a stream (that is,
		 * serialize it).
		 *
		 * @serialData the length value and only the ranged bytes while the extra byte
		 *             parts out the range ignored.
		 */
		@Override
		public void writeExternal(java.io.ObjectOutput out) throws IOException {
			out.writeInt(array.len);
			out.write(array.value, array.off, array.len);
		}

		/**
		 * The readObject is called to restore the state of the {@link ByteArray} from a
		 * stream.
		 */
		@Override
		public void readExternal(java.io.ObjectInput in) throws IOException {
			int len = in.readInt();
			byte[] value = new byte[len];
			in.readFully(value, 0, len);
			array = new ByteArray(value);
		}

		/**
		 * Resolve the read data from {@link #readExternal(java.io.ObjectInput)}.
		 */
		Object readResolve() {
			return array;
		}

	}

	/**
	 * Copies an array from the specified source array, beginning at the specified
	 * position, to the specified position of the destination array. A subsequence
	 * of array components are copied from the source array referenced by
	 * <code>src</code> to the destination array referenced by <code>dest</code>.
	 * The number of components copied is equal to the <code>length</code> argument.
	 * The components at positions <code>srcPos</code> through
	 * <code>srcPos+length-1</code> in the source array are copied into positions
	 * <code>destPos</code> through <code>destPos+length-1</code>, respectively, of
	 * the destination array.
	 * 
	 * @param src     the source array
	 * @param srcPos  starting position in the source array
	 * @param dest    the destination array
	 * @param destPos starting position in the destination data
	 * @param length  the number of array elements to be copied
	 * @throws IndexOutOfBoundsException if copying would cause access of data
	 *                                   outside array bounds
	 * @throws NullPointerException      if either <code>src</code> or
	 *                                   <code>dest</code> is <code>null</code>.
	 */
	public static void copy(ByteArray src, int srcPos, ByteArray dest, int destPos, int length) {
		if (srcPos < 0 || srcPos + length > src.len || srcPos + length < srcPos)
			throw new IndexOutOfBoundsException();
		if (destPos < 0 || destPos + length > dest.len || destPos + length < destPos)
			throw new IndexOutOfBoundsException();
		System.arraycopy(src.value, srcPos + src.off, dest.value, destPos + dest.off, length);
	}

	/**
	 * Concatenates these arrays as one from first to the end one by one.
	 * 
	 * @param arrays arrays to concatenate
	 * @return the concatenated array
	 */
	public static ByteArray concat(ByteArray... arrays) {
		long len = 0;
		for (int i = 0; i < arrays.length; i++)
			len += arrays[i].len;
		if (len != (int) len)
			throw new OutOfMemoryError();
		byte[] target = new byte[(int) len];
		int cur = 0;
		for (int i = 0; i < arrays.length; i++) {
			System.arraycopy(arrays[i].value, arrays[i].off, target, cur, arrays[i].len);
			cur += arrays[i].len;
		}
		return new ByteArray(target);
	}

}
