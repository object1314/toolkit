/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

import java.lang.reflect.Array;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * Tool to resolve string for those not affords in {@link String}.
 * 
 * @author XuYanhang
 * @since 2020-10-27
 * @see String
 *
 */
public final class Strings {

	/**
	 * Create a string in specified char repeated specified count.
	 * 
	 * @param repeat      the repeat char
	 * @param repeatCount the repeat count of the char
	 * @return result string of the char repeated
	 */
	public static String repeatString(char repeat, int repeatCount) {
		if (repeatCount < 0)
			throw new IllegalArgumentException();
		if (0 == repeatCount)
			return "";
		char[] buf = new char[repeatCount];
		for (int i = 0; i < repeatCount; i++)
			buf[i] = repeat;
		return new String(buf);
	}

	/**
	 * Create a string in specified string repeated specified count.
	 * 
	 * @param repeat      the repeat string
	 * @param repeatCount the repeat count of the string
	 * @return result string of the string repeated
	 */
	public static String repeatString(String repeat, int repeatCount) {
		if (repeatCount < 0)
			throw new IllegalArgumentException();
		if (0 == repeatCount || repeat.isEmpty())
			return "";
		if (1 == repeatCount)
			return repeat;
		int srcLen = repeat.length();
		int dstLen = Math.multiplyExact(repeatCount, srcLen);
		char[] buf = new char[dstLen];
		for (int i = 0; i < dstLen; i += srcLen)
			repeat.getChars(0, srcLen, buf, i);
		return new String(buf);
	}

	/**
	 * Returns a new String composed of copies of the {@code Object elements} joined
	 * together with a copy of the specified {@code delimiter}.
	 * 
	 * @param delimiter delimiter to split these elements
	 * @param prefix    prefix before all elements
	 * @param suffix    suffix after all elements
	 * @param elements  the elements to cast
	 * @return string on all elements joined
	 * @see String#join(CharSequence, CharSequence...)
	 * @see String#join(CharSequence, Iterable)
	 * @throws IllegalArgumentException if the elements can't be joined when not an
	 *                                  Iterable non an array
	 */
	public static String join(String delimiter, String prefix, String suffix, Object elements) {
		if (null == elements)
			throw new NullPointerException();
		if (elements instanceof Iterable)
			return join(delimiter, prefix, suffix, (Iterable<?>) elements, (Function<Object, String>) null);
		if (elements.getClass().isArray()) {
			int l = Array.getLength(elements);
			StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
			for (int i = 0; i < l; i++)
				joiner.add(castSequence(Array.get(elements, i)));
			return joiner.toString();
		}
		throw new IllegalArgumentException("Not an iterable object");
	}

	/**
	 * Returns a new String composed of copies of the {@code T[] elements} joined
	 * together with a copy of the specified {@code delimiter}.
	 * 
	 * @param delimiter delimiter to split these elements
	 * @param prefix    prefix before all elements
	 * @param suffix    suffix after all elements
	 * @param elements  the elements to cast
	 * @param toString  caster for each element to string
	 * @return string on all elements joined
	 * @see String#join(CharSequence, CharSequence...)
	 */
	public static <T> String join(String delimiter, String prefix, String suffix, T[] elements,
			Function<? super T, ? extends CharSequence> toString) {
		StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
		for (int i = 0, l = elements.length; i < l; i++)
			joiner.add(null == toString ? castSequence(elements[i]) : toString.apply(elements[i]));
		return joiner.toString();
	}

	/**
	 * Returns a new String composed of copies of the {@code Iterable elements}
	 * joined together with a copy of the specified {@code delimiter}.
	 * 
	 * @param delimiter delimiter to split these elements
	 * @param prefix    prefix before all elements
	 * @param suffix    suffix after all elements
	 * @param elements  the elements to cast
	 * @param toString  caster for each element to string
	 * @return string on all elements joined
	 * @see String#join(CharSequence, Iterable)
	 */
	public static <T> String join(String delimiter, String prefix, String suffix, Iterable<T> elements,
			Function<? super T, ? extends CharSequence> toString) {
		StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
		for (T element : elements)
			joiner.add(null == toString ? castSequence(element) : toString.apply(element));
		return joiner.toString();
	}

	/**
	 * Align a string in specified length. If the string length is longer than the
	 * specified length, head char pops until the length aligned. If the string
	 * length is shorter, specified char pushes at the head.
	 * 
	 * @param str    string value to align
	 * @param length required length
	 * @param c      the char would append when the aligned string is shorter
	 * @return string value after aligned
	 */
	public static String alignStringHead(String str, int length, char c) {
		if (length < 0)
			throw new IllegalArgumentException();
		if (str.length() == length)
			return str;
		if (str.length() > length)
			return str.substring(str.length() - length);
		char[] buf = new char[length];
		str.getChars(0, str.length(), buf, length - str.length());
		for (int i = length - str.length() - 1; i > -1; i--)
			buf[i] = c;
		return new String(buf);
	}

	/**
	 * Align a string in specified length. If the string length is longer than the
	 * specified length, tail char removes until the length aligned. If the string
	 * length is shorter, specified char adds at the tail.
	 * 
	 * @param str    string value to align
	 * @param length required length
	 * @param c      the char would append when the aligned string is shorter
	 * @return string value after aligned
	 */
	public static String alignStringTail(String str, int length, char c) {
		if (length < 0)
			throw new IllegalArgumentException();
		if (str.length() == length)
			return str;
		if (str.length() > length)
			return str.substring(0, length);
		char[] buf = new char[length];
		str.getChars(0, str.length(), buf, 0);
		for (int i = str.length(); i < length; i++)
			buf[i] = c;
		return new String(buf);
	}

	/**
	 * Cast an object to char sequence. Returns the origin value if it's a
	 * {@link CharSequence} else calls the {@link Object#toString()}.
	 * 
	 * @param value the value to cast from
	 * @return a CharSequence
	 */
	public static CharSequence castSequence(Object value) {
		if (null == value)
			return null;
		if (value instanceof CharSequence)
			return (CharSequence) value;
		return value.toString();
	}

	/**
	 * Produce a string in double quotes with backslash sequences in all the right
	 * places. A backslash will be inserted within /<, allowing JSON text to be
	 * delivered in HTML. In JSON text, a string cannot contain a control character
	 * or an unescaped quote or backslash.<br>
	 * <strong>CAUTION:</strong> if <code>string</code> represents a javascript
	 * function, translation of characters will take place. This will produce a
	 * non-conformant javascript function.
	 *
	 * @param origin An origin string
	 * @return A String correctly formatted for insertion in a JSON text
	 */
	public static String quoteJson(CharSequence origin) {
		if (null == origin)
			return "null";
		int len = origin.length();
		if (len == 0)
			return "\"\"";
		final StringBuilder target = new StringBuilder((int) (len * 1.5));
		char b;
		char c = 0;
		int i;
		String t;
		char[] buffer = new char[1030];
		int cur = 0;
		target.append('"');
		for (i = 0; i < len; i++) {
			if (cur > 1024) {
				target.append(buffer, 0, cur);
				cur = 0;
			}
			b = c;
			c = origin.charAt(i);
			switch (c) {
			case '\\':
			case '"':
				buffer[cur++] = '\\';
				buffer[cur++] = c;
				break;
			case '/':
				if (b == '<') {
					buffer[cur++] = '\\';
				}
				buffer[cur++] = c;
				break;
			default:
				if (c < ' ') {
					switch (c) {
					case '\b':
						buffer[cur++] = '\\';
						buffer[cur++] = 'b';
						break;
					case '\t':
						buffer[cur++] = '\\';
						buffer[cur++] = 't';
						break;
					case '\n':
						buffer[cur++] = '\\';
						buffer[cur++] = 'n';
						break;
					case '\f':
						buffer[cur++] = '\\';
						buffer[cur++] = 'f';
						break;
					case '\r':
						buffer[cur++] = '\\';
						buffer[cur++] = 'r';
						break;
					default:
						t = "000" + Integer.toHexString(c);
						int tLength = t.length();
						buffer[cur++] = '\\';
						buffer[cur++] = 'u';
						buffer[cur++] = t.charAt(tLength - 4);
						buffer[cur++] = t.charAt(tLength - 3);
						buffer[cur++] = t.charAt(tLength - 2);
						buffer[cur++] = t.charAt(tLength - 1);
					}
				} else {
					buffer[cur++] = c;
				}
			}
		}
		target.append(buffer, 0, cur);
		target.append('"');
		return target.toString();
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Strings() {
		super();
	}

}
