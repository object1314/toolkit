/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * Type manager on primitive types like {@link Byte#TYPE byte},
 * {@link Short#TYPE short}, {@link Integer#TYPE int}, {@link Long#TYPE long},
 * {@link Float#TYPE float}, {@link Double#TYPE double}, {@link Character#TYPE
 * char}, {@link Boolean#TYPE boolean}.
 * 
 * @author XuYanhang
 * @since 2020-10-24
 *
 * @param <T> generic in reference class on {@link Byte}, {@link Short},
 *        {@link Integer}, {@link Long}, {@link Float}, {@link Double},
 *        {@link Character}, {@link Boolean}
 * @param <A> generic in primitive array as byte[], short[], int[], long[],
 *        float[], double[], char[], boolean[]
 */
public interface PrimitiveType<T, A>
		extends java.lang.reflect.Type, java.io.Serializable, Comparable<PrimitiveType<?, ?>> {

	/** @see Byte */
	public static final PrimitiveType<Byte, byte[]> BYTE = new ByteType();

	/** @see Short */
	public static final PrimitiveType<Short, short[]> SHORT = new ShortType();

	/** @see Integer */
	public static final PrimitiveType<Integer, int[]> INT = new IntType();

	/** @see Long */
	public static final PrimitiveType<Long, long[]> LONG = new LongType();

	/** @see Float */
	public static final PrimitiveType<Float, float[]> FLOAT = new FloatType();

	/** @see Double */
	public static final PrimitiveType<Double, double[]> DOUBLE = new DoubleType();

	/** @see Character */
	public static final PrimitiveType<Character, char[]> CHAR = new CharType();

	/** @see Boolean */
	public static final PrimitiveType<Boolean, boolean[]> BOOLEAN = new BooleanType();

	/**
	 * Returns all types.
	 * 
	 * @return all types
	 */
	public static PrimitiveType<?, ?>[] types() {
		return new PrimitiveType<?, ?>[] { BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, CHAR, BOOLEAN };
	}

	/**
	 * Find a type in a reference value. A value, if in a reference map on a
	 * primitive type returns the type, returns the type, else, returns
	 * <code>null</code>.
	 * 
	 * @param value value in a reference map to query a primitive type
	 * @return a <tt>PrimitiveType</tt> of the value or <code>null</code> when not a
	 *         reference value on primitive type.
	 */
	public static PrimitiveType<?, ?> ofValue(Object value) {
		if (null == value)
			return null;
		return ofReferenceClass(value.getClass());
	}

	/**
	 * Find a type in a primitive array. An array in primitive class composed
	 * results a type in this primitive class.
	 * 
	 * @param array array component in primitive items
	 * @return a <tt>PrimitiveType</tt> of the array or <code>null</code> when not a
	 *         primitive array.
	 */
	public static PrimitiveType<?, ?> ofArray(Object array) {
		if (null == array)
			return null;
		Class<?> clazz = array.getClass();
		if (!clazz.isArray())
			return null;
		return ofPrimitiveClass(clazz.getComponentType());
	}

	/**
	 * Find a type in primitive class who is like int.class or <code>null</code>
	 * when it's not a primitive class.
	 * 
	 * @param primitiveClass the primitive class to find for
	 * @return a <tt>PrimitiveType</tt> of the primitive class or <code>null</code>
	 *         when not a primitive class
	 */
	public static PrimitiveType<?, ?> ofPrimitiveClass(Class<?> primitiveClass) {
		PrimitiveType<?, ?>[] types = types();
		for (int i = 0, l = types.length; i < l; i++)
			if (types[i].primitiveClass() == primitiveClass)
				return types[i];
		return null;
	}

	/**
	 * Find a type in reference class who is like Integer.class or <code>null</code>
	 * when it's not a reference class on primitive type.
	 * 
	 * @param referenceClass the reference class to find for
	 * @return a <tt>PrimitiveType</tt> of the reference class or <code>null</code>
	 *         when not a reference class on primitive type
	 */
	public static PrimitiveType<?, ?> ofReferenceClass(Class<?> referenceClass) {
		PrimitiveType<?, ?>[] types = types();
		for (int i = 0, l = types.length; i < l; i++)
			if (types[i].referenceClass() == referenceClass)
				return types[i];
		return null;
	}

	/**
	 * Returns the name of this primitive type
	 * 
	 * @return the name of this primitive type
	 */
	public String getTypeName();

	/**
	 * Returns the primitive class of the {@link PrimitiveType}
	 * 
	 * @return the primitive class
	 */
	public Class<T> primitiveClass();

	/**
	 * Returns the reference class of the {@link PrimitiveType}
	 * 
	 * @return the the reference class
	 */
	public Class<T> referenceClass();

	/**
	 * Returns the least required bits for this type value
	 * 
	 * @return the least required bits for this type value
	 */
	public int bitSize();

	/**
	 * Returns the least required bytes for this type value
	 * 
	 * @return the least required bytes for this type value
	 */
	public int byteSize();

	/**
	 * Cast a reference value to the reference value in this type who should wrap a
	 * primitive type.
	 * 
	 * @param value value in a reference map on a primitive type
	 * @return target value in the reference map on this primitive type
	 * @throws NullPointerException     if the value is <code>null</code>
	 * @throws IllegalArgumentException if the value is not in a reference type
	 */
	public T castValue(Object value);

	/**
	 * Cast a reference value on bits to the reference value in this type who should
	 * wrap a primitive type.
	 * 
	 * @param value value in a reference map on a primitive type
	 * @return target value in the reference map on this primitive type
	 * @throws NullPointerException     if the value is <code>null</code>
	 * @throws IllegalArgumentException if the value is not in a reference type
	 */
	public T castValueBits(Object value);

	/**
	 * Cast an array of primitive type to an array of primitive type in this type on
	 * all values. Cast is allowed between any two types in byte[],short[], int[],
	 * long[], float[], double[], char[], boolean[]. The boolean is resolve as 1 and
	 * any number including char is resolve as <code>true</code> only when not zero
	 * or '\0'. Returns the source array when the source array is same type with
	 * this and returns <code>null</code> when the source array is <code>null</code>
	 * too.
	 * 
	 * @param array source array in a primitive type
	 * @return array in this primitive type
	 * @throws NullPointerException     if the array is <code>null</code>
	 * @throws IllegalArgumentException if the array is not in the primitive type
	 */
	public A castArray(Object array);

	/**
	 * Cast an array of primitive type to an array of primitive type in this type on
	 * all bits. Cast is allowed between any two types in byte[],short[], int[],
	 * long[], float[], double[], char[], boolean[]. The boolean is resolve as 1 and
	 * any number including char is resolve as <code>true</code> only when not zero
	 * or '\0'. Returns the source array when the source array is same type with
	 * this and returns <code>null</code> when the source array is <code>null</code>
	 * too.
	 * 
	 * @param array source array in a primitive type
	 * @return array in this primitive type
	 * @throws NullPointerException     if the array is <code>null</code>
	 * @throws IllegalArgumentException if the array is not in the primitive type
	 */
	public A castArrayBits(Object array);

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	default public int compareTo(PrimitiveType<?, ?> other) {
		if (null == other)
			throw new NullPointerException();
		if (this == other)
			return 0;
		PrimitiveType<?, ?>[] types = types();
		for (int i = 0, l = types.length; i < l; i++) {
			if (types[i] == this)
				return -1;
			if (types[i] == other)
				return 1;
		}
		throw new IllegalStateException();
	}

}
