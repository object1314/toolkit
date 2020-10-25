/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * @see PrimitiveType#CHAR
 * @author XuYanhang
 *
 */
final class PrimitiveTypeChar implements PrimitiveType<Character, char[]> {

	/**
	 * 
	 */
	PrimitiveTypeChar() {
		super();
	}

	@Override
	public String getTypeName() {
		return "char";
	}

	@Override
	public Class<Character> primitiveClass() {
		return Character.TYPE;
	}

	@Override
	public Class<Character> referenceClass() {
		return Character.class;
	}

	@Override
	public int bitSize() {
		return Character.SIZE;
	}

	@Override
	public int byteSize() {
		return Character.BYTES;
	}

	@Override
	public Character castValue(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Character) value;
		if (otherType == BYTE)
			return Character.valueOf((char) (((Byte) value).byteValue() & 0Xff));
		if (otherType == SHORT)
			return Character.valueOf((char) (((Short) value).shortValue() & 0Xffff));
		if (otherType == INT || otherType == FLOAT || otherType == DOUBLE)
			return Character.valueOf((char) ((Number) value).intValue());
		if (otherType == LONG)
			return Character.valueOf((char) ((Long) value).longValue());
		if (otherType == BOOLEAN)
			return Character.valueOf(((Boolean) value).booleanValue() ? '\1' : '\0');
		throw new IllegalStateException();
	}

	@Override
	public Character castValueBits(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Character) value;
		if (otherType == BYTE)
			return Character.valueOf((char) (((Byte) value).byteValue() & 0Xff));
		if (otherType == SHORT)
			return Character.valueOf((char) (((Short) value).shortValue() & 0Xffff));
		if (otherType == INT)
			return Character.valueOf((char) ((Integer) value).intValue());
		if (otherType == LONG)
			return Character.valueOf((char) ((Long) value).longValue());
		if (otherType == FLOAT)
			return Character.valueOf((char) Float.floatToIntBits(((Float) value).floatValue()));
		if (otherType == DOUBLE)
			return Character.valueOf((char) Double.doubleToLongBits(((Double) value).doubleValue()));
		if (otherType == BOOLEAN)
			return Character.valueOf(((Boolean) value).booleanValue() ? '\1' : '\0');
		throw new IllegalStateException();
	}

	@Override
	public char[] castArray(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (char[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			char[] result = new char[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (char) (tarray[i] & 0Xff);
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			char[] result = new char[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (char) (tarray[i] & 0Xffff);
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			char[] result = new char[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (char) tarray[i];
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			char[] result = new char[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (char) tarray[i];
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			char[] result = new char[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (char) (int) tarray[i];
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			char[] result = new char[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (char) (int) tarray[i];
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			char[] result = new char[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] ? '\1' : '\0';
			return result;
		}
		throw new IllegalStateException();
	}

	@Override
	public char[] castArrayBits(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (char[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			char[] result = new char[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				result[i] |= (tarray[cur++] & 0Xff) << 8;
				result[i] |= (tarray[cur++] & 0Xff);
			}
			if (cur < tlen) {
				result[wLen] = (char) ((tarray[cur++] & 0Xff) << 8);
			}
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			char[] result = new char[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (char) (int) tarray[i];
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			char[] result = new char[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = (char) (tarray[i] >> 16);
				result[cur++] = (char) tarray[i];
			}
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 2))
				throw new OutOfMemoryError();
			char[] result = new char[tlen << 2];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = (char) (tarray[i] >> 48);
				result[cur++] = (char) (tarray[i] >> 32);
				result[cur++] = (char) (tarray[i] >> 16);
				result[cur++] = (char) tarray[i];
			}
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			char[] result = new char[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				int temp = Float.floatToIntBits(tarray[i]);
				result[cur++] = (char) (temp >> 16);
				result[cur++] = (char) temp;
			}
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 2))
				throw new OutOfMemoryError();
			char[] result = new char[tlen << 2];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				long temp = Double.doubleToLongBits(tarray[i]);
				result[cur++] = (char) (temp >> 48);
				result[cur++] = (char) (temp >> 32);
				result[cur++] = (char) (temp >> 16);
				result[cur++] = (char) tarray[i];
			}
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			int tlen = tarray.length;
			char[] result = new char[(tlen >> 4) + ((tlen & 15) == 0 ? 0 : 1)];
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
		return CHAR;
	}

	private static final long serialVersionUID = 1299120884568194865L;

}
