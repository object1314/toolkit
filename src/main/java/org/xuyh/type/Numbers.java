/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * Tool to resolve numbers. Only as-primitive number resolved here in
 * {@link Byte}, {@link Short}, {@link Integer}, {@link Long},
 * {@link BigInteger}, {@link Float}, {@link Double}, {@link BigDecimal}
 * 
 * @author XuYanhang
 * @since 2020-10-27
 *
 */
public final class Numbers {

	/**
	 * Cast an integer in specified radix and aligned length including the '-' if
	 * it's a negative value. Append <code>0</code> in head for extra length.
	 * 
	 * @param value  the number to format
	 * @param radix  the radix used
	 * @param length the length aligned
	 * @return number string after aligned
	 * @throws IndexOutOfBoundsException if the length is too short
	 */
	public static String toAlignString(int value, int radix, int length) {
		return toAlignString((long) value, radix, length);
	}

	/**
	 * Cast an integer in specified radix and aligned length including the '-' if
	 * it's a negative value. Append <code>0</code> in head for extra length.
	 * 
	 * @param value  the number to format
	 * @param radix  the radix used
	 * @param length the length aligned
	 * @return number string after aligned
	 * @throws IndexOutOfBoundsException if the length is too short
	 */
	public static String toAlignString(long value, int radix, int length) {
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
			throw new IllegalArgumentException("radix");
		if (length < 1)
			throw new IllegalArgumentException("length");
		boolean negative = value < 0;
		final char[] buf = new char[length];
		int cur = length - 1;
		if (!negative) {
			value = -value;
		}
		buf[0] = negative ? '-' : '0';
		while (value <= -radix) {
			buf[cur--] = Character.forDigit((int) -(value % radix), radix);
			value = value / radix;
		}
		buf[cur--] = Character.forDigit((int) -value, radix);
		while (cur > 0) {
			buf[cur--] = '0';
		}
		return new String(buf);
	}

	/**
	 * Formats a double value to specified format of fixed decimal length after '.'.
	 * Scale used ROUND_HALF_DOWN.
	 * 
	 * @param value         the number with decimal to format
	 * @param decimalLength the required length of decimal for those number after
	 *                      decimal dot
	 * @return number string after formatted
	 * @throws IllegalArgumentException if the number is NaN or Infinite
	 */
	public static String toAlignDecimalString(double value, int decimalLength) {
		if (!Double.isFinite(value))
			throw new IllegalArgumentException("Infinite or NaN");
		if (decimalLength < 0)
			throw new IllegalArgumentException();
		if (decimalLength == 0)
			return new DecimalFormat("0").format(value);
		char[] format = new char[2 + decimalLength];
		for (int i = decimalLength + 1; i > 1; i--)
			format[i] = '0';
		format[1] = '.';
		format[0] = '0';
		return new DecimalFormat(new String(format)).format(value);
	}

	/**
	 * Trims a double value in specified format of fixed decimal length after '.'
	 * where removes the extra zero in tail. Scale used ROUND_HALF_DOWN.
	 * 
	 * @param value         the number with decimal to format
	 * @param decimalLength the required length of decimal for those number after
	 *                      decimal dot
	 * @return number string after formatted and trimmed
	 * @throws IllegalArgumentException if the number is NaN or Infinite
	 * @see #toAlignDecimalString(double, int)
	 */
	public static String toTrimDecimalString(double value, int decimalLength) {
		String result = toAlignDecimalString(value, decimalLength);
		int dotIndex = result.indexOf('.');
		if (dotIndex < 0)
			return result;
		int begin = result.length() - 1;
		while (begin > dotIndex && result.charAt(begin) == '0')
			begin--;
		return (begin == dotIndex) ? result.substring(0, begin) : result.substring(0, begin + 1);
	}

	/**
	 * Cast an object value to a number value who is checked on #isNumber(Object) or
	 * <code>null</code>. Returns <code>null</code> if parses failed, or it's an
	 * infinite value or NaN(not a number).
	 * 
	 * @param value value to check cast
	 * @return number after casted or <code>null</code> if failed
	 * @see #isNumber(Object)
	 */
	public static Number castNumber(Object value) {
		if (null == value)
			return null;
		// Parse when it is a number
		if (isNumber(value)) {
			return (Number) value;
		}
		// Special check
		if ((value instanceof Double) || (value instanceof Float))
			return null;
		// Parse as string
		String str = value.toString().toLowerCase();
		boolean isHex = false;
		int hexSignIndex = str.indexOf("0x");
		if (hexSignIndex >= 0) {
			isHex = true;
			if (hexSignIndex == 0)
				str = str.substring(2);
			else
				str = str.substring(0, hexSignIndex) + str.substring(hexSignIndex + 2);
		}
		int dotIndex = str.lastIndexOf('.');
		int expIndex = str.indexOf('e');
		boolean isInt = dotIndex < 0 && expIndex < 0;
		if (dotIndex >= 0 && expIndex < 0) {
			boolean maybeInt = true;
			for (int index = str.length() - 1; index > dotIndex; index--) {
				if (str.charAt(index) != '0') {
					maybeInt = false;
					break;
				}
			}
			if (maybeInt) {
				if (dotIndex == 0) {
					return 0;
				} else {
					str = str.substring(0, dotIndex);
					isInt = true;
				}
			}
		}
		if (isInt) {
			try {
				return Integer.valueOf(str, isHex ? 16 : 10);
			} catch (Exception e) {
			}
			try {
				return Long.valueOf(str, isHex ? 16 : 10);
			} catch (Exception e) {
			}
			try {
				return new BigInteger(str, isHex ? 16 : 10);
			} catch (Exception e) {
			}
		}
		try {
			return new BigDecimal(str);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Cast an object value to integer number. Returns <code>null</code> if parse
	 * failed, or it's an infinite value or NaN(not a number).
	 * 
	 * @param value value to check cast
	 * @return integer number after casted or <code>null</code> if failed
	 */
	public static Number castInteger(Object value) {
		Number num = castNumber(value);
		if (null == num)
			return null;

		if (isInteger(num))
			return num;

		if (value instanceof BigDecimal)
			return ((BigDecimal) value).toBigInteger();

		double doubleValue = num.doubleValue();
		if (!Double.isFinite(doubleValue))
			return null;
		if (doubleValue < Integer.MAX_VALUE && doubleValue > Integer.MIN_VALUE)
			return num.intValue();
		if (doubleValue < Long.MAX_VALUE && doubleValue > Long.MIN_VALUE)
			return num.longValue();
		return new BigDecimal(doubleValue).toBigInteger();
	}

	/**
	 * Returns <code>true</code> if the number is a valid handle number of an
	 * as-primitive number in type of in {@link Byte}, {@link Short},
	 * {@link Integer}, {@link Long}, {@link BigInteger}, {@link Float},
	 * {@link Double}, {@link BigDecimal}. <code>Infinite</code> or <code>NaN</code>
	 * not supported.
	 * 
	 * @param value the number value to check
	 * @return if the number is a valid handled in the class
	 * @see #castNumber(Object)
	 */
	public static boolean isNumber(Object value) {
		if (null == value || !(value instanceof Number))
			return false;
		if (value instanceof Double) {
			if (!Double.isFinite(((Double) value).doubleValue())) {
				return false;
			}
			return true;
		}
		if (value instanceof Float) {
			if (!Float.isFinite(((Float) value).floatValue())) {
				return false;
			}
			return true;
		}
		return (value instanceof BigDecimal) || (value instanceof BigInteger) || (value instanceof Long)
				|| (value instanceof Integer) || (value instanceof Short) || (value instanceof Byte);
	}

	/**
	 * Returns <code>true</code> if a number is an integer number in type as
	 * {@link Byte}, {@link Short}, {@link Integer}, {@link Long},
	 * {@link BigInteger}
	 * 
	 * @param value the number value to check
	 * @return true if the value is in an integer type as {@link Byte},
	 *         {@link Short}, {@link Integer}, {@link Long}, {@link BigInteger}
	 */
	public static boolean isInteger(Number value) {
		if (null == value)
			return false;
		return ((value instanceof BigInteger) || (value instanceof Long) || (value instanceof Integer)
				|| (value instanceof Short) || (value instanceof Byte));
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Numbers() {
		super();
	}

}
