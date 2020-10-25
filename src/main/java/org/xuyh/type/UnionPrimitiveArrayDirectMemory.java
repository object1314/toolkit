/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

/**
 * Direct memory array. Array package interface in primitive type. Available of
 * byte[], short[], int[], long[], float[], double[], char[], boolean[]. These
 * types share a same memory.
 * 
 * @author XuYanhang
 * @since 2020-10-24
 */
class UnionPrimitiveArrayDirectMemory implements UnionPrimitiveArray, java.io.Externalizable {

	private static Unsafe unsafe = Unsafe.getUnsafe();

	private transient long address;

	private transient long bits;

	UnionPrimitiveArrayDirectMemory(long bits) {
		super();
		if (bits < 0)
			throw new OutOfMemoryError();
		long bytes = (bits >> 3) + ((bits & 7) == 0 ? 0 : 1);
		this.address = 0 == bytes ? 0 : unsafe.allocateMemory(bytes);
		this.bits = bits;
	}

	@Override
	public long bits() {
		return bits;
	}

	@Override
	public long byteLength() {
		return bits >> 3;
	}

	@Override
	public long shortLength() {
		return bits >> 4;
	}

	@Override
	public long intLength() {
		return bits >> 5;
	}

	@Override
	public long longLength() {
		return bits >> 6;
	}

	@Override
	public long floatLength() {
		return bits >> 5;
	}

	@Override
	public long doubleLength() {
		return bits >> 6;
	}

	@Override
	public long charLength() {
		return bits >> 4;
	}

	@Override
	public long booleanLength() {
		return bits;
	}

	@Override
	public byte getByte(long index) {
		if (index < 0 || index >= bits >> 3)
			throw new IndexOutOfBoundsException();
		return unsafe.getByte(address + index);
	}

	@Override
	public short getShort(long index) {
		if (index < 0 || index >= bits >> 4)
			throw new IndexOutOfBoundsException();
		return unsafeGetShort(address + (index << 1));
	}

	@Override
	public int getInt(long index) {
		if (index < 0 || index >= bits >> 5)
			throw new IndexOutOfBoundsException();
		return unsafeGetInt(address + (index << 2));
	}

	@Override
	public long getLong(long index) {
		if (index < 0 || index >= bits >> 6)
			throw new IndexOutOfBoundsException();
		return unsafeGetLong(address + (index << 3));
	}

	@Override
	public float getFloat(long index) {
		if (index < 0 || index >= bits >> 5)
			throw new IndexOutOfBoundsException();
		return Float.intBitsToFloat(unsafeGetInt(address + (index << 2)));
	}

	@Override
	public double getDouble(long index) {
		if (index < 0 || index >= bits >> 6)
			throw new IndexOutOfBoundsException();
		return Double.longBitsToDouble(unsafeGetLong(address + (index << 3)));
	}

	@Override
	public char getChar(long index) {
		if (index < 0 || index >= bits >> 4)
			throw new IndexOutOfBoundsException();
		return unsafeGetChar(address + (index << 1));
	}

	@Override
	public boolean getBoolean(long index) {
		if (index < 0 || index >= bits)
			throw new IndexOutOfBoundsException();
		byte b = unsafe.getByte(address + (index >> 3));
		return ((b >> (7 - (index & 7))) & 1) != 0;
	}

	@Override
	public void setByte(long index, byte value) {
		if (index < 0 || index >= bits >> 3)
			throw new IndexOutOfBoundsException();
		unsafe.putByte(address + index, value);
	}

	@Override
	public void setShort(long index, short value) {
		if (index < 0 || index >= bits >> 4)
			throw new IndexOutOfBoundsException();
		unsafePutShort(address + (index << 1), value);
	}

	@Override
	public void setInt(long index, int value) {
		if (index < 0 || index >= bits >> 5)
			throw new IndexOutOfBoundsException();
		unsafePutInt(address + (index << 2), value);
	}

	@Override
	public void setLong(long index, long value) {
		if (index < 0 || index >= bits >> 6)
			throw new IndexOutOfBoundsException();
		unsafePutLong(address + (index << 3), value);
	}

	@Override
	public void setFloat(long index, float value) {
		if (index < 0 || index >= bits >> 5)
			throw new IndexOutOfBoundsException();
		unsafePutInt(address + (index << 2), Float.floatToIntBits(value));
	}

	@Override
	public void setDouble(long index, double value) {
		if (index < 0 || index >= bits >> 6)
			throw new IndexOutOfBoundsException();
		unsafePutLong(address + (index << 3), Double.doubleToLongBits(value));
	}

	@Override
	public void setChar(long index, char value) {
		if (index < 0 || index >= bits >> 4)
			throw new IndexOutOfBoundsException();
		unsafePutChar(address + (index << 1), value);
	}

	@Override
	public void setBoolean(long index, boolean value) {
		if (index < 0 || index >= bits)
			throw new IndexOutOfBoundsException();
		long addr = address + (index >> 3);
		byte b = unsafe.getByte(addr);
		int bits = 7 - (int) (index & 7);
		if (value)
			b |= (1 << bits);
		else
			b &= (~(1 << bits));
		unsafe.putByte(addr, b);
	}

	@Override
	public void readBytes(long index, byte[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 3)
			throw new IndexOutOfBoundsException();
		long i = address + index;
		int j = off;
		while (j < oto)
			value[j++] = unsafe.getByte(i++);
	}

	@Override
	public void readShorts(long index, short[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 4)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 1);
		int j = off;
		while (j < oto) {
			value[j++] = unsafeGetShort(i);
			i += 2;
		}
	}

	@Override
	public void readInts(long index, int[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 5)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 2);
		int j = off;
		while (j < oto) {
			value[j++] = unsafeGetInt(i);
			i += 4;
		}
	}

	@Override
	public void readLongs(long index, long[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 6)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 3);
		int j = off;
		while (j < oto) {
			value[j++] = unsafeGetLong(i);
			i += 8;
		}
	}

	@Override
	public void readFloats(long index, float[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 5)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 2);
		int j = off;
		while (j < oto) {
			value[j++] = Float.intBitsToFloat(unsafeGetInt(i));
			i += 4;
		}
	}

	@Override
	public void readDoubles(long index, double[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 6)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 3);
		int j = off;
		while (j < oto) {
			value[j++] = Double.longBitsToDouble(unsafeGetLong(i));
			i += 8;
		}
	}

	@Override
	public void readChars(long index, char[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 4)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 1);
		int j = off;
		while (j < oto) {
			value[j++] = unsafeGetChar(i);
			i += 2;
		}
	}

	@Override
	public void readBooleans(long index, boolean[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits)
			throw new IndexOutOfBoundsException();
		long addr = address + (index >> 3);
		long endAddr = address + (tto >> 3);
		int bits = 7 - (int) (index & 7);
		byte b;
		int cur = off;
		// Resolve mainly
		while (addr < endAddr) {
			b = unsafe.getByte(addr);
			while (bits < 7) {
				value[cur++] = ((b >> (bits--)) & 1) != 0;
			}
			bits = 7;
			addr++;
		}
		// Resolve extra
		if (cur < oto) {
			b = unsafe.getByte(addr);
			while (cur < oto) {
				value[cur++] = ((b >> (bits--)) & 1) != 0;
			}
		}
	}

	@Override
	public void putBytes(long index, byte[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 3)
			throw new IndexOutOfBoundsException();
		long i = address + index;
		int j = off;
		while (j < oto)
			unsafe.putByte(i++, value[j++]);
	}

	@Override
	public void putShorts(long index, short[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 4)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 1);
		int j = off;
		while (j < oto) {
			unsafePutShort(i, value[j++]);
			i += 2;
		}
	}

	@Override
	public void putInts(long index, int[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 5)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 2);
		int j = off;
		while (j < oto) {
			unsafePutInt(i, value[j++]);
			i += 4;
		}
	}

	@Override
	public void putLongs(long index, long[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 6)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 3);
		int j = off;
		while (j < oto) {
			unsafePutLong(i, value[j++]);
			i += 8;
		}
	}

	@Override
	public void putFloats(long index, float[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 5)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 2);
		int j = off;
		while (j < oto) {
			unsafePutInt(i, Float.floatToIntBits(value[j++]));
			i += 4;
		}
	}

	@Override
	public void putDoubles(long index, double[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 6)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 3);
		int j = off;
		while (j < oto) {
			unsafePutLong(i, Double.doubleToLongBits(value[j++]));
			i += 8;
		}
	}

	@Override
	public void putChars(long index, char[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits >> 4)
			throw new IndexOutOfBoundsException();
		long i = address + (index << 1);
		int j = off;
		while (j < oto) {
			unsafePutChar(i, value[j++]);
			i += 2;
		}
	}

	@Override
	public void putBooleans(long index, boolean[] value, int off, int len) {
		int oto = off + len;
		if (off < 0 || oto > value.length || oto < off)
			throw new IndexOutOfBoundsException();
		long tto = index + len;
		if (index < 0 || tto < 0 || tto > bits)
			throw new IndexOutOfBoundsException();
		long addr = address + (index >> 3);
		long endAddr = address + (tto >> 3);
		int bits = 7 - (int) (index & 7);
		byte b;
		int cur = off;
		// Resolve head
		if (bits < 7 && addr < endAddr) {
			b = unsafe.getByte(addr);
			while (bits < 7) {
				if (value[cur++])
					b |= 1 << bits;
				else
					b &= ~(1 << bits);
				bits--;
			}
			unsafe.putByte(addr, b);
			bits = 7;
			addr++;
		}
		// Resolve body
		while (addr < endAddr) {
			b = 0;
			while (bits < 7) {
				if (value[cur++])
					b |= 1 << bits;
				bits--;
			}
			unsafe.putByte(addr, b);
			addr++;
		}
		// Resolve tail
		if (cur < oto) {
			b = unsafe.getByte(addr);
			while (cur < oto) {
				if (value[cur++])
					b |= 1 << bits;
				else
					b &= ~(1 << bits);
				bits--;
				cur++;
			}
			unsafe.putByte(addr, b);
		}
	}

	@Override
	public void fillBytes(byte value, long off, long len) {
		if (off < 0 || off + len < off || off + len > bits >> 3)
			throw new IndexOutOfBoundsException();
		long addr = address + off;
		if (value == 0) {
			unsafe.setMemory(addr, len, (byte) 0);
		} else if (value == (byte) 0Xff) {
			unsafe.setMemory(addr, len, (byte) 0Xff);
		} else {
			for (long i = addr, end = addr + len; i < end; i++)
				unsafe.putByte(i, value);
		}
	}

	@Override
	public void fillShorts(short value, long off, long len) {
		if (off < 0 || off + len < off || off + len > bits >> 4)
			throw new IndexOutOfBoundsException();
		long addr = address + (off << 1);
		if (value == 0) {
			unsafe.setMemory(addr, len << 1, (byte) 0);
		} else if (value == (short) 0Xffff) {
			unsafe.setMemory(addr, len << 1, (byte) 0Xff);
		} else {
			for (long i = addr, end = addr + (len << 1); i < end; i += 2)
				unsafePutShort(i, value);
		}
	}

	@Override
	public void fillInts(int value, long off, long len) {
		if (off < 0 || off + len < off || off + len > bits >> 5)
			throw new IndexOutOfBoundsException();
		long addr = address + (off << 2);
		if (value == 0) {
			unsafe.setMemory(addr, len << 2, (byte) 0);
		} else if (value == 0Xffffffff) {
			unsafe.setMemory(addr, len << 2, (byte) 0Xff);
		} else {
			for (long i = addr, end = addr + (len << 2); i < end; i += 4)
				unsafePutInt(i, value);
		}
	}

	@Override
	public void fillLongs(long value, long off, long len) {
		if (off < 0 || off + len < off || off + len > bits >> 6)
			throw new IndexOutOfBoundsException();
		long addr = address + (off << 3);
		if (value == 0L) {
			unsafe.setMemory(addr, len << 3, (byte) 0);
		} else if (value == 0XffffffffffffffffL) {
			unsafe.setMemory(addr, len << 3, (byte) 0Xff);
		} else {
			for (long i = addr, end = addr + (len << 3); i < end; i += 8)
				unsafePutLong(i, value);
		}
	}

	@Override
	public void fillFloats(float value, long off, long len) {
		fillInts(Float.floatToIntBits(value), off, len);
	}

	@Override
	public void fillDoubles(double value, long off, long len) {
		fillLongs(Double.doubleToLongBits(value), off, len);
	}

	@Override
	public void fillChars(char value, long off, long len) {
		if (off < 0 || off + len < off || off + len > bits >> 4)
			throw new IndexOutOfBoundsException();
		long addr = address + (off << 1);
		if (value == '\0') {
			unsafe.setMemory(addr, len << 1, (byte) 0);
		} else if (value == '\uffff') {
			unsafe.setMemory(addr, len << 1, (byte) 0Xff);
		} else {
			for (long i = addr, end = addr + (len << 1); i < end; i += 2)
				unsafePutChar(i, value);
		}
	}

	@Override
	public void fillBooleans(boolean value, long off, long len) {
		long to = off + len;
		if (off < 0 || to < off || to > bits)
			throw new IndexOutOfBoundsException();
		long addr = address + (off >> 3);
		long endAddr = address + (to >> 3);
		int bits = 7 - (int) (off & 7);
		byte b;
		long cur = off;
		// Resolve head
		if (bits < 7 && addr < endAddr) {
			b = unsafe.getByte(addr);
			while (bits < 7) {
				if (value)
					b |= 1 << bits;
				else
					b &= ~(1 << bits);
				bits--;
				cur++;
			}
			unsafe.putByte(addr, b);
			bits = 7;
			addr++;
		}
		// Resolve body
		if (addr < endAddr) {
			unsafe.setMemory(addr, endAddr - addr, (byte) (value ? 0Xff : 0));
			cur += (endAddr - addr) << 3;
			addr = endAddr;
		}
		// Resolve tail
		if (cur < to) {
			b = unsafe.getByte(addr);
			while (cur < to) {
				if (value)
					b |= 1 << bits;
				else
					b &= ~(1 << bits);
				bits--;
				cur++;
			}
			unsafe.putByte(addr, b);
		}
	}

	@Override
	public void rebase(long bits) {
		if (bits < 0)
			throw new OutOfMemoryError();
		long bytes = (bits >> 3) + ((bits & 7) == 0 ? 0 : 1);
		long oldBytes = (this.bits >> 3) + ((this.bits & 7) == 0 ? 0 : 1);
		if (bytes != oldBytes) {
			if (address == 0L) {
				this.address = unsafe.allocateMemory(bytes);
			} else {
				this.address = unsafe.reallocateMemory(address, bytes);
			}
		}
		this.bits = bits;
	}

	@Override
	public void clear() {
		long bytes = (bits >> 3) + ((bits & 7) == 0 ? 0 : 1);
		unsafe.setMemory(bytes, bytes, (byte) 0);
	}

	@Override
	public void free() {
		if (address != 0L) {
			unsafe.freeMemory(address);
			address = 0L;
			bits = 0L;
		}
	}

	@Override
	public void copyBits(long thisOff, UnionPrimitiveArray target, long targetOff, long bits) {
		long thisEnd = thisOff + bits;
		if (thisOff < 0 || thisEnd < thisOff || thisEnd > bits())
			throw new IndexOutOfBoundsException();
		long targetEnd = targetOff + bits;
		if (targetOff < 0 || targetEnd < targetOff || targetEnd > target.bits())
			throw new IndexOutOfBoundsException();
		if ((thisOff & 7) != (targetOff & 7)) {
			while (thisOff < thisEnd)
				target.setBoolean(targetOff++, getBoolean(thisOff++));
			return;
		}
		long thisAddr = address + (thisOff >> 3);
		long thisEndAddr = address + (thisEnd >> 3);
		int extraBits = 7 - (int) (thisOff & 7);
		byte b;
		long cur = targetOff;
		// Resolve head
		if (extraBits < 7 && thisAddr < thisEndAddr) {
			b = unsafe.getByte(thisAddr);
			while (extraBits < 7) {
				target.setBoolean(cur++, ((b >> bits) & 1) != 0);
				extraBits--;
			}
			extraBits = 7;
			thisAddr++;
		}
		// Resolve body
		if (thisAddr < thisEndAddr) {
			if (target instanceof UnionPrimitiveArrayDirectMemory) {
				UnionPrimitiveArrayDirectMemory other = (UnionPrimitiveArrayDirectMemory) target;
				unsafe.copyMemory(thisAddr, other.address + (cur >> 3), thisEndAddr - thisAddr);
				thisAddr = thisEndAddr;
				cur += (thisEndAddr - thisAddr) << 3;
			} else {
				while (thisAddr < thisEndAddr) {
					target.setByte(cur >> 3, unsafe.getByte(thisAddr++));
					cur += 8;
				}
			}
		}
		// Resolve tail
		if (cur < targetEnd) {
			b = unsafe.getByte(thisAddr);
			while (cur < targetEnd) {
				target.setBoolean(cur++, ((b >> bits) & 1) != 0);
			}
		}
	}

	@Override
	public int compareTo(UnionPrimitiveArray o) {
		if (this == o)
			return 0;
		long tbits = this.bits();
		long obits = o.bits();
		long bits = tbits > obits ? obits : tbits;
		long llen = bits >> 6;
		for (long i = 0; i < llen; i++) {
			long v1 = getLong(i);
			long v2 = o.getLong(i);
			if (v1 != v2)
				return Long.compareUnsigned(v1, v2);
		}
		long blen = bits >> 3;
		for (long i = llen << 3; i < blen; i++) {
			int v1 = getByte(i) & 0Xff;
			int v2 = o.getByte(i) & 0Xff;
			if (v1 != v2)
				return v1 > v2 ? 1 : -1;
		}
		for (long i = blen << 3; i < bits; i++) {
			boolean v1 = getBoolean(i);
			if (v1 ^ o.getBoolean(i))
				return v1 ? 1 : -1;
		}
		return tbits == obits ? 0 : (tbits > obits ? 1 : -1);
	}

	@Override
	public UnionPrimitiveArray clone() {
		long bits = this.bits;
		long bytes = (bits >> 3) + ((bits & 7) == 0 ? 0 : 1);
		UnionPrimitiveArrayDirectMemory clone = new UnionPrimitiveArrayDirectMemory(bits);
		unsafe.copyMemory(address, clone.address, bytes);
		return clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long bits = bits();
		long llen = bits >> 6;
		for (long i = 0; i < llen; i++) {
			long v = getLong(i);
			result = result * prime + (int) (v ^ (v >>> 32));
		}
		long blen = bits >> 3;
		for (long i = llen << 3; i < blen; i++) {
			result = result * prime + getByte(i) & 0Xff;
		}
		for (long i = blen << 3; i < bits; i++) {
			result = result * prime + (getBoolean(i) ? 1231 : 1237);
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UnionPrimitiveArray))
			return false;
		UnionPrimitiveArray other = (UnionPrimitiveArray) obj;
		long bits = bits();
		if (other.bits() != bits)
			return false;
		long llen = bits >> 6;
		for (long i = 0; i < llen; i++) {
			if (getLong(i) != other.getLong(i))
				return false;
		}
		long blen = bits >> 3;
		for (long i = llen << 3; i < blen; i++) {
			if (getByte(i) != other.getByte(i))
				return false;
		}
		for (long i = blen << 3; i < bits; i++) {
			if (getBoolean(i) ^ other.getBoolean(i))
				return false;
		}
		return true;
	}

	@Override
	protected void finalize() {
		free();
	}

	@Override
	public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
		long bits = this.bits;
		long addr = this.address;
		long endAddr = addr + (bits >> 3) + ((bits & 7) == 0 ? 0 : 1);
		out.writeLong(bits);
		while (addr < endAddr)
			out.write(unsafe.getByte(addr++));
	}

	@Override
	public void readExternal(java.io.ObjectInput in) throws java.io.IOException, ClassNotFoundException {
		this.bits = in.readLong();
		long bytes = (bits >> 3) + ((bits & 7) == 0 ? 0 : 1);
		this.address = unsafe.allocateMemory(bytes);
		long addr = this.address;
		long endAddr = addr + bytes;
		try {
			while (addr < endAddr)
				unsafe.putByte(addr++, in.readByte());
		} catch (Throwable e) {
			unsafe.freeMemory(address);
			address = 0;
		}
	}

	private static final long serialVersionUID = -2089822999969299715L;

	/*
	 * Methods to storage or seek values in duplicated type like float, integer,
	 * long, char. Values in bigEndian mode.
	 */

	private static void unsafePutShort(long addr, short value) {
		unsafe.putByte(addr, (byte) (value >> 8));
		unsafe.putByte(addr + 1, (byte) value);
	}

	private static void unsafePutInt(long addr, int value) {
		unsafe.putByte(addr, (byte) (value >> 24));
		unsafe.putByte(addr + 1, (byte) (value >> 16));
		unsafe.putByte(addr + 2, (byte) (value >> 8));
		unsafe.putByte(addr + 3, (byte) value);
	}

	private static void unsafePutLong(long addr, long value) {
		unsafe.putByte(addr, (byte) (value >> 56));
		unsafe.putByte(addr + 1, (byte) (value >> 48));
		unsafe.putByte(addr + 2, (byte) (value >> 40));
		unsafe.putByte(addr + 3, (byte) (value >> 32));
		unsafe.putByte(addr + 4, (byte) (value >> 24));
		unsafe.putByte(addr + 5, (byte) (value >> 16));
		unsafe.putByte(addr + 6, (byte) (value >> 8));
		unsafe.putByte(addr + 7, (byte) value);
	}

	private static void unsafePutChar(long addr, char value) {
		unsafe.putByte(addr, (byte) (value >> 8));
		unsafe.putByte(addr + 1, (byte) (value & 0Xff));
	}

	private static short unsafeGetShort(long addr) {
		short v = 0;
		v |= (unsafe.getByte(addr) & 0Xff) << 8;
		v |= unsafe.getByte(addr + 1) & 0Xff;
		return v;
	}

	private static int unsafeGetInt(long addr) {
		int v = 0;
		v |= (unsafe.getByte(addr) & 0Xff) << 24;
		v |= (unsafe.getByte(addr + 1) & 0Xff) << 16;
		v |= (unsafe.getByte(addr + 2) & 0Xff) << 8;
		v |= unsafe.getByte(addr + 3) & 0Xff;
		return v;
	}

	private static long unsafeGetLong(long addr) {
		long v = 0;
		v |= (unsafe.getByte(addr) & 0Xff) << 56;
		v |= (unsafe.getByte(addr + 1) & 0Xff) << 48;
		v |= (unsafe.getByte(addr + 2) & 0Xff) << 40;
		v |= (unsafe.getByte(addr + 3) & 0Xff) << 32;
		v |= (unsafe.getByte(addr + 4) & 0Xff) << 24;
		v |= (unsafe.getByte(addr + 5) & 0Xff) << 16;
		v |= (unsafe.getByte(addr + 6) & 0Xff) << 8;
		v |= unsafe.getByte(addr + 7) & 0Xff;
		return v;
	}

	private static char unsafeGetChar(long addr) {
		char v = 0;
		v |= (unsafe.getByte(addr) & 0Xff) << 8;
		v |= unsafe.getByte(addr + 1) & 0Xff;
		return v;
	}

}
