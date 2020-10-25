/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * @see PrimitiveType#LONG
 * @author XuYanhang
 *
 */
final class PrimitiveTypeLong implements PrimitiveType<Long, long[]> {

	/**
	 * 
	 */
	PrimitiveTypeLong() {
		super();
	}

	@Override
	public String getTypeName() {
		return "long";
	}

	@Override
	public Class<Long> primitiveClass() {
		return Long.TYPE;
	}

	@Override
	public Class<Long> referenceClass() {
		return Long.class;
	}

	@Override
	public int bitSize() {
		return Long.SIZE;
	}

	@Override
	public int byteSize() {
		return Long.SIZE >> 3;
	}

	@Override
	public Long castValue(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Long) value;
		if (otherType == BYTE || otherType == SHORT || otherType == INT || otherType == FLOAT || otherType == DOUBLE)
			return Long.valueOf(((Number) value).longValue());
		if (otherType == CHAR)
			return Long.valueOf((long) ((Character) value).charValue());
		if (otherType == BOOLEAN)
			return Long.valueOf(((Boolean) value).booleanValue() ? 1L : 0L);
		throw new IllegalStateException();
	}

	@Override
	public Long castValueBits(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Long) value;
		if (otherType == BYTE)
			return Long.valueOf(((Byte) value).byteValue() & 0XffL);
		if (otherType == SHORT)
			return Long.valueOf(((Short) value).shortValue() & 0XffffL);
		if (otherType == INT)
			return Long.valueOf(((Integer) value).intValue() & 0XffffffffL);
		if (otherType == FLOAT)
			return Long.valueOf(Float.floatToIntBits(((Float) value).floatValue()) & 0XffffffffL);
		if (otherType == DOUBLE)
			return Long.valueOf(Double.doubleToLongBits(((Double) value).doubleValue()));
		if (otherType == CHAR)
			return Long.valueOf(((Character) value).charValue() & 0XffffL);
		if (otherType == BOOLEAN)
			return Long.valueOf(((Boolean) value).booleanValue() ? 1L : 0L);
		throw new IllegalStateException();
	}

	@Override
	public long[] castArray(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (long[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			long[] result = new long[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (long) tarray[i];
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			long[] result = new long[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (long) tarray[i];
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			long[] result = new long[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (long) tarray[i];
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			long[] result = new long[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (long) tarray[i];
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			long[] result = new long[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (long) tarray[i];
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			long[] result = new long[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (long) tarray[i];
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			long[] result = new long[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] ? 1L : 0L;
			return result;
		}
		throw new IllegalStateException();
	}

	@Override
	public long[] castArrayBits(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (long[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 3;
			long[] result = new long[wLen + ((tlen & 7) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (tarray[cur++] & 0XffL) << 56;
				result[i] |= (tarray[cur++] & 0XffL) << 48;
				result[i] |= (tarray[cur++] & 0XffL) << 40;
				result[i] |= (tarray[cur++] & 0XffL) << 32;
				result[i] |= (tarray[cur++] & 0XffL) << 24;
				result[i] |= (tarray[cur++] & 0XffL) << 16;
				result[i] |= (tarray[cur++] & 0XffL) << 8;
				result[i] |= (tarray[cur++] & 0XffL);
			}
			int extraBits = 56;
			while (cur < tlen) {
				result[wLen] |= (tarray[cur++] & 0XffL) << extraBits;
				extraBits -= 8;
			}
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 2;
			long[] result = new long[wLen + ((tlen & 3) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (tarray[cur++] & 0XffffL) << 48;
				result[i] |= (tarray[cur++] & 0XffffL) << 32;
				result[i] |= (tarray[cur++] & 0XffffL) << 16;
				result[i] |= (tarray[cur++] & 0XffffL);
			}
			int extraBits = 48;
			while (cur < tlen) {
				result[wLen] |= (tarray[cur++] & 0XffffL) << extraBits;
				extraBits -= 16;
			}
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			long[] result = new long[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (tarray[cur++] & 0XffffffffL) << 32;
				result[i] |= (tarray[cur++] & 0XffffffffL);
			}
			if (cur < tlen) {
				result[wLen] = (tarray[cur++] & 0XffffffffL) << 32;
			}
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			long[] result = new long[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (Float.floatToIntBits(tarray[cur++]) & 0XffffffffL) << 32;
				result[i] |= (Float.floatToIntBits(tarray[cur++]) & 0XffffffffL);
			}
			if (cur < tlen) {
				result[wLen] = (Float.floatToIntBits(tarray[cur++]) & 0XffffffffL) << 32;
			}
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			long[] result = new long[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = Double.doubleToLongBits(tarray[i]);
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 2;
			long[] result = new long[wLen + ((tlen & 3) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (tarray[cur++] & 0XffffL) << 48;
				result[i] |= (tarray[cur++] & 0XffffL) << 32;
				result[i] |= (tarray[cur++] & 0XffffL) << 16;
				result[i] |= (tarray[cur++] & 0XffffL);
			}
			int extraBits = 48;
			while (cur < tlen) {
				result[wLen] |= (tarray[cur++] & 0XffffL) << extraBits;
				extraBits -= 16;
			}
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			int tlen = tarray.length;
			long[] result = new long[(tlen >> 6) + ((tlen & 63) == 0 ? 0 : 1)];
			for (int cur = 0; cur < tlen; cur++) {
				if (tarray[cur]) {
					int bits = 63 - (cur & 63);
					result[cur >> 6] |= 1L << bits;
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
		return LONG;
	}

	private static final long serialVersionUID = -365883089596918688L;

}
