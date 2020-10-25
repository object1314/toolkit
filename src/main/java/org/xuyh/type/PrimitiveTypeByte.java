/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * @see PrimitiveType#BYTE
 * @author XuYanhang
 *
 */
final class PrimitiveTypeByte implements PrimitiveType<Byte, byte[]> {

	/**
	 * 
	 */
	PrimitiveTypeByte() {
		super();
	}

	@Override
	public String getTypeName() {
		return "byte";
	}

	@Override
	public Class<Byte> primitiveClass() {
		return Byte.TYPE;
	}

	@Override
	public Class<Byte> referenceClass() {
		return Byte.class;
	}

	@Override
	public int bitSize() {
		return Byte.SIZE;
	}

	@Override
	public int byteSize() {
		return Byte.BYTES;
	}

	@Override
	public Byte castValue(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Byte) value;
		if (otherType == SHORT || otherType == INT || otherType == LONG || otherType == FLOAT || otherType == DOUBLE)
			return Byte.valueOf(((Number) value).byteValue());
		if (otherType == CHAR)
			return Byte.valueOf((byte) ((Character) value).charValue());
		if (otherType == BOOLEAN)
			return Byte.valueOf((byte) (((Boolean) value).booleanValue() ? 1 : 0));
		throw new IllegalStateException();
	}

	@Override
	public Byte castValueBits(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Byte) value;
		if (otherType == SHORT)
			return Byte.valueOf(((Short) value).byteValue());
		if (otherType == INT)
			return Byte.valueOf(((Integer) value).byteValue());
		if (otherType == LONG)
			return Byte.valueOf(((Long) value).byteValue());
		if (otherType == FLOAT)
			return Byte.valueOf((byte) Float.floatToIntBits(((Float) value).floatValue()));
		if (otherType == DOUBLE)
			return Byte.valueOf((byte) Double.doubleToLongBits(((Double) value).doubleValue()));
		if (otherType == CHAR)
			return Byte.valueOf((byte) ((Character) value).charValue());
		if (otherType == BOOLEAN)
			return Byte.valueOf((byte) (((Boolean) value).booleanValue() ? 1 : 0));
		throw new IllegalStateException();
	}

	@Override
	public byte[] castArray(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (byte[]) array;
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			byte[] result = new byte[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (byte) tarray[i];
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			byte[] result = new byte[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (byte) tarray[i];
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			byte[] result = new byte[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (byte) tarray[i];
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			byte[] result = new byte[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (byte) tarray[i];
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			byte[] result = new byte[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (byte) tarray[i];
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			byte[] result = new byte[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (byte) (int) tarray[i];
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			byte[] result = new byte[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (byte) (tarray[i] ? 1 : 0);
			return result;
		}
		throw new IllegalStateException();
	}

	@Override
	public byte[] castArrayBits(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (byte[]) array;
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			byte[] result = new byte[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = (byte) (tarray[i] >> 8);
				result[cur++] = (byte) tarray[i];
			}
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 2))
				throw new OutOfMemoryError();
			byte[] result = new byte[tlen << 2];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = (byte) (tarray[i] >> 24);
				result[cur++] = (byte) (tarray[i] >> 16);
				result[cur++] = (byte) (tarray[i] >> 8);
				result[cur++] = (byte) tarray[i];
			}
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 3))
				throw new OutOfMemoryError();
			byte[] result = new byte[tlen << 3];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = (byte) (tarray[i] >> 56);
				result[cur++] = (byte) (tarray[i] >> 48);
				result[cur++] = (byte) (tarray[i] >> 40);
				result[cur++] = (byte) (tarray[i] >> 32);
				result[cur++] = (byte) (tarray[i] >> 24);
				result[cur++] = (byte) (tarray[i] >> 16);
				result[cur++] = (byte) (tarray[i] >> 8);
				result[cur++] = (byte) tarray[i];
			}
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 2))
				throw new OutOfMemoryError();
			byte[] result = new byte[tlen << 2];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				int temp = Float.floatToIntBits(tarray[i]);
				result[cur++] = (byte) (temp >> 24);
				result[cur++] = (byte) (temp >> 16);
				result[cur++] = (byte) (temp >> 8);
				result[cur++] = (byte) temp;
			}
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 3))
				throw new OutOfMemoryError();
			byte[] result = new byte[tlen << 3];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				long temp = Double.doubleToLongBits(tarray[i]);
				result[cur++] = (byte) (temp >> 56);
				result[cur++] = (byte) (temp >> 48);
				result[cur++] = (byte) (temp >> 40);
				result[cur++] = (byte) (temp >> 32);
				result[cur++] = (byte) (temp >> 24);
				result[cur++] = (byte) (temp >> 16);
				result[cur++] = (byte) (temp >> 8);
				result[cur++] = (byte) temp;
			}
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			byte[] result = new byte[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = (byte) (tarray[i] >> 8);
				result[cur++] = (byte) (tarray[i] & 0Xff);
			}
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 3;
			byte[] result = new byte[wLen + ((tlen & 7) == 0 ? 0 : 1)];
			int cur = 0;
			for (int i = 0; i < wLen; i++) {
				if (tarray[cur++])
					result[i] |= 1 << 7;
				if (tarray[cur++])
					result[i] |= 1 << 6;
				if (tarray[cur++])
					result[i] |= 1 << 5;
				if (tarray[cur++])
					result[i] |= 1 << 4;
				if (tarray[cur++])
					result[i] |= 1 << 3;
				if (tarray[cur++])
					result[i] |= 1 << 2;
				if (tarray[cur++])
					result[i] |= 1 << 1;
				if (tarray[cur++])
					result[i] |= 1;
			}
			int extraBits = 56;
			while (cur < tlen) {
				if (tarray[cur++])
					result[wLen] |= 1 << extraBits;
				extraBits -= 8;
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
		return BYTE;
	}

	private static final long serialVersionUID = 7037679568082340977L;

}
