/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * Array package interface in primitive type where storaged in memory. Available
 * of byte[], short[], int[], long[], float[], double[], char[], boolean[].
 * These types share a same memory.
 * 
 * @author XuYanhang
 * @since 2020-10-24
 */
public interface UnionPrimitiveArray extends Cloneable, Comparable<UnionPrimitiveArray>, java.io.Serializable {

	/**
	 * Create direct memory union primitive array in specified bits.
	 * 
	 * @param bits required bit size
	 * @return the new {@link UnionPrimitiveArray}
	 * @see #bits()
	 */
	public static UnionPrimitiveArray allocDirect(long bits) {
		return new UnionPrimitiveArrayDirectMemory(bits);
	}

	/*
	 * Size measurements
	 */

	/**
	 * Returns the bits of the array
	 * 
	 * @return the bits of the array
	 */
	public long bits();

	/**
	 * Returns the byte length of the byte array
	 * 
	 * @return the byte length of the array
	 */
	public long byteLength();

	/**
	 * Returns the short length of the short array
	 * 
	 * @return the short length of the array
	 */
	public long shortLength();

	/**
	 * Returns the int length of the int array
	 * 
	 * @return the int length of the array
	 */
	public long intLength();

	/**
	 * Returns the long length of the long array
	 * 
	 * @return the long length of the array
	 */
	public long longLength();

	/**
	 * Returns the float length of the float array
	 * 
	 * @return the float length of the array
	 */
	public long floatLength();

	/**
	 * Returns the double length of the double array
	 * 
	 * @return the double length of the array
	 */
	public long doubleLength();

	/**
	 * Returns the char length of the char array
	 * 
	 * @return the char length of the array
	 */
	public long charLength();

	/**
	 * Returns the boolean length of the boolean array
	 * 
	 * @return the boolean length of the array
	 */
	public long booleanLength();

	/*
	 * Query methods
	 */

	/**
	 * Query a byte value at specified position
	 * 
	 * @param index consider the array as byte[]
	 * @return the byte value
	 */
	public byte getByte(long index);

	/**
	 * Query a short value at specified position
	 * 
	 * @param index consider the array as short[]
	 * @return the short value
	 */
	public short getShort(long index);

	/**
	 * Query a int value at specified position
	 * 
	 * @param index consider the array as int[]
	 * @return the int value
	 */
	public int getInt(long index);

	/**
	 * Query a long value at specified position
	 * 
	 * @param index consider the array as long[]
	 * @return the long value
	 */
	public long getLong(long index);

	/**
	 * Query a float value at specified position
	 * 
	 * @param index consider the array as float[]
	 * @return the float value
	 */
	public float getFloat(long index);

	/**
	 * Query a double value at specified position
	 * 
	 * @param index consider the array as double[]
	 * @return the double value
	 */
	public double getDouble(long index);

	/**
	 * Query a char value at specified position
	 * 
	 * @param index consider the array as char[]
	 * @return the char value
	 */
	public char getChar(long index);

	/**
	 * Query a boolean value at specified position
	 * 
	 * @param index consider the array as boolean[]
	 * @return the boolean value
	 */
	public boolean getBoolean(long index);

	/*
	 * Update methods
	 */

	/**
	 * Update a byte value at specified position
	 * 
	 * @param index consider the array as byte[]
	 * @param value the byte value
	 */
	public void setByte(long index, byte value);

	/**
	 * Update a short value at specified position
	 * 
	 * @param index consider the array as short[]
	 * @param value the short value
	 */
	public void setShort(long index, short value);

	/**
	 * Update a int value at specified position
	 * 
	 * @param index consider the array as int[]
	 * @param value the int value
	 */
	public void setInt(long index, int value);

	/**
	 * Update a long value at specified position
	 * 
	 * @param index consider the array as long[]
	 * @param value the long value
	 */
	public void setLong(long index, long value);

	/**
	 * Update a float value at specified position
	 * 
	 * @param index consider the array as float[]
	 * @param value the float value
	 */
	public void setFloat(long index, float value);

	/**
	 * Update a double value at specified position
	 * 
	 * @param index consider the array as double[]
	 * @param value the double value
	 */
	public void setDouble(long index, double value);

	/**
	 * Update a char value at specified position
	 * 
	 * @param index consider the array as char[]
	 * @param value the char value
	 */
	public void setChar(long index, char value);

	/**
	 * Update a boolean value at specified position
	 * 
	 * @param index consider the array as boolean[]
	 * @param value the boolean value
	 */
	public void setBoolean(long index, boolean value);

	/*
	 * Read methods on array
	 */

	/**
	 * Reload this array into a given array
	 * 
	 * @param index this array offset
	 * @param value target array
	 * @param off   target array offset
	 * @param len   reload length
	 */
	public void readBytes(long index, byte[] value, int off, int len);

	/**
	 * Reload this array into a given array
	 * 
	 * @param index this array offset
	 * @param value target array
	 * @param off   target array offset
	 * @param len   reload length
	 */
	public void readShorts(long index, short[] value, int off, int len);

	/**
	 * Reload this array into a given array
	 * 
	 * @param index this array offset
	 * @param value target array
	 * @param off   target array offset
	 * @param len   reload length
	 */
	public void readInts(long index, int[] value, int off, int len);

	/**
	 * Reload this array into a given array
	 * 
	 * @param index this array offset
	 * @param value target array
	 * @param off   target array offset
	 * @param len   reload length
	 */
	public void readLongs(long index, long[] value, int off, int len);

	/**
	 * Reload this array into a given array
	 * 
	 * @param index this array offset
	 * @param value target array
	 * @param off   target array offset
	 * @param len   reload length
	 */
	public void readFloats(long index, float[] value, int off, int len);

	/**
	 * Reload this array into a given array
	 * 
	 * @param index this array offset
	 * @param value target array
	 * @param off   target array offset
	 * @param len   reload length
	 */
	public void readDoubles(long index, double[] value, int off, int len);

	/**
	 * Reload this array into a given array
	 * 
	 * @param index this array offset
	 * @param value target array
	 * @param off   target array offset
	 * @param len   reload length
	 */
	public void readChars(long index, char[] value, int off, int len);

	/**
	 * Reload this array into a given array
	 * 
	 * @param index this array offset
	 * @param value target array
	 * @param off   target array offset
	 * @param len   reload length
	 */
	public void readBooleans(long index, boolean[] value, int off, int len);

	/*
	 * Update methods on array
	 */

	/**
	 * Update this array in a given array
	 * 
	 * @param index this array offset
	 * @param value source array
	 * @param off   source array offset
	 * @param len   update length
	 */
	public void putBytes(long index, byte[] value, int off, int len);

	/**
	 * Update this array in a given array
	 * 
	 * @param index this array offset
	 * @param value source array
	 * @param off   source array offset
	 * @param len   update length
	 */
	public void putShorts(long index, short[] value, int off, int len);

	/**
	 * Update this array in a given array
	 * 
	 * @param index this array offset
	 * @param value source array
	 * @param off   source array offset
	 * @param len   update length
	 */
	public void putInts(long index, int[] value, int off, int len);

	/**
	 * Update this array in a given array
	 * 
	 * @param index this array offset
	 * @param value source array
	 * @param off   source array offset
	 * @param len   update length
	 */
	public void putLongs(long index, long[] value, int off, int len);

	/**
	 * Update this array in a given array
	 * 
	 * @param index this array offset
	 * @param value source array
	 * @param off   source array offset
	 * @param len   update length
	 */
	public void putFloats(long index, float[] value, int off, int len);

	/**
	 * Update this array in a given array
	 * 
	 * @param index this array offset
	 * @param value source array
	 * @param off   source array offset
	 * @param len   update length
	 */
	public void putDoubles(long index, double[] value, int off, int len);

	/**
	 * Update this array in a given array
	 * 
	 * @param index this array offset
	 * @param value source array
	 * @param off   source array offset
	 * @param len   update length
	 */
	public void putChars(long index, char[] value, int off, int len);

	/**
	 * Update this array in a given array
	 * 
	 * @param index this array offset
	 * @param value source array
	 * @param off   source array offset
	 * @param len   update length
	 */
	public void putBooleans(long index, boolean[] value, int off, int len);

	/*
	 * Fill this array in specified value
	 */

	/**
	 * Fill this array in specified value
	 * 
	 * @param value value to fill
	 * @param off   this array offset
	 * @param len   length to fill
	 */
	public void fillBytes(byte value, long off, long len);

	/**
	 * Fill this array in specified value
	 * 
	 * @param value value to fill
	 * @param off   this array offset
	 * @param len   length to fill
	 */
	public void fillShorts(short value, long off, long len);

	/**
	 * Fill this array in specified value
	 * 
	 * @param value value to fill
	 * @param off   this array offset
	 * @param len   length to fill
	 */
	public void fillInts(int value, long off, long len);

	/**
	 * Fill this array in specified value
	 * 
	 * @param value value to fill
	 * @param off   this array offset
	 * @param len   length to fill
	 */
	public void fillLongs(long value, long off, long len);

	/**
	 * Fill this array in specified value
	 * 
	 * @param value value to fill
	 * @param off   this array offset
	 * @param len   length to fill
	 */
	public void fillFloats(float value, long off, long len);

	/**
	 * Fill this array in specified value
	 * 
	 * @param value value to fill
	 * @param off   this array offset
	 * @param len   length to fill
	 */
	public void fillDoubles(double value, long off, long len);

	/**
	 * Fill this array in specified value
	 * 
	 * @param value value to fill
	 * @param off   this array offset
	 * @param len   length to fill
	 */
	public void fillChars(char value, long off, long len);

	/**
	 * Fill this array in specified value
	 * 
	 * @param value value to fill
	 * @param off   this array offset
	 * @param len   length to fill
	 */
	public void fillBooleans(boolean value, long off, long len);

	/*
	 * Other methods
	 */

	/**
	 * reallocate memory in specified bits count
	 * 
	 * @param bits required bit size
	 */
	public void rebase(long bits);

	/**
	 * Reset all values in this memory. Replace all bits of 0.
	 */
	public void clear();

	/**
	 * Free the memory. It's unnecessary when it has initial the method of
	 * {@link java.lang.Object#finalize()} for JVM. Just a quick optional management
	 * to free memory in user environment.
	 */
	public void free();

	/**
	 * Copy the specified memory into another one in bits.
	 * 
	 * @param thisOff   Copy start position in this
	 * @param target    Copy target
	 * @param targetOff Copy start position in target
	 * @param bits      Copy bit size
	 */
	public void copyBits(long thisOff, UnionPrimitiveArray target, long targetOff, long bits);

	/*
	 * Public methods
	 */

	/**
	 * Compare this {@link UnionPrimitiveArray} with another one. Compare in
	 * unsigned while first bit 1 occur is larger or when share bits are equals, the
	 * larger bits size is larger.
	 * 
	 * @return the compare value with another {@link UnionPrimitiveArray}
	 */
	public int compareTo(UnionPrimitiveArray o);

	/**
	 * Clone this array in memory into another one.
	 * 
	 * @return the clone array
	 */
	public UnionPrimitiveArray clone();

	/**
	 * Returns the hashCode of this array. It's only relative with the bits content
	 * in it.
	 * 
	 * @return hashCode of this array
	 */
	public int hashCode();

	/**
	 * Returns if this array is equals with an object. They are equal only of the
	 * input object is a {@link UnionPrimitiveArray} and all bit values are equals.
	 * 
	 * @return if this is equal with an object
	 */
	public boolean equals(Object obj);

}
