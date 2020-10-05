package org.xuyh.container;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Array package interface in primitive type. The only method required
 * implemented is {@link #toArray()} who just get the value in the array. Notice
 * that the generic type of primitive array type and the reference type on the
 * primitive type should be declared in right pairs like int[] and Integer in
 * the implement classes.
 * 
 * @author XuYanhang
 * @since 2020-10-06
 * @param <AP> the generic type in primitive array as byte[], short[], int[],
 *        long[], float[], double[], char[], boolean[]
 * @param <G> the generic type component on {@link Byte}, {@link Short},
 *        {@link Integer}, {@link Long}, {@link Float}, {@link Double},
 *        {@link Character}, {@link Boolean} who just wrap the element type in
 *        array by A
 */
public interface PrimArray<AP, R> extends Iterable<R> {

	/**
	 * Returns the primitive array. The force cast is needed when use this PrimArray
	 * directly.
	 * 
	 * @return The array in type of byte[], short[], int[], long[], float[],
	 *         double[], char[], boolean[].
	 */
	public AP toArray();

	/**
	 * Returns the array in generic type who should be in {@link Byte},
	 * {@link Short}, {@link Integer}, {@link Long}, {@link Float}, {@link Double},
	 * {@link Character}, {@link Boolean}
	 * 
	 * @return The array in generic type.
	 * @throws IllegalStateException if the array is a illegal array when is not an
	 *                               array in primitive type
	 */
	@SuppressWarnings("unchecked")
	default public R[] toRefArray() {
		Object array = toArray();
		if (null == array)
			return null;
		PrimType type = PrimType.fromPriType(array.getClass().getComponentType());
		if (null == type)
			throw new IllegalStateException("Illegal array");
		int len = Array.getLength(array);
		Object newArray = Array.newInstance(type.refType, len);
		for (int i = 0; i < len; i++) {
			Array.set(newArray, i, Array.get(array, i));
		}
		return (R[]) newArray;
	}

	/**
	 * Returns the count of values in this array. An array in <code>null</code> has
	 * the size of 0.
	 * 
	 * @see #toArray()
	 * @return the count of values in this array
	 */
	default public int size() {
		Object array = toArray();
		if (null == array)
			return 0;
		return Array.getLength(array);
	}

	/**
	 * Returns the array in byte elements.
	 * 
	 * @return the array in byte elements
	 */
	default public byte[] toByteArray() {
		return (byte[]) PrimType.BYTE.castPrimArray(toArray());
	}

	/**
	 * Returns the array in short elements.
	 * 
	 * @return the array in short elements
	 */
	default public short[] toShortArray() {
		return (short[]) PrimType.SHORT.castPrimArray(toArray());
	}

	/**
	 * Returns the array in int elements.
	 * 
	 * @return the array in int elements
	 */
	default public int[] toIntArray() {
		return (int[]) PrimType.INT.castPrimArray(toArray());
	}

	/**
	 * Returns the array in long elements.
	 * 
	 * @return the array in long elements
	 */
	default public long[] toLongArray() {
		return (long[]) PrimType.LONG.castPrimArray(toArray());
	}

	/**
	 * Returns the array in float elements.
	 * 
	 * @return the array in float elements
	 */
	default public float[] toFloatArray() {
		return (float[]) PrimType.FLOAT.castPrimArray(toArray());
	}

	/**
	 * Returns the array in double elements.
	 * 
	 * @return the array in double elements
	 */
	default public double[] toDoubleArray() {
		return (double[]) PrimType.DOUBLE.castPrimArray(toArray());
	}

	/**
	 * Returns the array in char elements.
	 * 
	 * @return the array in char elements
	 */
	default public char[] toCharArray() {
		return (char[]) PrimType.CHAR.castPrimArray(toArray());
	}

	/**
	 * Returns the array in boolean elements.
	 * 
	 * @return the array in boolean elements
	 */
	default public boolean[] toBoolArray() {
		return (boolean[]) PrimType.BOOL.castPrimArray(toArray());
	}

	/**
	 * Returns an iterator over elements of the reference type.
	 *
	 * @return an Iterator.
	 */
	default public Iterator<R> iterator() {

		return new Iterator<R>() {

			final Object array = toArray();
			final int len = null == array ? 0 : Array.getLength(array);
			int cursor = 0;

			@Override
			public boolean hasNext() {
				return cursor < len;
			}

			@SuppressWarnings("unchecked")
			@Override
			public R next() {
				if (cursor >= len)
					throw new NoSuchElementException("next");
				return (R) Array.get(array, cursor++);
			}

		};
	}

	/**
	 * Types wrapped on the eight primitive types as well as their wrapped reference
	 * type.
	 * 
	 * @author XuYanhang
	 * @since 2020-10-06
	 *
	 */
	public static enum PrimType {

		BYTE(Byte.class) {
			@Override
			public Byte castPrimValue(Object val) {
				if (val instanceof Byte)
					return (Byte) val;
				if (null == fromRefType(val.getClass()))
					throw new IllegalArgumentException("Not a primitive value");
				if (val instanceof Number)
					return ((Number) val).byteValue();
				if (val instanceof Character)
					return (byte) (int) ((Character) val).charValue();
				if (val instanceof Boolean)
					return (byte) (((Boolean) val).booleanValue() ? 1 : 0);
				// Never goto here
				throw new IllegalStateException("Illegal state");
			}
		},
		SHORT(Short.class) {
			@Override
			public Short castPrimValue(Object val) {
				if (val instanceof Short)
					return (Short) val;
				if (null == fromRefType(val.getClass()))
					throw new IllegalArgumentException("Not a primitive value");
				if (val instanceof Number)
					return ((Number) val).shortValue();
				if (val instanceof Character)
					return (short) (int) ((Character) val).charValue();
				if (val instanceof Boolean)
					return (short) (((Boolean) val).booleanValue() ? 1 : 0);
				// Never goto here
				throw new IllegalStateException("Illegal state");
			}
		},
		INT(Integer.class) {
			@Override
			public Integer castPrimValue(Object val) {
				if (val instanceof Integer)
					return (Integer) val;
				if (null == fromRefType(val.getClass()))
					throw new IllegalArgumentException("Not a primitive value");
				if (val instanceof Number)
					return ((Number) val).intValue();
				if (val instanceof Character)
					return (int) ((Character) val).charValue();
				if (val instanceof Boolean)
					return (int) (((Boolean) val).booleanValue() ? 1 : 0);
				// Never goto here
				throw new IllegalStateException("Illegal state");
			}
		},
		LONG(Long.class) {
			@Override
			public Long castPrimValue(Object val) {
				if (val instanceof Long)
					return (Long) val;
				if (null == fromRefType(val.getClass()))
					throw new IllegalArgumentException("Not a primitive value");
				if (val instanceof Number)
					return ((Number) val).longValue();
				if (val instanceof Character)
					return (long) (int) ((Character) val).charValue();
				if (val instanceof Boolean)
					return (long) (((Boolean) val).booleanValue() ? 1 : 0);
				// Never goto here
				throw new IllegalStateException("Illegal state");
			}
		},
		FLOAT(Float.class) {
			@Override
			public Float castPrimValue(Object val) {
				if (val instanceof Float)
					return (Float) val;
				if (null == fromRefType(val.getClass()))
					throw new IllegalArgumentException("Not a primitive value");
				if (val instanceof Number)
					return ((Number) val).floatValue();
				if (val instanceof Character)
					return (float) (int) ((Character) val).charValue();
				if (val instanceof Boolean)
					return (float) (((Boolean) val).booleanValue() ? 1 : 0);
				// Never goto here
				throw new IllegalStateException("Illegal state");
			}
		},
		DOUBLE(Double.class) {
			@Override
			public Double castPrimValue(Object val) {
				if (val instanceof Double)
					return (Double) val;
				if (null == fromRefType(val.getClass()))
					throw new IllegalArgumentException("Not a primitive value");
				if (val instanceof Number)
					return ((Number) val).doubleValue();
				if (val instanceof Character)
					return (double) (int) ((Character) val).charValue();
				if (val instanceof Boolean)
					return (double) (((Boolean) val).booleanValue() ? 1 : 0);
				// Never goto here
				throw new IllegalStateException("Illegal state");
			}
		},
		CHAR(Character.class) {
			@Override
			public Character castPrimValue(Object val) {
				if (val instanceof Character)
					return (Character) val;
				if (null == fromRefType(val.getClass()))
					throw new IllegalArgumentException("Not a primitive value");
				if (val instanceof Number)
					return (char) ((Number) val).intValue();
				if (val instanceof Boolean)
					return (char) (((Boolean) val).booleanValue() ? 1 : 0);
				// Never goto here
				throw new IllegalStateException("Illegal state");
			}
		},
		BOOL(Boolean.class) {
			@Override
			public Boolean castPrimValue(Object val) {
				if (val instanceof Boolean)
					return (Boolean) val;
				if (null == fromRefType(val.getClass()))
					throw new IllegalArgumentException("Not a primitive value");
				if (val instanceof Number)
					return ((Number) val).doubleValue() != 0;
				if (val instanceof Character)
					return ((Character) val).charValue() != '\0';
				// Never goto here
				throw new IllegalStateException("Illegal state");
			}
		};

		/**
		 * The primitive type as byte, short, int, long, float, double, char, boolean
		 * who should be paired with reference type.
		 */
		public final Class<?> priType;
		/**
		 * The reference type as {@link Byte}, {@link Short}, {@link Integer},
		 * {@link Long}, {@link Float}, {@link Double}, {@link Character},
		 * {@link Boolean} who should be paired with primitive type.
		 */
		public final Class<?> refType;

		/*
		 * Initialize this type in the reference type.
		 */
		private PrimType(Class<?> refType) {
			try {
				this.priType = (Class<?>) refType.getField("TYPE").get(null);
				this.refType = refType;
			} catch (Exception e) {
				throw new InternalError();
			}
		}

		/**
		 * Cast a reference value to the reference value in this type who should wrap a
		 * primitive type.
		 * 
		 * @param val value in a reference type map on a primitive type
		 * @return target value in the reference type map on the primitive type
		 * @throws NullPointerException     if the value is <code>null</code>.
		 * @throws IllegalArgumentException if the value is not in the type.
		 */
		public abstract Object castPrimValue(Object val);

		/**
		 * Cast an array of primitive type to an array of primitive type in this type.
		 * Cast is allowed between any two types in byte[],short[], int[], long[],
		 * float[], double[], char[], boolean[]. The boolean is resolve as 1 and any
		 * number including char is resolve as <code>true</code> only when not zero or
		 * '\0'. Returns the source array when the source array is same type with this
		 * and returns <code>null</code> when the source array is <code>null</code> too.
		 * 
		 * @param array source array in a primitive.
		 * @return array in this primitive type.
		 * @throws IllegalArgumentException if the array is not in the primitive type.
		 */
		public Object castPrimArray(Object array) {
			if (null == array)
				return null;
			PrimType type = PrimType.fromPriType(array.getClass().getComponentType());
			if (null == type)
				throw new IllegalArgumentException("Illegal array");
			if (type == this)
				return array;
			int len = Array.getLength(array);
			Object newArray = Array.newInstance(priType, len);
			for (int i = 0; i < len; i++)
				Array.set(array, i, this.castPrimValue(Array.get(array, i)));
			return newArray;
		}

		/**
		 * Find a type in primitive type who is like int.class or <code>null</code> when
		 * it's not a primitive type.
		 * 
		 * @param priType the primitive type
		 * @return a result {@link PrimType} or <code>null</code>
		 */
		public static PrimType fromPriType(Class<?> priType) {
			PrimType[] types = values();
			for (int i = 0; i < types.length; i++)
				if (types[i].priType == priType)
					return types[i];
			return null;
		}

		/**
		 * Find a type in reference type who is like Integer.class or <code>null</code>
		 * when it's not a reference type on primitive type.
		 * 
		 * @param refType the reference type
		 * @return a result {@link PrimType} or <code>null</code>
		 */
		public static PrimType fromRefType(Class<?> refType) {
			PrimType[] types = values();
			for (int i = 0; i < types.length; i++)
				if (types[i].refType == refType)
					return types[i];
			return null;
		}

	}

}
