/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * @see PrimitiveType#DOUBLE
 * @author XuYanhang
 *
 */
final class PrimitiveTypeDouble implements PrimitiveType<Double, double[]> {

	/**
	 * 
	 */
	PrimitiveTypeDouble() {
		super();
	}

	@Override
	public String getTypeName() {
		return "double";
	}

	@Override
	public Class<Double> primitiveClass() {
		return Double.TYPE;
	}

	@Override
	public Class<Double> referenceClass() {
		return Double.class;
	}

	@Override
	public int bitSize() {
		return Double.SIZE;
	}

	@Override
	public int byteSize() {
		return Double.SIZE >> 3;
	}

	@Override
	public Double castValue(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Double) value;
		if (otherType == BYTE || otherType == SHORT || otherType == INT || otherType == LONG || otherType == FLOAT)
			return Double.valueOf(((Number) value).doubleValue());
		if (otherType == CHAR)
			return Double.valueOf((double) ((Character) value).charValue());
		if (otherType == BOOLEAN)
			return Double.valueOf(((Boolean) value).booleanValue() ? 1.d : 0.d);
		throw new IllegalStateException();
	}

	@Override
	public Double castValueBits(Object value) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofReferenceClass(value.getClass());
		if (null == otherType)
			throw new IllegalArgumentException("No primitive map");
		if (otherType == this)
			return (Double) value;
		long bits;
		if (otherType == BYTE)
			bits = ((Byte) value).byteValue() & 0XffL;
		else if (otherType == SHORT)
			bits = ((Short) value).shortValue() & 0XffffL;
		else if (otherType == INT)
			bits = ((Integer) value).intValue() & 0XffffffffL;
		else if (otherType == LONG)
			bits = ((Long) value).longValue();
		else if (otherType == FLOAT)
			bits = Float.floatToIntBits(((Float) value).floatValue()) & 0XffffffffL;
		else if (otherType == CHAR)
			bits = ((Character) value).charValue() & 0XffffL;
		else if (otherType == BOOLEAN)
			bits = ((Boolean) value).booleanValue() ? 1L : 0L;
		else
			throw new IllegalStateException();
		return Double.valueOf(Double.longBitsToDouble(bits));
	}

	@Override
	public double[] castArray(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (double[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			double[] result = new double[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (double) tarray[i];
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			double[] result = new double[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (double) tarray[i];
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			double[] result = new double[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (double) tarray[i];
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			double[] result = new double[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (double) tarray[i];
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			double[] result = new double[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (double) tarray[i];
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			double[] result = new double[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = (double) (int) tarray[i];
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			double[] result = new double[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = tarray[i] ? 1.d : 0.d;
			return result;
		}
		throw new IllegalStateException();
	}

	@Override
	public double[] castArrayBits(Object array) {
		PrimitiveType<?, ?> otherType = PrimitiveType.ofPrimitiveClass(array.getClass().getComponentType());
		if (null == otherType)
			throw new IllegalArgumentException("Not primitive array");
		if (otherType == this)
			return (double[]) array;
		if (otherType == BYTE) {
			byte[] tarray = (byte[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 3;
			double[] result = new double[wLen + ((tlen & 7) == 0 ? 0 : 1)];
			int cur = 0;
			long temp = 0;
			for (int i = 0; i < wLen; i++) {
				temp |= (tarray[cur++] & 0XffL) << 56;
				temp |= (tarray[cur++] & 0XffL) << 48;
				temp |= (tarray[cur++] & 0XffL) << 40;
				temp |= (tarray[cur++] & 0XffL) << 32;
				temp |= (tarray[cur++] & 0XffL) << 24;
				temp |= (tarray[cur++] & 0XffL) << 16;
				temp |= (tarray[cur++] & 0XffL) << 8;
				temp |= (tarray[cur++] & 0XffL);
				result[i] = Double.longBitsToDouble(temp);
				temp = 0;
			}
			int extraBits = 56;
			while (cur < tlen) {
				temp |= (tarray[cur++] & 0XffL) << extraBits;
				extraBits -= 8;
			}
			if (temp != 0) {
				result[wLen] = Double.longBitsToDouble(temp);
			}
			return result;
		}
		if (otherType == SHORT) {
			short[] tarray = (short[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 2;
			double[] result = new double[wLen + ((tlen & 3) == 0 ? 0 : 1)];
			int cur = 0;
			long temp = 0;
			for (int i = 0; i < wLen; i++) {
				temp |= (tarray[cur++] & 0XffffL) << 48;
				temp |= (tarray[cur++] & 0XffffL) << 32;
				temp |= (tarray[cur++] & 0XffffL) << 16;
				temp |= (tarray[cur++] & 0XffffL);
				result[i] = Double.longBitsToDouble(temp);
				temp = 0;
			}
			int extraBits = 48;
			while (cur < tlen) {
				temp |= (tarray[cur++] & 0XffffL) << extraBits;
				extraBits -= 16;
			}
			if (temp != 0) {
				result[wLen] = Double.longBitsToDouble(temp);
			}
			return result;
		}
		if (otherType == INT) {
			int[] tarray = (int[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			double[] result = new double[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			long temp = 0;
			for (int i = 0; i < wLen; i++) {
				temp |= (tarray[cur++] & 0XffffffffL) << 32;
				temp |= (tarray[cur++] & 0XffffffffL);
				result[i] = Double.longBitsToDouble(temp);
				temp = 0;
			}
			if (cur < tlen) {
				result[wLen] = Double.longBitsToDouble((tarray[cur++] & 0XffffffffL) << 32);
			}
			return result;
		}
		if (otherType == LONG) {
			long[] tarray = (long[]) array;
			double[] result = new double[tarray.length];
			for (int i = 0, l = result.length; i < l; i++)
				result[i] = Double.longBitsToDouble(tarray[i]);
			return result;
		}
		if (otherType == FLOAT) {
			float[] tarray = (float[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 1;
			double[] result = new double[wLen + ((tlen & 1) == 0 ? 0 : 1)];
			int cur = 0;
			long temp = 0;
			for (int i = 0; i < wLen; i++) {
				temp |= (Float.floatToIntBits(tarray[cur++]) & 0XffffffffL) << 32;
				temp |= (Float.floatToIntBits(tarray[cur++]) & 0XffffffffL);
				result[i] = Double.longBitsToDouble(temp);
				temp = 0;
			}
			if (cur < tlen) {
				result[wLen] = Double.longBitsToDouble((Float.floatToIntBits(tarray[cur++]) & 0XffffffffL) << 32);
			}
			return result;
		}
		if (otherType == CHAR) {
			char[] tarray = (char[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 2;
			double[] result = new double[wLen + ((tlen & 3) == 0 ? 0 : 1)];
			int cur = 0;
			long temp = 0;
			for (int i = 0; i < wLen; i++) {
				temp |= (tarray[cur++] & 0XffffL) << 48;
				temp |= (tarray[cur++] & 0XffffL) << 32;
				temp |= (tarray[cur++] & 0XffffL) << 16;
				temp |= (tarray[cur++] & 0XffffL);
				result[i] = Double.longBitsToDouble(temp);
				temp = 0;
			}
			int extraBits = 48;
			while (cur < tlen) {
				temp |= (tarray[cur++] & 0XffffL) << extraBits;
				extraBits -= 16;
			}
			if (temp != 0) {
				result[wLen] = Double.longBitsToDouble(temp);
			}
			return result;
		}
		if (otherType == BOOLEAN) {
			boolean[] tarray = (boolean[]) array;
			int tlen = tarray.length;
			int wLen = tlen >> 6;
			double[] result = new double[wLen + ((tlen & 63) == 0 ? 0 : 1)];
			long temp = 0;
			for (int cur = 0; cur < tlen; cur++) {
				int bits = 63 - (cur & 63);
				if (tarray[cur]) {
					temp |= 1L << bits;
				}
				if (bits == 0) {
					result[cur >> 6] = Double.longBitsToDouble(temp);
					temp = 0;
				}
			}
			if (temp != 0) {
				result[wLen] = Double.longBitsToDouble(temp);
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
		return DOUBLE;
	}

	private static final long serialVersionUID = -438422306484440928L;

}
