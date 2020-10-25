/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * @see PrimitiveType#BOOLEAN
 * @author XuYanhang
 *
 */
final class PrimitiveTypeBoolean implements PrimitiveType<Boolean, boolean[]> {

	/**
	 * 
	 */
	PrimitiveTypeBoolean() {
		super();
	}

	@Override
	public String getTypeName() {
		return "boolean";
	}

	@Override
	public Class<Boolean> primitiveClass() {
		return Boolean.TYPE;
	}

	@Override
	public Class<Boolean> referenceClass() {
		return Boolean.class;
	}

	@Override
	public int bitSize() {
		return 1;
	}

	@Override
	public int byteSize() {
		return 1;
	}

	@Override
	public Boolean castValue(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Boolean) value;
		if (otherType == BYTE)
			return Boolean.valueOf(((Byte) value).byteValue() != (byte) 0);
		if (otherType == SHORT)
			return Boolean.valueOf(((Short) value).shortValue() != (short) 0);
		if (otherType == INT)
			return Boolean.valueOf(((Integer) value).intValue() != 0);
		if (otherType == LONG)
			return Boolean.valueOf(((Long) value).longValue() != 0L);
		if (otherType == FLOAT)
			return Boolean.valueOf(((Float) value).floatValue() != 0.0f);
		if (otherType == DOUBLE)
			return Boolean.valueOf(((Double) value).doubleValue() != 0.0);
		if (otherType == CHAR)
			return Boolean.valueOf(((Character) value).charValue() != '\0');
		throw new IllegalStateException();
	}

	@Override
	public Boolean castValueBits(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Boolean) value;
		if (otherType == BYTE)
			return Boolean.valueOf((((Byte) value).byteValue() & 1) != (byte) 0);
		if (otherType == SHORT)
			return Boolean.valueOf((((Short) value).shortValue() & 1) != (short) 0);
		if (otherType == INT)
			return Boolean.valueOf((((Integer) value).intValue() & 1) != 0);
		if (otherType == LONG)
			return Boolean.valueOf((((Long) value).longValue() & 1L) != 0L);
		if (otherType == FLOAT)
			return Boolean.valueOf((Float.floatToIntBits(((Float) value).floatValue()) & 1) != 0);
		if (otherType == DOUBLE)
			return Boolean.valueOf((Double.doubleToLongBits(((Double) value).doubleValue()) & 1L) != 0L);
		if (otherType == CHAR)
			return Boolean.valueOf((((Character) value).charValue() & 1) != 0);
		throw new IllegalStateException();
	}

	@Override
	public boolean[] castArray(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (boolean[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			boolean[] result = new boolean[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] != (byte) 0;
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			boolean[] result = new boolean[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] != (short) 0;
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			boolean[] result = new boolean[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] != 0;
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			boolean[] result = new boolean[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] != 0L;
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			boolean[] result = new boolean[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] != 0.f;
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			boolean[] result = new boolean[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] != 0.d;
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			boolean[] result = new boolean[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] != '\0';
			return result;
		}
		throw new IllegalStateException();
	}

	@Override
	public boolean[] castArrayBits(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (boolean[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 3))
				throw new OutOfMemoryError();
			boolean[] result = new boolean[tlen << 3];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = ((tarray[i] >> 7) & 1) != 0;
				result[cur++] = ((tarray[i] >> 6) & 1) != 0;
				result[cur++] = ((tarray[i] >> 5) & 1) != 0;
				result[cur++] = ((tarray[i] >> 4) & 1) != 0;
				result[cur++] = ((tarray[i] >> 3) & 1) != 0;
				result[cur++] = ((tarray[i] >> 2) & 1) != 0;
				result[cur++] = ((tarray[i] >> 1) & 1) != 0;
				result[cur++] = (tarray[i] & 1) != 0;
			}
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 4))
				throw new OutOfMemoryError();
			boolean[] result = new boolean[tlen << 4];
			int cur = 0;
			for (int i = 0; i < tlen; i++)
				for (int j = 15; j > -1; j--)
					result[cur++] = ((tarray[i] >> j) & 1) != 0;
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 5))
				throw new OutOfMemoryError();
			boolean[] result = new boolean[tlen << 5];
			int cur = 0;
			for (int i = 0; i < tlen; i++)
				for (int j = 31; j > -1; j--)
					result[cur++] = ((tarray[i] >> j) & 1) != 0;
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 6))
				throw new OutOfMemoryError();
			boolean[] result = new boolean[tlen << 6];
			int cur = 0;
			for (int i = 0; i < tlen; i++)
				for (int j = 63; j > -1; j--)
					result[cur++] = ((tarray[i] >> j) & 1) != 0;
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 5))
				throw new OutOfMemoryError();
			boolean[] result = new boolean[tlen << 5];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				int temp = Float.floatToIntBits(tarray[i]);
				for (int j = 31; j > -1; j--)
					result[cur++] = ((temp >> j) & 1) != 0;
			}
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 6))
				throw new OutOfMemoryError();
			boolean[] result = new boolean[tlen << 6];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				long temp = Double.doubleToLongBits(tarray[i]);
				for (int j = 63; j > -1; j--)
					result[cur++] = ((temp >> j) & 1) != 0;
			}
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 4))
				throw new OutOfMemoryError();
			boolean[] result = new boolean[tlen << 4];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				int temp = (int) tarray[i];
				for (int j = 15; j > -1; j--)
					result[cur++] = ((temp >> j) & 1) != 0;
			}
			return result;
		}
		throw new IllegalStateException();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getTypeName();
	}

	Object readResolve() {
		return BOOLEAN;
	}

	private static final long serialVersionUID = 1413662154162115847L;

}
