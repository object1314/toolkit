/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;

/**
 * Operator to manage bits of number or a byte array. Provides some methods on
 * read(get) or write(put) in some java types.
 * <p>
 * Short names: 'U'--unsigned; 'R'--reverse(bytes reversed, smallEndian).
 * 
 * @author XuYanhang
 * @since 2020-10-21
 */
public class Bits {

	/*
	 * Methods for unpacking values from byte arrays starting at given positions.
	 */

	/**
	 * Returns the bit value at specified position, required 1 byte.
	 * 
	 * @param data   source byte array data
	 * @param pos    byte position in data
	 * @param bitPos bit position in byte range from 0 to 7 where 0 specifies low
	 *               bit and 7 specifies high bit
	 * @return bit value of 0 or 1 at specified bit
	 */
	public static int getBit(byte[] data, int pos, int bitPos) {
		if (bitPos < 0 || bitPos > 7)
			throw new IllegalArgumentException();
		return (data[pos] >> bitPos) & 0X1;
	}

	/**
	 * Returns the byte value at specified position, required 1 byte.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return byte value at specified position
	 */
	public static byte getByte(byte[] data, int pos) {
		return data[pos];
	}

	/**
	 * Returns the unsigned byte value at specified position, required 1 byte.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return unsigned byte value at specified position
	 */
	public static int getUByte(byte[] data, int pos) {
		return data[pos] & 0Xff;
	}

	/**
	 * Returns the boolean value at specified position, required 1 byte.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return boolean value at specified position
	 */
	public static boolean getBool(byte[] data, int pos) {
		return (data[pos] & 0X1) != 0;
	}

	/**
	 * Returns the char value at specified position in big-endian mode, required 2
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return char value at specified position
	 */
	public static char getChar(byte[] data, int pos) {
		return (char) ((data[pos + 1] & 0Xff) | ((data[pos] & 0Xff) << 8));
	}

	/**
	 * Returns the char value at specified position in small-endian mode, required 2
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return char value at specified position
	 */
	public static char getRChar(byte[] data, int pos) {
		return (char) ((data[pos] & 0Xff) | ((data[pos + 1] & 0Xff) << 8));
	}

	/**
	 * Returns the short value at specified position in big-endian mode, required 2
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return short value at specified position
	 */
	public static short getShort(byte[] data, int pos) {
		return (short) ((data[pos + 1] & 0Xff) | ((data[pos] & 0Xff) << 8));
	}

	/**
	 * Returns the short value at specified position in small-endian mode, required
	 * 2 bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return short value at specified position
	 */
	public static short getRShort(byte[] data, int pos) {
		return (short) ((data[pos] & 0Xff) | ((data[pos + 1] & 0Xff) << 8));
	}

	/**
	 * Returns the unsigned short value at specified position in big-endian mode,
	 * required 2 bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return unsigned short value at specified position
	 */
	public static int getUShort(byte[] data, int pos) {
		return (data[pos + 1] & 0Xff) | ((data[pos] & 0Xff) << 8);
	}

	/**
	 * Returns the unsigned short value at specified position in small-endian mode,
	 * required 2 bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return unsigned short value at specified position
	 */
	public static int getRUShort(byte[] data, int pos) {
		return (data[pos] & 0Xff) | ((data[pos + 1] & 0Xff) << 8);
	}

	/**
	 * Returns the int value at specified position in big-endian mode, required 4
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return int value at specified position
	 */
	public static int getInt(byte[] data, int pos) {
		return (data[pos + 3] & 0Xff) //
				| ((data[pos + 2] & 0Xff) << 8) //
				| ((data[pos + 1] & 0Xff) << 16) //
				| ((data[pos] & 0Xff) << 24);
	}

	/**
	 * Returns the int value at specified position in small-endian mode, required 4
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return int value at specified position
	 */
	public static int getRInt(byte[] data, int pos) {
		return (data[pos] & 0Xff) //
				| ((data[pos + 1] & 0Xff) << 8) //
				| ((data[pos + 2] & 0Xff) << 16) //
				| ((data[pos + 3] & 0Xff) << 24);
	}

	/**
	 * Returns the unsigned int value at specified position in big-endian mode,
	 * required 4 bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return unsigned int value at specified position
	 */
	public static long getUInt(byte[] data, int pos) {
		return (data[pos + 3] & 0XffL) //
				| ((data[pos + 2] & 0XffL) << 8) //
				| ((data[pos + 1] & 0XffL) << 16) //
				| ((data[pos] & 0XffL) << 24);
	}

	/**
	 * Returns the unsigned int value at specified position in small-endian mode,
	 * required 4 bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return unsigned int value at specified position
	 */
	public static long getRUInt(byte[] data, int pos) {
		return (data[pos] & 0XffL) //
				| ((data[pos + 1] & 0XffL) << 8) //
				| ((data[pos + 2] & 0XffL) << 16) //
				| ((data[pos + 3] & 0XffL) << 24);
	}

	/**
	 * Returns the float value at specified position in big-endian mode, required 4
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return float value at specified position
	 */
	public static float getFloat(byte[] data, int pos) {
		return Float.intBitsToFloat(getInt(data, pos));
	}

	/**
	 * Returns the float value at specified position in small-endian mode, required
	 * 4 bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return float value at specified position
	 */
	public static float getRFloat(byte[] data, int pos) {
		return Float.intBitsToFloat(getRInt(data, pos));
	}

	/**
	 * Returns the long value at specified position in big-endian mode, required 8
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return long value at specified position
	 */
	public static long getLong(byte[] data, int pos) {
		return (data[pos + 7] & 0XffL) //
				| ((data[pos + 6] & 0XffL) << 8) //
				| ((data[pos + 5] & 0XffL) << 16) //
				| ((data[pos + 4] & 0XffL) << 24) //
				| ((data[pos + 3] & 0XffL) << 32) //
				| ((data[pos + 2] & 0XffL) << 40) //
				| ((data[pos + 1] & 0XffL) << 48) //
				| ((data[pos] & 0XffL) << 56);
	}

	/**
	 * Returns the long value at specified position in small-endian mode, required 8
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return long value at specified position
	 */
	public static long getRLong(byte[] data, int pos) {
		return (data[pos] & 0XffL) //
				| ((data[pos + 1] & 0XffL) << 8) //
				| ((data[pos + 2] & 0XffL) << 16) //
				| ((data[pos + 3] & 0XffL) << 24) //
				| ((data[pos + 4] & 0XffL) << 32) //
				| ((data[pos + 5] & 0XffL) << 40) //
				| ((data[pos + 6] & 0XffL) << 48) //
				| ((data[pos + 7] & 0XffL) << 56);
	}

	/**
	 * Returns the double value at specified position in big-endian mode, required 8
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return double value at specified position
	 */
	public static double getDouble(byte[] data, int pos) {
		return Double.longBitsToDouble(getLong(data, pos));
	}

	/**
	 * Returns the double value at specified position in small-endian mode, required
	 * 8 bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return double value at specified position
	 */
	public static double getRDouble(byte[] data, int pos) {
		return Double.longBitsToDouble(getRLong(data, pos));
	}

	/**
	 * Skip some bytes on check it where negative value is also allowed.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param len  byte size to skip in data
	 * @return the <code>len</code>
	 */
	public static int skip(byte[] data, int pos, int len) {
		int end = pos + len;
		if (pos < 0 || pos > data.length || end < 0 || end > data.length)
			throw new IndexOutOfBoundsException();
		return len;
	}

	/**
	 * Returns the integer value at specified position in big-endian mode, required
	 * <code>size</code> byte.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param size byte size to get in data
	 * @return integer value at specified position
	 */
	public static int getInt(byte[] data, int pos, int size) {
		int end = pos + size;
		if (pos < 0 || end > data.length || end < pos)
			throw new IndexOutOfBoundsException();
		int v = 0;
		while (pos != end) {
			v <<= 8;
			v |= data[pos++] & 0Xff;
		}
		return v;
	}

	/**
	 * Returns the integer value at specified position in small-endian mode,
	 * required <code>size</code> byte.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param size byte size to get in data
	 * @return integer value at specified position
	 */
	public static int getRInt(byte[] data, int pos, int size) {
		int end = pos + size;
		if (pos < 0 || end > data.length || end < pos)
			throw new IndexOutOfBoundsException();
		int v = 0;
		while (end != pos) {
			v <<= 8;
			v |= data[--end] & 0Xff;
		}
		return v;
	}

	/**
	 * Returns the long integer value at specified position in big-endian mode,
	 * required <code>size</code> byte.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param size byte size to get in data
	 * @return long integer value at specified position
	 */
	public static long getLong(byte[] data, int pos, int size) {
		int end = pos + size;
		if (pos < 0 || end > data.length || end < pos)
			throw new IndexOutOfBoundsException();
		long v = 0;
		while (pos != end) {
			v <<= 8;
			v |= data[pos++] & 0XffL;
		}
		return v;
	}

	/**
	 * Returns the long integer value at specified position in small-endian mode,
	 * required <code>size</code> byte.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param size byte size to get in data
	 * @return long integer value at specified position
	 */
	public static long getRLong(byte[] data, int pos, int size) {
		int end = pos + size;
		if (pos < 0 || end > data.length || end < pos)
			throw new IndexOutOfBoundsException();
		long v = 0;
		while (end != pos) {
			v <<= 8;
			v |= data[--end] & 0XffL;
		}
		return v;
	}

	/**
	 * Returns the bytes value at specified position, required <code>len</code>
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param len  data to get
	 * @return the bytes
	 */
	public static byte[] getBytes(byte[] data, int pos, int len) {
		byte[] val = new byte[len];
		System.arraycopy(data, pos, val, 0, len);
		return val;
	}

	/**
	 * Returns the UTF-8 string value at specified position, required
	 * <code>len</code> bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param len  data to get
	 * @return the string value
	 * @throws IllegalStateException if fail decode the bytes as UTF-8 string
	 */
	public static String getUTF(byte[] data, int pos, int len) {
		int end = pos + len;
		if (pos < 0 || end > data.length || pos > end)
			throw new IndexOutOfBoundsException();
		StringBuilder sb = new StringBuilder(len >> 1);
		int c1, c2, c3;
		while (pos < end) {
			c1 = data[pos] & 0Xff;
			if ((c1 & 0X80) == 0) {
				/* 0xxx xxxx */
				pos++;
				sb.append((char) c1);
			} else if ((c1 & 0Xe0) == 0Xc0) {
				/* 110x xxxx 10xx xxxx */
				pos += 2;
				if (pos > end)
					throw new IllegalStateException("malformed utf");
				c2 = (int) data[pos - 1];
				if ((c2 & 0Xc0) != 0x80)
					throw new IllegalStateException("malformed utf");
				sb.append((char) (((c1 & 0X1f) << 6) | (c2 & 0X3f)));
			} else if ((c1 & 0Xf0) == 0Xe0) {
				/* 1110 xxxx 10xx xxxx 10xx xxxx */
				pos += 3;
				if (pos > end)
					throw new IllegalStateException("malformed utf");
				c2 = data[pos - 2] & 0Xff;
				c3 = data[pos - 1] & 0Xff;
				if (((c2 & 0Xc0) != 0X80) || ((c3 & 0Xc0) != 0X80))
					throw new IllegalStateException("malformed utf");
				sb.append((char) (((c1 & 0X0f) << 12) | ((c2 & 0X3f) << 6) | ((c3 & 0X3f) << 0)));
			} else {
				/* 10xx xxxx, 1111 xxxx */
				throw new IllegalStateException("malformed utf");
			}
		}
		return sb.toString();
	}

	/**
	 * Returns the object value at specified position, required serial data's length
	 * bytes.
	 * 
	 * @see ObjectInputStream#readObject()
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @return the object value
	 * @throws IllegalStateException if fail to get the object on IOException or
	 *                               ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObject(byte[] data, int pos) {
		Object o;
		ByteArrayInputStream bais = new ByteArrayInputStream(data, pos, data.length - pos);
		try (ObjectInputStream ois = new ObjectInputStream(bais)) {
			o = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		return (T) o;
	}

	/**
	 * Returns the objects value at specified position, required serial data's
	 * length bytes.
	 * 
	 * @see ObjectInputStream#readObject()
	 * @param data  source byte array data
	 * @param pos   byte position in data
	 * @param count objects count
	 * @return the objects value in array
	 * @throws IllegalStateException if fail to get the object on IOException or
	 *                               ClassNotFoundException
	 */
	public static Object[] getObjects(byte[] data, int pos, int count) {
		Object[] os = new Object[count];
		ByteArrayInputStream bais = new ByteArrayInputStream(data, pos, data.length - pos);
		try (ObjectInputStream ois = new ObjectInputStream(bais)) {
			for (int i = 0; i < count; i++)
				os[i] = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		return os;
	}

	/**
	 * Returns the objects value at specified position, required <code>len</code>
	 * bytes.
	 * 
	 * @see ObjectInputStream#readObject()
	 * @param data     source byte array data
	 * @param pos      byte position in data
	 * @param consumer consumer to cost all objects
	 * @param count    objects count
	 * @return the objects value in array
	 * @throws IllegalStateException if fail to get the object on IOException or
	 *                               ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> void getObjects(byte[] data, int pos, Consumer<T> consumer, int count) {
		if (null == consumer)
			throw new NullPointerException();
		ByteArrayInputStream bais = new ByteArrayInputStream(data, pos, data.length - pos);
		try (ObjectInputStream ois = new ObjectInputStream(bais)) {
			for (int i = 0; i < count; i++)
				consumer.accept((T) ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	/*
	 * Methods for packing values into byte arrays starting at given positions.
	 */

	/**
	 * Put the bit value at specified position, required 1 byte.
	 * 
	 * @param data   source byte array data
	 * @param pos    byte position in data
	 * @param bitPos bit position in byte range from 0 to 7 where 0 specifies low
	 *               bit and 7 specifies high bit
	 * @param bit    bit value of 0 or 1 to put at specified bit
	 */
	public static void putBit(byte[] data, int pos, int bitPos, int bit) {
		if (bitPos < 0 || bitPos > 7)
			throw new IllegalArgumentException();
		if ((bit & 0X1) == 0)
			data[pos] &= (~(1 << bitPos));
		else
			data[pos] |= (1 << bitPos);
	}

	/**
	 * Put the byte value at specified position, required 1 byte.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param b    byte value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putByte(byte[] data, int pos, int b) {
		data[pos] = (byte) b;
		return 1;
	}

	/**
	 * Put the byte value at specified position, required 1 byte.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param b    boolean value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putBool(byte[] data, int pos, boolean b) {
		data[pos] = (byte) (b ? 1 : 0);
		return 1;
	}

	/**
	 * Put the char value at specified position in big-endian mode, required 2
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param c    char value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putChar(byte[] data, int pos, int c) {
		data[pos] = (byte) (c >>> 8);
		data[pos + 1] = (byte) c;
		return 2;
	}

	/**
	 * Put the char value at specified position in small-endian mode, required 2
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param c    char value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putRChar(byte[] data, int pos, int c) {
		data[pos] = (byte) c;
		data[pos + 1] = (byte) (c >>> 8);
		return 2;
	}

	/**
	 * Put the short value at specified position in big-endian mode, required 2
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  short value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putShort(byte[] data, int pos, int val) {
		data[pos] = (byte) (val >>> 8);
		data[pos + 1] = (byte) val;
		return 2;
	}

	/**
	 * Put the short value at specified position in small-endian mode, required 2
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  short value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putRShort(byte[] data, int pos, int val) {
		data[pos] = (byte) val;
		data[pos + 1] = (byte) (val >>> 8);
		return 2;
	}

	/**
	 * Put the int value at specified position in big-endian mode, required 4 bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  int value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putInt(byte[] data, int pos, int val) {
		data[pos] = (byte) (val >>> 24);
		data[pos + 1] = (byte) (val >>> 16);
		data[pos + 2] = (byte) (val >>> 8);
		data[pos + 3] = (byte) val;
		return pos;
	}

	/**
	 * Put the int value at specified position in small-endian mode, required 4
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  int value to put at specified position
	 * @return new position on next write
	 */
	public static int putRInt(byte[] data, int pos, int val) {
		data[pos] = (byte) val;
		data[pos + 1] = (byte) (val >>> 8);
		data[pos + 2] = (byte) (val >>> 16);
		data[pos + 3] = (byte) (val >>> 24);
		return 4;
	}

	/**
	 * Put the float value at specified position in big-endian mode, required 4
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  float value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putFloat(byte[] data, int pos, float val) {
		return putInt(data, pos, Float.floatToIntBits(val));
	}

	/**
	 * Put the float value at specified position in small-endian mode, required 4
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  float value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putRFloat(byte[] data, int pos, float val) {
		return putRInt(data, pos, Float.floatToIntBits(val));
	}

	/**
	 * Put the long value at specified position in big-endian mode, required 8
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  long value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putLong(byte[] data, int pos, long val) {
		data[pos] = (byte) (val >>> 56);
		data[pos + 1] = (byte) (val >>> 48);
		data[pos + 2] = (byte) (val >>> 40);
		data[pos + 3] = (byte) (val >>> 32);
		data[pos + 4] = (byte) (val >>> 24);
		data[pos + 5] = (byte) (val >>> 16);
		data[pos + 6] = (byte) (val >>> 8);
		data[pos + 7] = (byte) val;
		return 8;
	}

	/**
	 * Put the long value at specified position in small-endian mode, required 8
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  long value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putRLong(byte[] data, int pos, long val) {
		data[pos] = (byte) val;
		data[pos + 1] = (byte) (val >>> 8);
		data[pos + 2] = (byte) (val >>> 16);
		data[pos + 3] = (byte) (val >>> 24);
		data[pos + 4] = (byte) (val >>> 32);
		data[pos + 5] = (byte) (val >>> 40);
		data[pos + 6] = (byte) (val >>> 48);
		data[pos + 7] = (byte) (val >>> 56);
		return 8;
	}

	/**
	 * Put the double value at specified position in big-endian mode, required 8
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  double value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putDouble(byte[] data, int pos, double val) {
		return putLong(data, pos, Double.doubleToLongBits(val));
	}

	/**
	 * Put the double value at specified position in small-endian mode, required 8
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  double value to put at specified position
	 * @return bytes count on put into this data
	 */
	public static int putRDouble(byte[] data, int pos, double val) {
		return putRLong(data, pos, Double.doubleToLongBits(val));
	}

	/**
	 * Fill the data in a specified byte value, required <code>size</code> bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param b    byte value to fill
	 * @param size bytes size to fill
	 * @return the <code>size</code>
	 */
	public static int fill(byte[] data, int pos, int b, int size) {
		int end = pos + size;
		if (pos < 0 || end > data.length || pos > end)
			throw new IndexOutOfBoundsException();
		for (int i = pos; i < end; i++)
			data[i] = (byte) b;
		return size;
	}

	/**
	 * Put the integer value at specified position in big-endian mode, required
	 * <code>size</code> bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  integer value to put at specified position
	 * @param size the byte size to write this integer value
	 * @return bytes count on put into this data
	 */
	public static int putInt(byte[] data, int pos, int val, int size) {
		int end = pos + size;
		if (pos < 0 || end > data.length || end < pos)
			throw new IndexOutOfBoundsException();
		while (end != pos) {
			data[--end] = (byte) val;
			val >>>= 8;
		}
		return size;
	}

	/**
	 * Put the integer value at specified position in small-endian mode, required
	 * <code>size</code> bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  integer value to put at specified position
	 * @param size the byte size to write this integer value
	 * @return bytes count on put into this data
	 */
	public static int putRInt(byte[] data, int pos, int val, int size) {
		int end = pos + size;
		if (pos < 0 || end > data.length || end < pos)
			throw new IndexOutOfBoundsException();
		while (pos != end) {
			data[pos++] = (byte) val;
			val >>>= 8;
		}
		return size;
	}

	/**
	 * Put the long integer value at specified position in big-endian mode, required
	 * <code>size</code> bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  long integer value to put at specified position
	 * @param size the byte size to write this long integer value
	 * @return bytes count on put into this data
	 */
	public static int putLong(byte[] data, int pos, long val, int size) {
		int end = pos + size;
		if (pos < 0 || end > data.length || end < pos)
			throw new IndexOutOfBoundsException();
		while (end != pos) {
			data[--end] = (byte) val;
			val >>>= 8;
		}
		return size;
	}

	/**
	 * Put the long integer value at specified position in small-endian mode,
	 * required <code>size</code> bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param val  long integer value to put at specified position
	 * @param size the byte size to write this long integer value
	 * @return bytes count on put into this data
	 */
	public static int putRLong(byte[] data, int pos, long val, int size) {
		int end = pos + size;
		if (pos < 0 || end > data.length || end < pos)
			throw new IndexOutOfBoundsException();
		while (pos != end) {
			data[pos++] = (byte) val;
			val >>>= 8;
		}
		return size;
	}

	/**
	 * Put a bytes into the data at specified position, required
	 * <code>val.length</code> bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param bs   bytes value to write
	 * @return bytes count on put into this data
	 */
	public static int putBytes(byte[] data, int pos, byte[] bs) {
		System.arraycopy(bs, 0, data, pos, bs.length);
		return bs.length;
	}

	/**
	 * Put a bytes into the data at specified position, required <code>len</code>
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param bs   bytes value to write
	 * @param off  bytes value to write offset
	 * @param len  bytes value to write length
	 * @return bytes count on put into this data
	 */
	public static int putBytes(byte[] data, int pos, byte[] bs, int off, int len) {
		System.arraycopy(bs, off, data, pos, len);
		return len;
	}

	/**
	 * Put a char sequence at specified position on UTF-8 encoding, required
	 * {@link #measureUTFBytes(CharSequence)} bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param cs   char sequence value to write
	 * @return bytes count on put into this data
	 */
	public static int putUTF(byte[] data, int pos, CharSequence cs) {
		return putUTF(data, pos, cs, 0, cs.length());
	}

	/**
	 * Put a char sequence at specified position on UTF-8 encoding, required
	 * {@link #measureUTFBytes(CharSequence, int, int)} bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param cs   char sequence value to write
	 * @param coff offset in char sequence to write
	 * @param clen length of char sequence to write
	 * @return bytes count on put into this data
	 */
	public static int putUTF(byte[] data, int pos, CharSequence cs, int coff, int clen) {
		int cend = coff + clen;
		if (coff < 0 || cend > cs.length() || coff > cend)
			throw new IndexOutOfBoundsException();
		/* use charAt instead of copying String to char array */
		int p = pos;
		char c;
		for (int i = coff; i < cend; i++) {
			c = cs.charAt(i);
			if (c >= 0X0001 && c <= 0X007f) {
				data[p++] = (byte) c;
			} else if (c > 0X07ff) {
				data[p++] = (byte) (0Xe0 | ((c >> 12) & 0X0f));
				data[p++] = (byte) (0X80 | ((c >> 6) & 0X3f));
				data[p++] = (byte) (0X80 | ((c >> 0) & 0X3f));
			} else {
				data[p++] = (byte) (0Xc0 | ((c >> 6) & 0X1f));
				data[p++] = (byte) (0X80 | ((c >> 0) & 0X3f));
			}
		}
		return p - pos;
	}

	/**
	 * Put an object value at specified position, required the serial data size
	 * bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param obj  object value to write
	 * @return bytes count on put into this data
	 * @throws IllegalStateException if the data space not enough or the any
	 *                               IOException on serial the object value
	 */
	public static int putObject(byte[] data, int pos, Object obj) {
		SpecifyByteArrayOutputStream baos = new SpecifyByteArrayOutputStream(data, pos, data.length - pos);
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(obj);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return baos.tell() - pos;
	}

	/**
	 * Put some objects value at specified position, required the serial data size
	 * bytes. Only one object serial head write in the data.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param objs objects value to write
	 * @return bytes count on put into this data
	 * @throws IllegalStateException if the data space not enough or the any
	 *                               IOException on serial the objects value
	 */
	public static int putObjects(byte[] data, int pos, Object[] objs) {
		int len = objs.length;
		SpecifyByteArrayOutputStream baos = new SpecifyByteArrayOutputStream(data, pos, data.length - pos);
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			for (int i = 0; i < len; i++)
				oos.writeObject(objs[i]);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return baos.tell();
	}

	/**
	 * Put some objects value at specified position, required the serial data size
	 * bytes. Only one object serial head write in the data.
	 * 
	 * @param data source byte array data
	 * @param pos  byte position in data
	 * @param objs objects value to write
	 * @return bytes count on put into this data
	 * @throws IllegalStateException if the data space not enough or the any
	 *                               IOException on serial the objects value
	 */
	public static int putObjects(byte[] data, int pos, Iterable<?> objs) {
		java.util.Iterator<?> ite = objs.iterator();
		SpecifyByteArrayOutputStream baos = new SpecifyByteArrayOutputStream(data, pos, data.length - pos);
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			while (ite.hasNext())
				oos.writeObject(ite.next());
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return baos.tell();
	}

	/*
	 * Methods to help convert data
	 */

	/**
	 * Returns the created bytes in a list of bytes in integer values where only the
	 * lower 8 bits used.
	 * 
	 * @param bs source bytes in integer formats
	 * @return created bytes
	 */
	public static byte[] newBytes(int... bs) {
		int len = bs.length;
		byte[] data = new byte[len];
		for (int i = 0; i < len; i++)
			data[i] = (byte) bs[i];
		return data;
	}

	/**
	 * Convert a list of bits as byte array. The bits value loaded only on lower bit
	 * position where <code>bit=bit&0X1</code>. If convert the bytes as integer or
	 * long etc., then load in in a bits reversed way.
	 * <p>
	 * Example, the bits of 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 will cause a two length
	 * bytes of 10111011, 10000000 where the order is on from higher to lower.
	 *
	 * @param bits bits value in 0 or 1
	 * @return the bytes on the bits
	 */
	public static byte[] makeBits(int... bits) {
		int bil = bits.length;
		int byl = (bil >> 3) + ((bil & 7) == 0 ? 0 : 1);
		byte[] bytes = new byte[byl];
		for (int i = 0, j = 0; j < bil; i++)
			for (int t = 7; t > -1 && j < bil; t--, j++)
				bytes[i] |= (bits[j] & 0X1) << t;
		return bytes;
	}

	/**
	 * Convert a list of bits as a byte. The bits value loaded only on lower bit
	 * position where <code>bit=bit&0X1</code>. Most 8 bits fetched here.
	 * <p>
	 * Different with {@link #makeBits(int...)} the order is on from lower to
	 * higher.
	 *
	 * @param bits bits value in 0 or 1
	 * @return the byte on the bits
	 */
	public static byte makeBitsAsByte(int... bits) {
		int bil = bits.length > 8 ? 8 : bits.length;
		byte val = 0;
		for (int i = 0; i < bil; i++)
			val |= (bits[i] & 0X1) << i;
		return val;
	}

	/**
	 * Convert a list of bits as a short. The bits value loaded only on lower bit
	 * position where <code>bit=bit&0X1</code>. Most 16 bits fetched here.
	 * <p>
	 * Different with {@link #makeBits(int...)} the order is on from lower to
	 * higher.
	 *
	 * @param bits bits value in 0 or 1
	 * @return the short on the bits
	 */
	public static short makeBitsAsShort(int... bits) {
		int bil = bits.length > 16 ? 16 : bits.length;
		short val = 0;
		for (int i = 0; i < bil; i++)
			val |= (bits[i] & 0X1) << i;
		return val;
	}

	/**
	 * Convert a list of bits as a integer. The bits value loaded only on lower bit
	 * position where <code>bit=bit&0X1</code>. Most 32 bits fetched here.
	 * <p>
	 * Different with {@link #makeBits(int...)} the order is on from lower to
	 * higher.
	 *
	 * @param bits bits value in 0 or 1
	 * @return the integer on the bits
	 */
	public static int makeBitsAsInt(int... bits) {
		int bil = bits.length > 32 ? 32 : bits.length;
		int val = 0;
		for (int i = 0; i < bil; i++)
			val |= (bits[i] & 0X1) << i;
		return val;
	}

	/**
	 * Convert a list of bits as a long integer. The bits value loaded only on lower
	 * bit position where <code>bit=bit&0X1</code>. Most 64 bits fetched here.
	 * <p>
	 * Different with {@link #makeBits(int...)} the order is on from lower to
	 * higher.
	 *
	 * @param bits bits value in 0 or 1
	 * @return the long integer on the bits
	 */
	public static long makeBitsAsLong(int... bits) {
		int bil = bits.length > 64 ? 64 : bits.length;
		long val = 0;
		for (int i = 0; i < bil; i++)
			val |= (bits[i] & 0X1L) << i;
		return val;
	}

	/**
	 * Returns the bits in string of a byte array as bits string like
	 * "0110001111110000" from value of 01100011, 11110000 on order from higher to
	 * lower.
	 * 
	 * @param data    source data to load bits.
	 * @param bitSize the size of the bits
	 * @return the bits in string of '0' and '1', split by ' ' each 8 bits
	 */
	public static String dumpBits(byte[] data, int bitSize) {
		if (bitSize < 0)
			throw new IllegalArgumentException();
		if (0 == bitSize)
			return "";
		int byteSize = (bitSize >> 3) + ((bitSize & 0X7) == 0 ? 0 : 1);
		int charSize = bitSize + byteSize - 1;
		char[] bits = new char[charSize];
		for (int i = 0, j = 0; i < byteSize; i++) {
			byte v = i < data.length ? data[i] : 0;
			for (int t = 7; t > -1 && j < charSize; t--, j++)
				bits[j] = ((v >>> t) & 0X1) == 0 ? '0' : '1';
			if (j < charSize)
				bits[j++] = ' ';
		}
		return new String(bits);
	}

	/**
	 * Returns the bits in string of an integer value as bits string like "01100011"
	 * from value of 01100011 on order from higher to lower.
	 * 
	 * @param val     source value to load bits.
	 * @param bitSize the size of the bits
	 * @return the bits in string of '0' and '1', split by ' ' each 8 bits
	 */
	public static String dumpValBits(int val, int bitSize) {
		if (bitSize < 0)
			throw new IllegalArgumentException();
		if (0 == bitSize)
			return "";
		int charSize = bitSize + (bitSize >> 3) - ((bitSize & 0X7) == 0 ? 1 : 0);
		char[] bits = new char[charSize];
		for (int i = bitSize - 1, j = 0, c = 0; i > -1; i--) {
			if (i > 31)
				bits[j++] = '0';
			else
				bits[j++] = ((val >>> i) & 0X1) == 0 ? '0' : '1';
			if ((++c & 0X7) == 0 && j < charSize)
				bits[j++] = ' ';
		}
		return new String(bits);
	}

	/**
	 * Returns the bits in string of a long integer value as bits string like
	 * "01100011" from value of 01100011 on order from higher to lower.
	 * 
	 * @param val     source value to load bits.
	 * @param bitSize the size of the bits
	 * @return the bits in string of '0' and '1', split by ' ' each 8 bits
	 */
	public static String dumpValBits(long val, int bitSize) {
		if (bitSize < 0)
			throw new IllegalArgumentException();
		if (0 == bitSize)
			return "";
		int charSize = bitSize + (bitSize >> 3) - ((bitSize & 0X7) == 0 ? 1 : 0);
		char[] bits = new char[charSize];
		for (int i = bitSize - 1, j = 0, c = 0; i > -1; i--) {
			if (i > 63)
				bits[j++] = '0';
			else
				bits[j++] = (((int) (val >>> i)) & 0X1) == 0 ? '0' : '1';
			if ((++c & 0X7) == 0 && j < charSize)
				bits[j++] = ' ';
		}
		return new String(bits);
	}

	/**
	 * Returns the bit value at specified bit position.
	 * 
	 * @param src    source value
	 * @param bitPos bit position in the source value
	 * @return bit value of 0 or 1 at specified bit
	 */
	public static int getBit(int src, int bitPos) {
		if (bitPos < 0 || bitPos > 31)
			return 0;
		return (src >>> bitPos) & 0X1;
	}

	/**
	 * Returns the bit value at specified bit position.
	 * 
	 * @param src    source value
	 * @param bitPos bit position in the source value
	 * @return bit value of 0 or 1 at specified bit
	 */
	public static int getBit(long src, int bitPos) {
		if (bitPos < 0 || bitPos > 63)
			return 0;
		return (int) ((src >>> bitPos) & 0X1);
	}

	/**
	 * Returns the target value on bit set at specified position for source value.
	 * 
	 * @param src    source value
	 * @param bitPos bit position in the source value
	 * @param bit    bit of 1 or 0 to set the bit
	 * @return target value
	 */
	public static byte setBit(byte src, int bitPos, int bit) {
		if (bitPos < 0 || bitPos > 7)
			return src;
		if ((bit & 1) == 0)
			src &= (~(1 << bitPos));
		else
			src |= (1 << bitPos);
		return src;
	}

	/**
	 * Returns the target value on bit set at specified position for source value.
	 * 
	 * @param src    source value
	 * @param bitPos bit position in the source value
	 * @param bit    bit of 1 or 0 to set the bit
	 * @return target value
	 */
	public static int setBit(int src, int bitPos, int bit) {
		if (bitPos < 0 || bitPos > 31)
			return src;
		if ((bit & 1) == 0)
			src &= (~(1 << bitPos));
		else
			src |= (1 << bitPos);
		return src;
	}

	/**
	 * Returns the target value on bit set at specified position for source value.
	 * 
	 * @param src    source value
	 * @param bitPos bit position in the source value
	 * @param bit    bit of 1 or 0 to set the bit
	 * @return target value
	 */
	public static long setBit(long src, int bitPos, int bit) {
		if (bitPos < 0 || bitPos > 53)
			return src;
		if ((bit & 1) == 0)
			src &= (~(1L << bitPos));
		else
			src |= (1L << bitPos);
		return src;
	}

	/**
	 * Reverse the bits order in specified range of a bytes, required
	 * <code>len</code> bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  start position
	 * @param len  length to adjust
	 */
	public static void reverseBits(byte[] data, int pos, int len) {
		reverseBytes(data, pos, len);
		for (int i = pos + len - 1; i >= pos; i--)
			data[i] = reverseByteBits(data[i]);
	}

	/**
	 * Returns a bits order reversed byte value, on 1 bytes, 8 bits.
	 *
	 * @param val the origin value
	 * @return reversed value
	 */
	public static byte reverseByteBits(byte val) {
		int i = val & 0Xff;
		i = (i & 0X55) << 1 | (i >>> 1) & 0X55;
		i = (i & 0X33) << 2 | (i >>> 2) & 0X33;
		i = (i & 0X0f) << 4 | (i >>> 4) & 0X0f;
		return (byte) i;
	}

	/**
	 * Returns a bits order reversed short value, on 2 bytes, 16 bits.
	 *
	 * @param val the origin value
	 * @return reversed value
	 */
	public static short reverseShortBits(short val) {
		int i = val & 0Xffff;
		i = (i & 0X5555) << 1 | (i >>> 1) & 0X5555;
		i = (i & 0X3333) << 2 | (i >>> 2) & 0X3333;
		i = (i & 0X0f0f) << 4 | (i >>> 4) & 0X0f0f;
		i = (i & 0X00ff) << 8 | (i >>> 8) & 0X00ff;
		return (short) i;
	}

	/**
	 * Returns a bits order reversed int value, on 4 bytes, 32 bits.
	 * 
	 * @see Integer#reverse(int)
	 * @param val the origin value
	 * @return reversed value
	 */
	public static int reverseIntBits(int val) {
		val = (val & 0X55555555) << 1 | (val >>> 1) & 0X55555555;
		val = (val & 0X33333333) << 2 | (val >>> 2) & 0X33333333;
		val = (val & 0X0f0f0f0f) << 4 | (val >>> 4) & 0X0f0f0f0f;
		val = (val & 0X00ff00ff) << 8 | (val >>> 8) & 0X00ff00ff;
		return (val & 0Xffff) << 16 | (val >>> 16) & 0Xffff;
	}

	/**
	 * Returns a bits order reversed long value, on 8 bytes, 64 bits.
	 * 
	 * @see Long#reverse(long)
	 * @param val the origin value
	 * @return reversed value
	 */
	public static long reverseLongBits(long val) {
		val = (val & 0X5555555555555555L) << 1 | (val >>> 1) & 0X5555555555555555L;
		val = (val & 0X3333333333333333L) << 2 | (val >>> 2) & 0X3333333333333333L;
		val = (val & 0X0f0f0f0f0f0f0f0fL) << 4 | (val >>> 4) & 0X0f0f0f0f0f0f0f0fL;
		val = (val & 0X00ff00ff00ff00ffL) << 8 | (val >>> 8) & 0X00ff00ff00ff00ffL;
		return (val << 48) | ((val & 0Xffff0000L) << 16) | ((val >>> 16) & 0Xffff0000L) | (val >>> 48);
	}

	/**
	 * Returns a bits order reversed integer value on specified bits, on most 32
	 * bits.
	 * 
	 * @param val     the origin value
	 * @param bitSize the bits size of this value
	 * @return reversed value
	 */
	public static int reverseValBits(int val, int bitSize) {
		if (bitSize < 0)
			throw new IllegalArgumentException();
		if (bitSize == 0 || bitSize > 63)
			return 0;
		if ((bitSize & 0X7) == 0) {
			val = reverseValBytes(val, bitSize >> 3);
			val = (val & 0X55555555) << 1 | (val >>> 1) & 0X55555555;
			val = (val & 0X33333333) << 2 | (val >>> 2) & 0X33333333;
			val = (val & 0X0f0f0f0f) << 4 | (val >>> 4) & 0X0f0f0f0f;
			return val;
		}
		int result = 0;
		for (int i = 0, j = bitSize - 1; i < j; i++, j--) {
			if (j < 32)
				result |= (val << (j - i)) & (0X1 << j);
			if (j - i < 32)
				result |= (val >> (j - i)) & (0X1 << i);
		}
		return result;
	}

	/**
	 * Returns a bits order reversed long value, on most 64 bits.
	 * 
	 * @param val     the origin value
	 * @param bitSize the bits size of this value
	 * @return reversed value
	 */
	public static long reverseValBits(long val, int bitSize) {
		if (bitSize < 0)
			throw new IllegalArgumentException();
		if (bitSize == 0 || bitSize > 127)
			return 0L;
		if ((bitSize & 0X7) == 0) {
			val = reverseValBytes(val, bitSize >> 3);
			val = (val & 0X5555555555555555L) << 1 | (val >>> 1) & 0X5555555555555555L;
			val = (val & 0X3333333333333333L) << 2 | (val >>> 2) & 0X3333333333333333L;
			val = (val & 0X0f0f0f0f0f0f0f0fL) << 4 | (val >>> 4) & 0X0f0f0f0f0f0f0f0fL;
			return val;
		}
		long result = 0;
		for (int i = 0, j = bitSize - 1; i < j; i++, j--) {
			if (j < 64)
				result |= (val << (j - i)) & (0X1L << j);
			if (j - i < 64)
				result |= (val >> (j - i)) & (0X1L << i);
		}
		return result;
	}

	/**
	 * Reverse the bytes order in specified range of a bytes, required
	 * <code>len</code> bytes.
	 * 
	 * @param data source byte array data
	 * @param pos  start position
	 * @param len  length to adjust
	 */
	public static void reverseBytes(byte[] data, int pos, int len) {
		int end = pos + len;
		if (pos < 0 || end > data.length || pos > end)
			throw new IndexOutOfBoundsException();
		for (int i = pos, j = end - 1; i < j; i++, j--) {
			byte tmp = data[i];
			data[i] = data[j];
			data[j] = tmp;
		}
	}

	/**
	 * Returns a bytes order reversed short value, on 2 bytes.
	 *
	 * @see Short#reverseBytes(short)
	 * @param val the origin value
	 * @return reversed value
	 */
	public static short reverseShortBytes(short val) {
		return (short) ((val << 8) | ((val & 0Xff00) >>> 8));
	}

	/**
	 * Returns a bytes order reversed int value, on 4 bytes.
	 * 
	 * @see Integer#reverseBytes(int)
	 * @param val the origin value
	 * @return reversed value
	 */
	public static int reverseIntBytes(int val) {
		return (val << 24) //
				| ((val << 8) & 0X00ff0000) //
				| ((val >>> 8) & 0X0000ff00) //
				| (val >>> 24);
	}

	/**
	 * Returns a bytes order reversed long value, on 8 bytes.
	 * 
	 * @see Long#reverseBytes(long)
	 * @param val the origin value
	 * @return reversed value
	 */
	public static long reverseLongBytes(long val) {
		return (val << 56) //
				| ((val << 40) & 0X00ff000000000000L) //
				| ((val << 24) & 0X0000ff0000000000L) //
				| ((val << 8) & 0X000000ff00000000L) //
				| ((val >>> 8) & 0X00000000ff000000L) //
				| ((val >>> 24) & 0X0000000000ff0000L) //
				| ((val >>> 40) & 0X000000000000ff00L) //
				| (val >>> 56);
	}

	/**
	 * Returns a bytes order reversed integer value in specified bytes size, on most
	 * 4 bytes.
	 * 
	 * @param val  the origin value
	 * @param size the bytes size of this value
	 * @return reversed value
	 */
	public static int reverseValBytes(int val, int size) {
		if (size < 0)
			throw new IllegalArgumentException();
		if (size > 7)
			return 0;
		switch (size) {
		case 0:
			return 0;
		case 1:
			return val & 0Xff;
		case 2:
			return ((val & 0Xff) << 8) | ((val & 0Xff00) >>> 8);
		case 4:
			return reverseIntBytes(val);
		}
		int result = 0;
		for (int i = 0, j = (size - 1) << 3; i < j; i += 8, j -= 8) {
			if (j < 32)
				result |= (val << (j - i)) & (0Xff << j);
			if (j - i < 32)
				result |= (val >> (j - i)) & (0Xff << i);
		}
		return result;
	}

	/**
	 * Returns a bytes order reversed long integer value in specified bytes size, on
	 * most 8 bytes.
	 * 
	 * @param val  the origin value
	 * @param size the bytes size of this value
	 * @return reversed value
	 */
	public static long reverseValBytes(long val, int size) {
		if (size < 0)
			throw new IllegalArgumentException();
		if (size > 15)
			return 0L;
		switch (size) {
		case 0:
			return 0;
		case 1:
			return val & 0XffL;
		case 2:
			return ((val & 0XffL) << 8) | ((val & 0Xff00L) >>> 8);
		case 4:
			return reverseIntBytes((int) val) & 0XffffffffL;
		case 8:
			return reverseLongBytes(val);
		}
		long result = 0;
		for (int i = 0, j = (size - 1) << 3; i < j; i += 8, j -= 8) {
			if (j < 64)
				result |= (val << (j - i)) & (0XffL << j);
			if (j - i < 64)
				result |= (val >> (j - i)) & (0XffL << i);
		}
		return result;
	}

	/**
	 * Returns the length of a char sequence when convert it as bytes on UTF-8.
	 * 
	 * @param cs char sequence to measure
	 * @return measured bytes length of this char sequence on UTF-8
	 */
	public static int measureUTFBytes(CharSequence cs) {
		return measureUTFBytes(cs, 0, cs.length());
	}

	/**
	 * Returns the length of a char sequence when convert it as bytes on UTF-8.
	 * 
	 * @param cs  char sequence to measure
	 * @param off char sequence offset to measure
	 * @param len char sequence length to measure
	 * @return measured bytes length of this char sequence on UTF-8
	 */
	public static int measureUTFBytes(CharSequence cs, int off, int len) {
		int end = off + len;
		if (off < 0 || end > cs.length() || off > end)
			throw new IndexOutOfBoundsException();
		/* use charAt instead of copying String to char array */
		int utflen = 0;
		int c;
		for (int i = off; i < end; i++) {
			c = cs.charAt(i);
			if ((c >= 0X0001) && (c <= 0X007f))
				utflen++;
			else if (c > 0X07ff)
				utflen += 3;
			else
				utflen += 2;
		}
		return utflen;
	}

	/**
	 * Encode an object as bytes on JDK serialization.
	 * 
	 * @param obj object to serialize encode
	 * @return bytes of the object
	 * @throws IllegalStateException if serial happens any IOException
	 */
	public static byte[] serialObject(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(obj);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return baos.toByteArray();
	}

	/**
	 * Encode some objects as bytes on JDK serialization.
	 * 
	 * @param objs objects to serialize encode
	 * @return bytes of the objects
	 * @throws IllegalStateException if serial happens any IOException
	 */
	public static byte[] serialObjects(Object[] objs) {
		int count = objs.length;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			for (int i = 0; i < count; i++)
				oos.writeObject(objs[i]);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return baos.toByteArray();
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Bits() {
		super();
	}

}
