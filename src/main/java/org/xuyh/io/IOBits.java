/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * Operator to manage bits on IO stream. Provides some methods on read(get) or
 * write(set) in some java types.
 * <p>
 * Short names: 'U'--unsigned; 'R'--reverse(smallEndian).
 * 
 * @author XuYanhang
 * @since 2020-10-21
 */
public class IOBits {

	/*
	 * Methods for unpacking values from input streams.
	 */

	/**
	 * Returns the next byte value, required 1 byte.
	 * 
	 * @param in source to load data
	 * @return next byte value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static byte getByte(InputStream in) {
		return (byte) getUByte(in);
	}

	/**
	 * Returns the next unsigned byte value, required 1 byte.
	 * 
	 * @param in source to load data
	 * @return next unsigned byte value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static int getUByte(InputStream in) {
		int b;
		try {
			b = in.read();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		if (b < 0)
			throw new IllegalStateException("EOF");
		return b;
	}

	/**
	 * Returns the next boolean value, required 1 byte.
	 * 
	 * @param in source to load data
	 * @return next boolean value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static boolean getBool(InputStream in) {
		return (getUByte(in) & 0X1) != 0;
	}

	/**
	 * Returns the next char value in big-endian, required 2 byte.
	 * 
	 * @param in source to load data
	 * @return next char value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static char getChar(InputStream in) {
		return (char) ((getUByte(in) << 8) | getUByte(in));
	}

	/**
	 * Returns the next char value in small-endian, required 2 byte.
	 * 
	 * @param in source to load data
	 * @return next char value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static char getRChar(InputStream in) {
		return (char) (getUByte(in) | (getUByte(in) << 8));
	}

	/**
	 * Returns the next short value in big-endian, required 2 byte.
	 * 
	 * @param in source to load data
	 * @return next short value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static short getShort(InputStream in) {
		return (short) ((getUByte(in) << 8) | getUByte(in));
	}

	/**
	 * Returns the next short value in small-endian, required 2 byte.
	 * 
	 * @param in source to load data
	 * @return next short value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static short getRShort(InputStream in) {
		return (short) (getUByte(in) | (getUByte(in) << 8));
	}

	/**
	 * Returns the next unsigned short value in big-endian, required 2 byte.
	 * 
	 * @param in source to load data
	 * @return next unsigned short value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static int getUShort(InputStream in) {
		return (getUByte(in) << 8) | getUByte(in);
	}

	/**
	 * Returns the next unsigned short value in small-endian, required 2 byte.
	 * 
	 * @param in source to load data
	 * @return next unsigned short value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static int getRUShort(InputStream in) {
		return getUByte(in) | (getUByte(in) << 8);
	}

	/**
	 * Returns the next integer value in big-endian, required 4 byte.
	 * 
	 * @param in source to load data
	 * @return next integer value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static int getInt(InputStream in) {
		return (getUByte(in) << 24) | (getUByte(in) << 16) | (getUByte(in) << 8) | getUByte(in);
	}

	/**
	 * Returns the next integer value in small-endian, required 4 byte.
	 * 
	 * @param in source to load data
	 * @return next integer value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static int getRInt(InputStream in) {
		return getUByte(in) | (getUByte(in) << 8) | (getUByte(in) << 16) | (getUByte(in) << 24);
	}

	/**
	 * Returns the next unsigned integer value in big-endian, required 4 byte.
	 * 
	 * @param in source to load data
	 * @return next unsigned integer value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static long getUInt(InputStream in) {
		return ((long) getInt(in)) & 0XffffffffL;
	}

	/**
	 * Returns the next unsigned integer value in small-endian, required 4 byte.
	 * 
	 * @param in source to load data
	 * @return next unsigned integer value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static long getRUInt(InputStream in) {
		return ((long) getRInt(in)) & 0XffffffffL;
	}

	/**
	 * Returns the next float value in big-endian, required 4 byte.
	 * 
	 * @param in source to load data
	 * @return next float value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static float getFloat(InputStream in) {
		return Float.intBitsToFloat(getInt(in));
	}

	/**
	 * Returns the next float value in small-endian, required 4 byte.
	 * 
	 * @param in source to load data
	 * @return next float value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static float getRFloat(InputStream in) {
		return Float.intBitsToFloat(getRInt(in));
	}

	/**
	 * Returns the next long value in big-endian, required 8 byte.
	 * 
	 * @param in source to load data
	 * @return next long value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static long getLong(InputStream in) {
		return (getUInt(in) << 32) | getUInt(in);
	}

	/**
	 * Returns the next long value in small-endian, required 8 byte.
	 * 
	 * @param in source to load data
	 * @return next long value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static long getRLong(InputStream in) {
		return getRUInt(in) | (getRUInt(in) << 32);
	}

	/**
	 * Returns the next double value in big-endian, required 8 byte.
	 * 
	 * @param in source to load data
	 * @return next double value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static double getDouble(InputStream in) {
		return Double.longBitsToDouble(getLong(in));
	}

	/**
	 * Returns the next double value in small-endian, required 8 byte.
	 * 
	 * @param in source to load data
	 * @return next double value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static double getRDouble(InputStream in) {
		return Double.longBitsToDouble(getLong(in));
	}

	/**
	 * Skip some bytes on check it where negative value is allowed only when the
	 * input stream handle this operation.
	 * 
	 * @param data source byte array data
	 * @param len  byte size to skip in data
	 * @return the <code>len</code>
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static int skip(InputStream in, int len) {
		int t;
		try {
			t = (int) in.skip(len);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		if (t != len)
			throw new IllegalStateException("EOF");
		return len;
	}

	/**
	 * Returns the next integer type value at specified size in big-endian mode,
	 * required <code>size</code> byte.
	 * 
	 * @param in   source to load data
	 * @param size the bytes size of this integer
	 * @return next specified bytes integer value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static int getInt(InputStream in, int size) {
		int v = 0;
		for (int i = 0; i < size; i++) {
			v <<= 8;
			v |= getUByte(in);
		}
		return v;
	}

	/**
	 * Returns the next integer type value at specified size in small-endian mode,
	 * required <code>size</code> byte.
	 * 
	 * @param in   source to load data
	 * @param size the bytes size of this integer
	 * @return next specified bytes integer value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static int getRInt(InputStream in, int size) {
		int v = 0;
		int i = 0;
		if (size < 5) {
			for (; i < size; i++)
				v |= (getUByte(in) << (i << 3));
		} else {
			for (; i < 4; i++)
				v |= (getUByte(in) << (i << 3));
			for (; i < size; i++)
				getUByte(in);
		}
		return v;
	}

	/**
	 * Returns the next long integer type value at specified size in big-endian
	 * mode, required <code>size</code> byte.
	 * 
	 * @param in   source to load data
	 * @param size the bytes size of this long integer
	 * @return next specified bytes long integer value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static long getLong(InputStream in, int size) {
		long v = 0;
		for (int i = 0; i < size; i++) {
			v <<= 8;
			v |= getUByte(in);
		}
		return v;
	}

	/**
	 * Returns the next long integer type value at specified size in small-endian
	 * mode, required <code>size</code> byte.
	 * 
	 * @param in   source to load data
	 * @param size the bytes size of this long integer
	 * @return next specified bytes long integer value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static long getRLong(InputStream in, int size) {
		long v = 0;
		int i = 0;
		if (size < 9) {
			for (; i < size; i++)
				v |= ((long) getUByte(in) << (i << 3));
		} else {
			for (; i < 8; i++)
				v |= ((long) getUByte(in) << (i << 3));
			for (; i < size; i++)
				getUByte(in);
		}
		return v;
	}

	/**
	 * Returns the bytes in specified size, required <code>len</code> bytes.
	 * 
	 * @param in  source to load data
	 * @param len bytes length to get
	 * @return the bytes
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static byte[] getBytes(InputStream in, int len) {
		byte[] target = new byte[len];
		getBytes(in, target, 0, len);
		return target;
	}

	/**
	 * Read the bytes in specified size, required <code>len</code> bytes.
	 * 
	 * @param in     source to load data
	 * @param target target bytes to storage the data
	 * @param off    target bytes storage position
	 * @param len    bytes length to get
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 */
	public static void getBytes(InputStream in, byte[] target, int off, int len) {
		if (off < 0 || off + len > target.length || off > off + len)
			throw new IndexOutOfBoundsException();
		int rl;
		try {
			while ((rl = in.read(target, off, len)) >= 0) {
				off += rl;
				len -= rl;
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		if (len > 0)
			throw new IllegalStateException("EOF");
	}

	/**
	 * Returns the next UTF-8 string value, required <code>len</code> bytes.
	 * 
	 * @param in  source to load data
	 * @param len bytes length to get
	 * @return the string value
	 * @throws IllegalStateException when an IO exception happens or reach the end
	 *                               or fails decode the bytes as UTF-8 string
	 */
	public static String getUTF(InputStream in, int len) {
		StringBuilder sb = new StringBuilder(len >> 1);
		int c1, c2, c3;
		int i = 0;
		while (i < len) {
			c1 = getUByte(in);
			if ((c1 & 0X80) == 0) {
				/* 0xxx xxxx */
				i++;
				sb.append((char) c1);
			} else if ((c1 & 0Xe0) == 0Xc0) {
				/* 110x xxxx 10xx xxxx */
				i += 2;
				if (i > len)
					throw new IllegalStateException("malformed utf");
				c2 = (int) getUByte(in);
				if ((c2 & 0Xc0) != 0x80)
					throw new IllegalStateException("malformed utf");
				sb.append((char) (((c1 & 0X1f) << 6) | (c2 & 0X3f)));
			} else if ((c1 & 0Xf0) == 0Xe0) {
				/* 1110 xxxx 10xx xxxx 10xx xxxx */
				i += 3;
				if (i > len)
					throw new IllegalStateException("malformed utf");
				c2 = getUByte(in);
				c3 = getUByte(in);
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
	 * @param in source to load data
	 * @return the object value
	 * @throws IllegalStateException if fail to get the object on IOException or
	 *                               ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObject(InputStream in) {
		Object o;
		UncloseProxyInputStream pin = new UncloseProxyInputStream(in);
		try (ObjectInputStream ois = new ObjectInputStream(pin)) {
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
	 * @param in    source to load data
	 * @param count objects count
	 * @return the objects value in array
	 * @throws IllegalStateException if fail to get the object on IOException or
	 *                               ClassNotFoundException
	 */
	public static Object[] getObjects(InputStream in, int count) {
		Object[] os = new Object[count];
		UncloseProxyInputStream pin = new UncloseProxyInputStream(in);
		try (ObjectInputStream ois = new ObjectInputStream(pin)) {
			for (int i = 0; i < count; i++)
				os[i] = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		return os;
	}

	/**
	 * Returns the objects value at specified position, required serial data's
	 * length bytes.
	 * 
	 * @see ObjectInputStream#readObject()
	 * @param in         source to load data
	 * @param collection collection to storage all objects
	 * @param count      objects count
	 * @return the objects value in array
	 * @throws IllegalStateException if fail to get the object on IOException or
	 *                               ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> void getObjects(InputStream in, Collection<T> collection, int count) {
		if (null == collection)
			throw new NullPointerException();
		UncloseProxyInputStream pin = new UncloseProxyInputStream(in);
		try (ObjectInputStream ois = new ObjectInputStream(pin)) {
			for (int i = 0; i < count; i++)
				collection.add((T) ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	/*
	 * Methods for packing values into output streams.
	 */

	/**
	 * Put the byte value into next write, required 1 byte.
	 * 
	 * @param out target to write data
	 * @param b   byte value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putByte(OutputStream out, int b) {
		try {
			out.write(b);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 1;
	}

	/**
	 * Put the boolean value into next write, required 1 byte.
	 * 
	 * @param out target to write data
	 * @param b   boolean value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putBool(OutputStream out, boolean b) {
		try {
			out.write(b ? 1 : 0);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 1;
	}

	/**
	 * Put the char value into next write in big-endian mode, required 2 bytes.
	 * 
	 * @param out target to write data
	 * @param c   char value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putChar(OutputStream out, int c) {
		try {
			out.write(c >>> 8);
			out.write(c);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 2;
	}

	/**
	 * Put the char value into next write in small-endian mode, required 2 bytes.
	 * 
	 * @param out target to write data
	 * @param c   char value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putRChar(OutputStream out, int c) {
		try {
			out.write(c);
			out.write(c >>> 8);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 2;
	}

	/**
	 * Put the short value into next write in big-endian mode, required 2 bytes.
	 * 
	 * @param out target to write data
	 * @param val short value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putShort(OutputStream out, int val) {
		try {
			out.write(val >>> 8);
			out.write(val);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 2;
	}

	/**
	 * Put the short value into next write in small-endian mode, required 2 bytes.
	 * 
	 * @param out target to write data
	 * @param val short value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putRShort(OutputStream out, int val) {
		try {
			out.write(val);
			out.write(val >>> 8);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 2;
	}

	/**
	 * Put the integer value into next write in big-endian mode, required 4 bytes.
	 * 
	 * @param out target to write data
	 * @param val integer value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putInt(OutputStream out, int val) {
		try {
			out.write(val >>> 24);
			out.write(val >>> 16);
			out.write(val >>> 8);
			out.write(val);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 4;
	}

	/**
	 * Put the integer value into next write in small-endian mode, required 4 bytes.
	 * 
	 * @param out target to write data
	 * @param val integer value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putRInt(OutputStream out, int val) {
		try {
			out.write(val);
			out.write(val >>> 8);
			out.write(val >>> 16);
			out.write(val >>> 24);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 4;
	}

	/**
	 * Put the float value into next write in big-endian mode, required 4 bytes.
	 * 
	 * @param out target to write data
	 * @param val float value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putFloat(OutputStream out, float val) {
		return putInt(out, Float.floatToIntBits(val));
	}

	/**
	 * Put the float value into next write in small-endian mode, required 4 bytes.
	 * 
	 * @param out target to write data
	 * @param val float value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putRFloat(OutputStream out, float val) {
		return putRInt(out, Float.floatToIntBits(val));
	}

	/**
	 * Put the long value into next write in big-endian mode, required 8 bytes.
	 * 
	 * @param out target to write data
	 * @param val long value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putLong(OutputStream out, long val) {
		try {
			out.write((int) (val >>> 56));
			out.write((int) (val >>> 48));
			out.write((int) (val >>> 40));
			out.write((int) (val >>> 32));
			out.write((int) (val >>> 24));
			out.write((int) (val >>> 16));
			out.write((int) (val >>> 8));
			out.write((int) val);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 8;
	}

	/**
	 * Put the long value into next write in small-endian mode, required 8 bytes.
	 * 
	 * @param out target to write data
	 * @param val long value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putRLong(OutputStream out, long val) {
		try {
			out.write((int) val);
			out.write((int) (val >>> 8));
			out.write((int) (val >>> 16));
			out.write((int) (val >>> 24));
			out.write((int) (val >>> 32));
			out.write((int) (val >>> 40));
			out.write((int) (val >>> 48));
			out.write((int) (val >>> 56));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return 8;
	}

	/**
	 * Put the double value into next write in big-endian mode, required 8 bytes.
	 * 
	 * @param out target to write data
	 * @param val double value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putDouble(OutputStream out, double val) {
		return putLong(out, Double.doubleToLongBits(val));
	}

	/**
	 * Put the double value into next write in small-endian mode, required 8 bytes.
	 * 
	 * @param out target to write data
	 * @param val double value to put
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putRDouble(OutputStream out, double val) {
		return putRLong(out, Double.doubleToLongBits(val));
	}

	/**
	 * Fill write in a specified byte value, required <code>size</code> bytes.
	 * 
	 * @param out  target to write data
	 * @param b    byte value to fill
	 * @param size bytes size to fill
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int fill(OutputStream out, int b, int size) {
		if (size < 0)
			throw new IllegalArgumentException();
		try {
			for (int i = 0; i < size; i++)
				out.write(b);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return size;
	}

	/**
	 * Put the integer value into next write in big-endian mode, required
	 * <code>size</code> bytes.
	 * 
	 * @param out  target to write data
	 * @param val  integer value to put
	 * @param size the byte size to write this integer value
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putInt(OutputStream out, int val, int size) {
		if (size < 0)
			throw new IllegalArgumentException();
		int i = size;
		while (i-- > 4)
			try {
				out.write(0);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		for (; i > 0; i--)
			try {
				out.write(val >>> (i << 3));
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		return size;
	}

	/**
	 * Put the integer value into next write in small-endian mode, required
	 * <code>size</code> bytes.
	 * 
	 * @param out  target to write data
	 * @param val  integer value to put
	 * @param size the byte size to write this integer value
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putRInt(OutputStream out, int val, int size) {
		if (size < 0)
			throw new IllegalArgumentException();
		for (int i = 0; i < size; i++) {
			try {
				out.write(val);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			val >>>= 8;
		}
		return size;
	}

	/**
	 * Put the long integer value into next write in big-endian mode, required
	 * <code>size</code> bytes.
	 * 
	 * @param out  target to write data
	 * @param val  long integer value to put
	 * @param size the byte size to write this long integer value
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putLong(OutputStream out, long val, int size) {
		if (size < 0)
			throw new IllegalArgumentException();
		int i = size;
		while (i-- > 8)
			try {
				out.write(0);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		for (; i > 0; i--)
			try {
				out.write((int) (val >>> (i << 3)));
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		return size;
	}

	/**
	 * Put the long integer value into next write in small-endian mode, required
	 * <code>size</code> bytes.
	 * 
	 * @param out  target to write data
	 * @param val  long integer value to put
	 * @param size the byte size to write this long integer value
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putRLong(OutputStream out, long val, int size) {
		if (size < 0)
			throw new IllegalArgumentException();
		for (int i = 0; i < size; i++) {
			try {
				out.write((int) val);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			val >>>= 8;
		}
		return size;
	}

	/**
	 * Put a bytes into the output stream, required <code>val.length</code> bytes.
	 * 
	 * @param out target to write data
	 * @param bs  bytes value to write
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putBytes(OutputStream out, byte[] bs) {
		try {
			out.write(bs, 0, bs.length);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return bs.length;
	}

	/**
	 * Put a bytes into the output stream, required <code>len</code> bytes.
	 * 
	 * @param out target to write data
	 * @param bs  bytes value to write
	 * @param off bytes value to write offset
	 * @param len bytes value to write length
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putBytes(OutputStream out, byte[] bs, int off, int len) {
		if (off < 0 || off + len > bs.length || off + len < off)
			throw new IndexOutOfBoundsException();
		try {
			out.write(bs, off, len);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return len;
	}

	/**
	 * Put a char sequence into output stream on UTF-8 encoding, required
	 * {@link Bits#measureUTFBytes(CharSequence)} bytes.
	 * 
	 * @param out target to write data
	 * @param cs  char sequence value to write
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putUTF(OutputStream out, CharSequence cs) {
		return putUTF(out, cs, 0, cs.length());
	}

	/**
	 * Put a char sequence into output stream on UTF-8 encoding, required
	 * {@link Bits#measureUTFBytes(CharSequence, int, int)} bytes.
	 * 
	 * @param out  target to write data
	 * @param cs   char sequence value to write
	 * @param coff offset in char sequence to write
	 * @param clen length of char sequence to write
	 * @return write bytes count
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static int putUTF(OutputStream out, CharSequence cs, int coff, int clen) {
		int cend = coff + clen;
		if (coff < 0 || cend > cs.length() || coff > cend)
			throw new IndexOutOfBoundsException();
		/* use charAt instead of copying String to char array */
		int p = 0;
		char c;
		for (int i = coff; i < cend; i++) {
			c = cs.charAt(i);
			if (c >= 0X0001 && c <= 0X007f) {
				try {
					out.write((int) c);
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				p += 1;
			} else if (c > 0X07ff) {
				try {
					out.write(0Xe0 | ((c >> 12) & 0X0f));
					out.write(0X80 | ((c >> 6) & 0X3f));
					out.write(0X80 | ((c >> 0) & 0X3f));
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				p += 3;
			} else {
				try {
					out.write(0Xc0 | ((c >> 6) & 0X1f));
					out.write(0X80 | ((c >> 0) & 0X3f));
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				p += 2;
			}
		}
		return p;
	}

	/**
	 * Put an object as bytes on JDK serialization encoding, required the serial
	 * data size bytes.
	 * 
	 * @param os  output stream to write the object
	 * @param obj object to serialize encode
	 * @return the write bytes count
	 * @throws IllegalStateException if serial happens any IOException
	 */
	public static int putObject(OutputStream os, Object obj) {
		UncloseProxyOutputStream pos = new UncloseProxyOutputStream(os);
		try (ObjectOutputStream oos = new ObjectOutputStream(pos)) {
			oos.writeObject(obj);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return (int) pos.count();
	}

	/**
	 * Put some objects as bytes on JDK serialization encoding, required the serial
	 * data size bytes.
	 * 
	 * @param os   output stream to write the object
	 * @param objs objects to serialize encode
	 * @return the write bytes count
	 * @throws IllegalStateException if serial happens any IOException
	 */
	public static int putObjects(OutputStream os, Object[] objs) {
		int count = objs.length;
		UncloseProxyOutputStream pos = new UncloseProxyOutputStream(os);
		try (ObjectOutputStream oos = new ObjectOutputStream(pos)) {
			for (int i = 0; i < count; i++)
				oos.writeObject(objs[i]);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return (int) pos.count();
	}

	/**
	 * Put some objects as bytes on JDK serialization encoding, required the serial
	 * data size bytes.
	 * 
	 * @param os   output stream to write the object
	 * @param objs objects to serialize encode
	 * @return the write bytes count
	 * @throws IllegalStateException if serial happens any IOException
	 */
	public static int putObjects(OutputStream os, Iterable<?> objs) {
		java.util.Iterator<?> ite = objs.iterator();
		UncloseProxyOutputStream pos = new UncloseProxyOutputStream(os);
		try (ObjectOutputStream oos = new ObjectOutputStream(pos)) {
			while (ite.hasNext())
				oos.writeObject(ite.next());
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return (int) pos.count();
	}

	/*
	 * Methods to help convert data
	 */

	/**
	 * Read all data from an input stream into an output stream target.
	 * 
	 * @param in  source to read data
	 * @param out target to write data
	 * @throw IllegalStateException when an IO exception happens
	 */
	public static void reloadData(InputStream in, OutputStream out) {
		byte[] buf = new byte[256];
		int tl;
		try {
			while ((tl = in.read(buf)) > -1)
				out.write(buf, 0, tl);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Read the specified length data from an input stream into an output stream
	 * target.
	 * 
	 * @param in  source to read data
	 * @param out target to write data
	 * @param len target length to read data from the input stream
	 * @throw IllegalStateException when an IO exception happens or reaches the end
	 *        of the source <code>in</code>
	 */
	public static void reloadData(InputStream in, OutputStream out, int len) {
		byte[] buf = new byte[len > 256 ? 256 : len];
		int rl = len;
		int tl;
		try {
			while ((tl = in.read(buf, 0, Math.min(buf.length, rl))) > -1) {
				out.write(buf, 0, tl);
				rl -= tl;
				if (rl <= 0)
					break;
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		if (rl > 0)
			throw new IllegalStateException("EOF");
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private IOBits() {
		super();
	}

}
