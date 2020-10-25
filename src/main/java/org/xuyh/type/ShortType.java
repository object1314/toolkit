/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * @see PrimitiveType#SHORT
 * @author XuYanhang
 *
 */
final class ShortType implements PrimitiveType<Short, short[]> {

	/**
	 * 
	 */
	ShortType() {
		super();
	}

	@Override
	public String getTypeName() {
		return "short";
	}

	@Override
	public Class<Short> primitiveClass() {
		return Short.TYPE;
	}

	@Override
	public Class<Short> referenceClass() {
		return Short.class;
	}

	@Override
	public int bitSize() {
		return Short.SIZE;
	}

	@Override
	public int byteSize() {
		return Short.SIZE >> 3;
	}

	@Override
	public Short castValue(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Short) value;
		if (otherType == BYTE || otherType == INT || otherType == LONG || otherType == FLOAT || otherType == DOUBLE)
			return Short.valueOf(((Number) value).shortValue());
		if (otherType == CHAR)
			return Short.valueOf((short) ((Character) value).charValue());
		if (otherType == BOOLEAN)
			return Short.valueOf((short) (((Boolean) value).booleanValue() ? 1 : 0));
		throw new IllegalStateException();
	}

	@Override
	public Short castValueBits(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Short) value;
		if (otherType == BYTE)
			return Short.valueOf((short) (((Byte) value).byteValue() & 0Xff));
		if (otherType == INT)
			return Short.valueOf(((Integer) value).shortValue());
		if (otherType == LONG)
			return Short.valueOf(((Long) value).shortValue());
		if (otherType == FLOAT)
			return Short.valueOf((short) Float.floatToIntBits(((Float) value).floatValue()));
		if (otherType == DOUBLE)
			return Short.valueOf((short) Double.doubleToLongBits(((Double) value).doubleValue()));
		if (otherType == BOOLEAN)
			return Short.valueOf((short) (((Boolean) value).booleanValue() ? 1 : 0));
		if (otherType == CHAR)
			return Short.valueOf((short) ((Character) value).charValue());
		throw new IllegalStateException();
	}

	@Override
	public short[] castArray(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (short[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			short[] result = new short[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (short) tarray[i];
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			short[] result = new short[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (short) tarray[i];
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			short[] result = new short[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (short) tarray[i];
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			short[] result = new short[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (short) tarray[i];
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			short[] result = new short[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (short) tarray[i];
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			short[] result = new short[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (short) (int) tarray[i];
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			short[] result = new short[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (short) (tarray[i] ? 1 : 0);
			return result;
		}
		throw new IllegalStateException();
	}

	@Override
	public short[] castArrayBits(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (short[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			short[] result = new short[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (tarray[cur++] & 0Xff) << 8;
				result[i] |= (tarray[cur++] & 0Xff);
			}
			if (cur < tlen) {
				result[wLen] = (short) ((tarray[cur++] & 0Xff) << 8);
			}
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			short[] result = new short[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = (short) (tarray[i] >> 16);
				result[cur++] = (short) tarray[i];
			}
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 2))
				throw new OutOfMemoryError();
			short[] result = new short[tlen << 2];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = (short) (tarray[i] >> 48);
				result[cur++] = (short) (tarray[i] >> 32);
				result[cur++] = (short) (tarray[i] >> 16);
				result[cur++] = (short) tarray[i];
			}
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			short[] result = new short[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				int temp = Float.floatToIntBits(tarray[i]);
				result[cur++] = (short) (temp >> 16);
				result[cur++] = (short) temp;
			}
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 2))
				throw new OutOfMemoryError();
			short[] result = new short[tlen << 2];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				long temp = Double.doubleToLongBits(tarray[i]);
				result[cur++] = (short) (temp >> 48);
				result[cur++] = (short) (temp >> 32);
				result[cur++] = (short) (temp >> 16);
				result[cur++] = (short) tarray[i];
			}
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			short[] result = new short[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (short) (int) tarray[i];
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			int tlen = tarray.length;
			short[] result = new short[(tlen >> 4) + ((tlen & 15) == 0 ? 0 : 1)];
			for (int cur = 0; cur < tlen; cur++) {
				if (tarray[cur]) {
					int bits = 15 - (cur & 15);
					result[cur >> 4] |= 1 << bits;
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
		return SHORT;
	}

	private static final long serialVersionUID = -1720905130956550324L;

}
