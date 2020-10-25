/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * @see PrimitiveType#FLOAT
 * @author XuYanhang
 *
 */
final class PrimitiveTypeFloat implements PrimitiveType<Float, float[]> {

	/**
	 * 
	 */
	PrimitiveTypeFloat() {
		super();
	}

	@Override
	public String getTypeName() {
		return "float";
	}

	@Override
	public Class<Float> primitiveClass() {
		return Float.TYPE;
	}

	@Override
	public Class<Float> referenceClass() {
		return Float.class;
	}

	@Override
	public int bitSize() {
		return Float.SIZE;
	}

	@Override
	public int byteSize() {
		return Float.SIZE >> 3;
	}

	@Override
	public Float castValue(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Float) value;
		if (otherType == BYTE || otherType == SHORT || otherType == INT || otherType == LONG || otherType == DOUBLE)
			return Float.valueOf(((Number) value).floatValue());
		if (otherType == CHAR)
			return Float.valueOf((float) ((Character) value).charValue());
		if (otherType == BOOLEAN)
			return Float.valueOf(((Boolean) value).booleanValue() ? 1.f : 0.f);
		throw new IllegalStateException();
	}

	@Override
	public Float castValueBits(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Float) value;
		int bits;
		if (otherType == BYTE)
			bits = ((Byte) value).byteValue() & 0Xff;
		else if (otherType == SHORT)
			bits = ((Short) value).shortValue() & 0Xffff;
		else if (otherType == INT)
			bits = ((Integer) value).intValue();
		else if (otherType == LONG)
			bits = ((Long) value).intValue();
		else if (otherType == DOUBLE)
			bits = (int) Double.doubleToLongBits(((Double) value).doubleValue());
		else if (otherType == CHAR)
			bits = ((Character) value).charValue() & 0Xffff;
		else if (otherType == BOOLEAN)
			bits = ((Boolean) value).booleanValue() ? 1 : 0;
		else
			throw new IllegalStateException();
		return Float.valueOf(Float.intBitsToFloat(bits));
	}

	@Override
	public float[] castArray(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (float[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			float[] result = new float[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (float) tarray[i];
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			float[] result = new float[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (float) tarray[i];
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			float[] result = new float[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (float) tarray[i];
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			float[] result = new float[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (float) tarray[i];
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			float[] result = new float[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (float) tarray[i];
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			float[] result = new float[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (float) (int) tarray[i];
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			float[] result = new float[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] ? 1.f : 0.f;
			return result;
		}
		throw new IllegalStateException();
	}

	@Override
	public float[] castArrayBits(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (float[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 2;
			float[] result = new float[wLen + ((tlen & 3) == 0 ? 0 : 1)];
			int cur = 0;
			int temp = 0;
			for (int i = 0; i < wLen; i++) {
				temp |= (tarray[cur++] & 0Xff) << 24;
				temp |= (tarray[cur++] & 0Xff) << 16;
				temp |= (tarray[cur++] & 0Xff) << 8;
				temp |= (tarray[cur++] & 0Xff);
				result[i] = Float.intBitsToFloat(temp);
				temp = 0;
			}
			int extraBits = 24;
			while (cur < tlen) {
				temp |= (tarray[cur++] & 0XffL) << extraBits;
				extraBits -= 8;
			}
			if (temp != 0) {
				result[wLen] = Float.intBitsToFloat(temp);
			}
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			float[] result = new float[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			int temp = 0;
			for (int i = 0; i < wLen; i++) {
				temp |= (tarray[cur++] & 0Xffff) << 16;
				temp |= (tarray[cur++] & 0Xffff);
				result[i] = Float.intBitsToFloat(temp);
				temp = 0;
			}
			if (cur < tlen) {
				result[wLen] = Float.intBitsToFloat((tarray[cur++] & 0Xffff) << 16);
			}
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			float[] result = new float[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = Float.intBitsToFloat(tarray[i]);
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			float[] result = new float[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				result[cur++] = Float.intBitsToFloat((int) (tarray[i] >> 32));
				result[cur++] = Float.intBitsToFloat((int) tarray[i]);
			}
			return result;
		}
		if (otherType == DOUBLE) {
			double[] tarray = (double[]) array;
			int tlen = tarray.length;
			if (tlen > (Integer.MAX_VALUE >> 1))
				throw new OutOfMemoryError();
			float[] result = new float[tlen << 1];
			int cur = 0;
			for (int i = 0; i < tlen; i++) {
				long temp = Double.doubleToLongBits(tarray[i]);
				result[cur++] = Float.intBitsToFloat((int) (temp >> 32));
				result[cur++] = Float.intBitsToFloat((int) temp);
			}
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			float[] result = new float[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			int temp = 0;
			for (int i = 0; i < wLen; i++) {
				temp |= (tarray[cur++] & 0Xffff) << 16;
				temp |= (tarray[cur++] & 0Xffff);
				result[i] = Float.intBitsToFloat(temp);
				temp = 0;
			}
			if (cur < tlen) {
				result[wLen] = Float.intBitsToFloat((tarray[cur++] & 0Xffff) << 16);
			}
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 5;
			float[] result = new float[wLen + ((tlen & 31) == 0 ? 0 : 1)];
			int temp = 0;
			for (int cur = 0; cur < tlen; cur++) {
				int bits = 31 - (cur & 31);
				if (tarray[cur]) {
					temp |= 1 << bits;
				}
				if (bits == 0) {
					result[cur >> 5] = Float.intBitsToFloat(temp);
					temp = 0;
				}
			}
			if (temp != 0) {
				result[wLen] = Float.intBitsToFloat(temp);
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
		return FLOAT;
	}

	private static final long serialVersionUID = -845687664228829268L;

}
