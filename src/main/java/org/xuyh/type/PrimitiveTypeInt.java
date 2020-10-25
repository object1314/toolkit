/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * @see PrimitiveType#INT
 * @author XuYanhang
 *
 */
final class PrimitiveTypeInt implements PrimitiveType<Integer, int[]> {

	/**
	 * 
	 */
	PrimitiveTypeInt() {
		super();
	}

	@Override
	public String getTypeName() {
		return "int";
	}

	@Override
	public Class<Integer> primitiveClass() {
		return Integer.TYPE;
	}

	@Override
	public Class<Integer> referenceClass() {
		return Integer.class;
	}

	@Override
	public int bitSize() {
		return Integer.SIZE;
	}

	@Override
	public int byteSize() {
		return Integer.SIZE >> 3;
	}

	@Override
	public Integer castValue(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Integer) value;
		if (otherType == BYTE || otherType == SHORT || otherType == LONG || otherType == FLOAT || otherType == DOUBLE)
			return Integer.valueOf(((Number) value).intValue());
		if (otherType == CHAR)
			return Integer.valueOf((int) ((Character) value).charValue());
		if (otherType == BOOLEAN)
			return Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
		throw new IllegalStateException();
	}

	@Override
	public Integer castValueBits(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Integer) value;
		if (otherType == BYTE)
			return Integer.valueOf(((Byte) value).byteValue() & 0Xff);
		if (otherType == SHORT)
			return Integer.valueOf(((Short) value).shortValue() & 0Xffff);
		if (otherType == LONG)
			return Integer.valueOf(((Long) value).intValue());
		if (otherType == FLOAT)
			return Integer.valueOf(Float.floatToIntBits(((Float) value).floatValue()));
		if (otherType == DOUBLE)
			return Integer.valueOf((int) Double.doubleToLongBits(((Double) value).doubleValue()));
		if (otherType == CHAR)
			return Integer.valueOf(((Character) value).charValue() & 0Xffff);
		if (otherType == BOOLEAN)
			return Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
		throw new IllegalStateException();
	}

	@Override
	public int[] castArray(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (int[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			int[] result = new int[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (int) tarray[i];
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			int[] result = new int[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (int) tarray[i];
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			int[] result = new int[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (int) tarray[i];
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			int[] result = new int[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (int) tarray[i];
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			int[] result = new int[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (int) tarray[i];
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			int[] result = new int[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (int) tarray[i];
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			int[] result = new int[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] ? 1 : 0;
			return result;
		}
		throw new IllegalStateException();
	}

	@Override
	public int[] castArrayBits(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (int[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 2;
			int[] result = new int[wLen + ((tlen & 3) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (tarray[cur++] & 0Xff) << 24;
				result[i] |= (tarray[cur++] & 0Xff) << 16;
				result[i] |= (tarray[cur++] & 0Xff) << 8;
				result[i] |= (tarray[cur++] & 0Xff);
			}
			int extraBits = 24;
			while (cur < tlen) {
				result[wLen] |= (tarray[cur++] & 0XffL) << extraBits;
				extraBits -= 8;
			}
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			int[] result = new int[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (tarray[cur++] & 0Xffff) << 16;
				result[i] |= (tarray[cur++] & 0Xffff);
			}
			if (cur < tlen) {
				result[wLen] = (tarray[cur++] & 0Xffff) << 16;
			}
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			int[] result = new int[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = (int) (tarray[i] >> 32);
				result[cur++] = (int) tarray[i];
			}
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			int[] result = new int[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = Float.floatToIntBits(tarray[i]);
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			int[] result = new int[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				long temp = Double.doubleToLongBits(tarray[i]);
				result[cur++] = (int) (temp >> 32);
				result[cur++] = (int) temp;
			}
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			int[] result = new int[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (tarray[cur++] & 0Xffff) << 16;
				result[i] |= (tarray[cur++] & 0Xffff);
			}
			if (cur < tlen) {
				result[wLen] = (tarray[cur++] & 0Xffff) << 16;
			}
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			int tlen = tarray.length;
			int[] result = new int[(tlen >> 5) + ((tlen & 31) == 0 ? 0 : 1)];
			for (int cur = 0; cur < tlen; cur++) {
				if (tarray[cur]) {
					int bits = 31 - (cur & 31);
					result[cur >> 5] |= 1 << bits;
				}
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
		return INT;
	}

	private static final long serialVersionUID = 8334888064311020340L;

}
