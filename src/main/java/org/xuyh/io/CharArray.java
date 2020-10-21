/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.io;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.function.IntConsumer;

/**
 * Char sequence on an array. Sometimes like a string but the array
 * {@link #value} field is allowed to change in elements and the change is
 * shared when the {@link #value} is not copy for {@link #subArray(int, int)}
 * and {@link #rootArray()}.
 * 
 * @author XuYanhang
 * @since 2020-10-20
 *
 */
public final class CharArray
		implements CharSequence, Cloneable, java.io.Serializable, Iterable<Character>, Comparable<CharArray> {

	/** The value is used for character storage */
	private final char[] value;
	/** offset of the char array to manage, never set here */
	private final int off;
	/** length of the char array to manage, never set here */
	private final int len;

	/**
	 * Creates {@link CharArray} that uses <code>value</code> as its {@link #value}.
	 * The initial value of {@link #off} is 0, The initial value of {@link #len} is
	 * <code>value.length</code>. The value array is not copied.
	 * 
	 * @param value char array to manage
	 */
	public CharArray(char... value) {
		super();
		this.value = value;
		this.off = 0;
		this.len = value.length;
	}

	/**
	 * Creates {@link CharArray} that uses <code>value</code> as its {@link #value}.
	 * The initial value of {@link #off} is <code>off</code>, The initial value of
	 * {@link #len} is <code>len</code>. The value array is not copied.
	 * 
	 * @param value char array to manage
	 * @param off   offset of the char array to manage
	 * @param len   length of the char array to manage
	 */
	public CharArray(char[] value, int off, int len) {
		super();
		if (off < 0 || off + len > value.length || off > off + len)
			throw new IndexOutOfBoundsException();
		this.value = value;
		this.off = off;
		this.len = len;
	}

	/**
	 * Returns the managed origin character array in this {@link CharArray}.
	 * 
	 * @return the {@link #value}
	 */
	public char[] source() {
		return value;
	}

	/**
	 * Returns the managed origin character array's offset in this
	 * {@link CharArray}.
	 * 
	 * @return the {@link #offset}
	 */
	public char[] offset() {
		return value;
	}

	/**
	 * Returns the managed origin character array's length in this
	 * {@link CharArray}.
	 * 
	 * @return the {@link #len}
	 * @see java.lang.CharSequence#length()
	 */
	@Override
	public int length() {
		return len;
	}

	/**
	 * Returns if the sequence is empty.
	 * 
	 * @return true if the sequence is empty or false if not
	 */
	public boolean isEmpty() {
		return len == 0;
	}

	/**
	 * @see java.lang.CharSequence#charAt(int)
	 */
	@Override
	public char charAt(int index) {
		if (index < 0 || index >= len)
			throw new IndexOutOfBoundsException();
		return value[index + off];
	}

	/**
	 * Returns the character (Unicode code point) at the specified index. The index
	 * refers to {@code char} values (Unicode code units) and ranges from {@code 0}
	 * to {@link #length()}{@code  -1}.
	 *
	 * <p>
	 * If the {@code char} value specified at the given index is in the
	 * high-surrogate range, the following index is less than the length of this
	 * {@code String}, and the {@code char} value at the following index is in the
	 * low-surrogate range, then the supplementary code point corresponding to this
	 * surrogate pair is returned. Otherwise, the {@code char} value at the given
	 * index is returned.
	 *
	 * @param index the index to the {@code char} values
	 * @return the code point value of the character at the {@code index}
	 * @throws IndexOutOfBoundsException if the {@code index} argument is negative
	 *                                   or not less than the length of this string.
	 */
	public int codePointAt(int index) {
		if (index < 0 || index >= len)
			throw new IndexOutOfBoundsException();
		return Character.codePointAt(value, index + off, len + off);
	}

	/**
	 * Returns the character (Unicode code point) before the specified index. The
	 * index refers to {@code char} values (Unicode code units) and ranges from
	 * {@code 1} to {@link #length()}.
	 *
	 * <p>
	 * If the {@code char} value at {@code (index - 1)} is in the low-surrogate
	 * range, {@code (index - 2)} is not negative, and the {@code char} value at
	 * {@code (index -
	 * 2)} is in the high-surrogate range, then the supplementary code point value
	 * of the surrogate pair is returned. If the {@code char} value at
	 * {@code index -
	 * 1} is an unpaired low-surrogate or a high-surrogate, the surrogate value is
	 * returned.
	 *
	 * @param index the index following the code point that should be returned
	 * @return the Unicode code point value before the given index.
	 * @throws IndexOutOfBoundsException if the {@code index} argument is less than
	 *                                   1 or greater than the length of this array.
	 */
	public int codePointBefore(int index) {
		if (index < 1 || index > len)
			throw new IndexOutOfBoundsException();
		return Character.codePointBefore(value, index + off, off);
	}

	/**
	 * Returns the number of Unicode code points in the specified text range of this
	 * {@code CharArray}. The text range begins at the specified {@code beginIndex}
	 * and extends to the {@code char} at index {@code endIndex - 1}. Thus the
	 * length (in {@code char}s) of the text range is {@code endIndex-beginIndex}.
	 * Unpaired surrogates within the text range count as one code point each.
	 *
	 * @param beginIndex the index to the first {@code char} of the text range.
	 * @param endIndex   the index after the last {@code char} of the text range.
	 * @return the number of Unicode code points in the specified text range
	 * @throws IndexOutOfBoundsException if the {@code beginIndex} is negative, or
	 *                                   {@code endIndex} is larger than the length
	 *                                   of this {@code CharArray}, or
	 *                                   {@code beginIndex} is larger than
	 *                                   {@code endIndex}.
	 */
	public int codePointCount(int beginIndex, int endIndex) {
		if (beginIndex < 0 || endIndex > len || beginIndex > endIndex)
			throw new IndexOutOfBoundsException();
		return Character.codePointCount(value, beginIndex + off, endIndex - beginIndex);
	}

	/**
	 * Returns the index within this {@code CharArray} that is offset from the given
	 * {@code index} by {@code codePointOffset} code points. Unpaired surrogates
	 * within the text range given by {@code index} and {@code codePointOffset}
	 * count as one code point each.
	 *
	 * @param index           the index to be offset
	 * @param codePointOffset the offset in code points
	 * @return the index within this {@code CharArray}
	 * @throws IndexOutOfBoundsException if {@code index} is negative or larger than
	 *                                   the length of this {@code CharArray}, or if
	 *                                   {@code codePointOffset} is positive and the
	 *                                   substring starting with {@code index} has
	 *                                   fewer than {@code codePointOffset} code
	 *                                   points, or if {@code codePointOffset} is
	 *                                   negative and the subSequence before
	 *                                   {@code index} has fewer than the absolute
	 *                                   value of {@code codePointOffset} code
	 *                                   points.
	 */
	public int offsetByCodePoints(int index, int codePointOffset) {
		if (index < 0 || index > len)
			throw new IndexOutOfBoundsException();
		return Character.offsetByCodePoints(value, off, len, index + off, codePointOffset) - off;
	}

	/**
	 * Set the char as a specified value at specified position.
	 * 
	 * @param index specified position to set
	 * @param c     specified char value to set
	 */
	public void setChar(int index, int c) {
		if (c != (char) c)
			throw new IllegalArgumentException("illegal char");
		if (index < 0 || index >= len)
			throw new IndexOutOfBoundsException();
		value[index + off] = (char) c;
	}

	/**
	 * Set the codes as a specified value at specified position.
	 * 
	 * @param index specified position to set
	 * @param c     specified code value to set
	 */
	public CharArray setCodePoint(int index, int c) {
		if (!Character.isValidCodePoint(c))
			throw new IllegalArgumentException("illegal code");
		if (c < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
			if (index < 0 || index >= len)
				throw new IndexOutOfBoundsException();
			value[index + off] = (char) c;
		} else {
			if (index < 0 || index >= len - 1)
				throw new IndexOutOfBoundsException();
			value[index + off] = Character.highSurrogate(c);
			value[index + off + 1] = Character.lowSurrogate(c);
		}
		return this;
	}

	/**
	 * Returns the index within this array of the first occurrence of the specified
	 * character. If a character with value {@code ch} occurs in the character
	 * sequence represented by this {@code CharArray} object, then the index (in
	 * Unicode code units) of the first such occurrence is returned. For values of
	 * {@code c} in the range from 0 to 0Xffff (inclusive), this is the smallest
	 * value <i>k</i> such that: <blockquote>
	 * 
	 * <pre>
	 * this.charAt(<i>k</i>) == c
	 * </pre>
	 * 
	 * </blockquote> is true. For other values of {@code c}, it is the smallest
	 * value <i>k</i> such that: <blockquote>
	 * 
	 * <pre>
	 * this.codePointAt(<i>k</i>) == c
	 * </pre>
	 * 
	 * </blockquote> is true. In either case, if no such character occurs in this
	 * string, then {@code -1} is returned.
	 *
	 * @param c a character (Unicode code point).
	 * @return the index of the first occurrence of the character in the character
	 *         sequence represented by this object, or {@code -1} if the character
	 *         does not occur.
	 */
	public int indexOf(int c) {
		return indexOf(c, 0);
	}

	/**
	 * Returns the index within this string of the first occurrence of the specified
	 * character, starting the search at the specified index.
	 * <p>
	 * If a character with value {@code c} occurs in the character sequence
	 * represented by this {@code CharArray} object at an index no smaller than
	 * {@code fromIndex}, then the index of the first such occurrence is returned.
	 * For values of {@code c} in the range from 0 to 0Xffff (inclusive), this is
	 * the smallest value <i>k</i> such that: <blockquote>
	 * 
	 * <pre>
	 * (this.charAt(<i>k</i>) == c) {@code &&} (<i>k</i> &gt;= fromIndex)
	 * </pre>
	 * 
	 * </blockquote> is true. For other values of {@code c}, it is the smallest
	 * value <i>k</i> such that: <blockquote>
	 * 
	 * <pre>
	 * (this.codePointAt(<i>k</i>) == c) {@code &&} (<i>k</i> &gt;= fromIndex)
	 * </pre>
	 * 
	 * </blockquote> is true. In either case, if no such character occurs in this
	 * string at or after position {@code fromIndex}, then {@code -1} is returned.
	 *
	 * <p>
	 * There is no restriction on the value of {@code fromIndex}. If it is negative,
	 * it has the same effect as if it were zero: this entire string may be
	 * searched. If it is greater than the length of this string, it has the same
	 * effect as if it were equal to the length of this string: {@code -1} is
	 * returned.
	 *
	 * <p>
	 * All indices are specified in {@code char} values (Unicode code units).
	 *
	 * @param c         a character (Unicode code point).
	 * @param fromIndex the index to start the search from.
	 * @return the index of the first occurrence of the character in the character
	 *         sequence represented by this object that is greater than or equal to
	 *         {@code fromIndex}, or {@code -1} if the character does not occur.
	 */
	public int indexOf(int c, int fromIndex) {
		if (fromIndex < 0)
			fromIndex = 0;
		if (fromIndex >= len)
			// Note: fromIndex might be near -1>>>1.
			return -1;
		if (!Character.isValidCodePoint(c))
			return -1;

		int begin = off + fromIndex;
		int end = off + len;
		if (c < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
			// handle most cases here (c is a BMP code point)
			for (int i = begin; i < end; i++)
				if (value[i] == c)
					return i - off;
		} else {
			final char hi = Character.highSurrogate(c);
			final char lo = Character.lowSurrogate(c);
			end--;
			for (int i = begin; i < end; i++)
				if (value[i] == hi && value[i + 1] == lo)
					return i - off;
		}
		return -1;
	}

	/**
	 * Returns the index within this array of the last occurrence of the specified
	 * character. For values of {@code c} in the range from 0 to 0Xffff (inclusive),
	 * the index (in Unicode code units) returned is the largest value <i>k</i> such
	 * that: <blockquote>
	 * 
	 * <pre>
	 * this.charAt(<i>k</i>) == c
	 * </pre>
	 * 
	 * </blockquote> is true. For other values of {@code c}, it is the largest value
	 * <i>k</i> such that: <blockquote>
	 * 
	 * <pre>
	 * this.codePointAt(<i>k</i>) == c
	 * </pre>
	 * 
	 * </blockquote> is true. In either case, if no such character occurs in this
	 * string, then {@code -1} is returned. The {@code CharArray} is searched
	 * backwards starting at the last character.
	 *
	 * @param c a character (Unicode code point).
	 * @return the index of the last occurrence of the character in the character
	 *         sequence represented by this object, or {@code -1} if the character
	 *         does not occur.
	 */
	public int lastIndexOf(int c) {
		return lastIndexOf(c, len - 1);
	}

	/**
	 * Returns the index within this array of the last occurrence of the specified
	 * character, searching backward starting at the specified index. For values of
	 * {@code c} in the range from 0 to 0Xffff (inclusive), the index returned is
	 * the largest value <i>k</i> such that: <blockquote>
	 * 
	 * <pre>
	 * (this.charAt(<i>k</i>) == c) {@code &&} (<i>k</i> &lt;= fromIndex)
	 * </pre>
	 * 
	 * </blockquote> is true. For other values of {@code c}, it is the largest value
	 * <i>k</i> such that: <blockquote>
	 * 
	 * <pre>
	 * (this.codePointAt(<i>k</i>) == c) {@code &&} (<i>k</i> &lt;= fromIndex)
	 * </pre>
	 * 
	 * </blockquote> is true. In either case, if no such character occurs in this
	 * string at or before position {@code fromIndex}, then {@code -1} is returned.
	 *
	 * <p>
	 * All indices are specified in {@code char} values (Unicode code units).
	 *
	 * @param c         a character (Unicode code point).
	 * @param fromIndex the index to start the search from. There is no restriction
	 *                  on the value of {@code fromIndex}. If it is greater than or
	 *                  equal to the length of this string, it has the same effect
	 *                  as if it were equal to one less than the length of this
	 *                  string: this entire string may be searched. If it is
	 *                  negative, it has the same effect as if it were -1: -1 is
	 *                  returned.
	 * @return the index of the last occurrence of the character in the character
	 *         sequence represented by this object that is less than or equal to
	 *         {@code fromIndex}, or {@code -1} if the character does not occur
	 *         before that point.
	 */
	public int lastIndexOf(int c, int fromIndex) {
		if (fromIndex >= len)
			// Note: fromIndex might be near -1>>>1.
			fromIndex = len - 1;
		if (fromIndex < 0)
			return -1;
		if (!Character.isValidCodePoint(c))
			return -1;

		int begin = fromIndex >= len ? len + off - 1 : fromIndex + off;
		int end = off - 1;
		if (c < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
			// handle most cases here (c is a BMP code point)
			for (int i = begin; i > end; i--)
				if (value[i] == c)
					return i - off;
		} else {
			char hi = Character.highSurrogate(c);
			char lo = Character.lowSurrogate(c);
			for (int i = begin - 1; i > end; i--)
				if (value[i] == hi && value[i + 1] == lo)
					return i - off;
		}
		return -1;
	}

	/**
	 * Performs the given action for each element of the array until all elements
	 * have been processed or the action throws an exception. Actions are performed
	 * in the order of the array. Exceptions thrown by the action are relayed to the
	 * caller.
	 *
	 * @param action The action to be performed for each character
	 * @throws NullPointerException if the specified action is null
	 */
	public void forEachChar(IntConsumer action) {
		if (null == action)
			throw new NullPointerException();
		int end = off + len;
		int i = off;
		while (i < end)
			action.accept(value[i++]);
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Character> iterator() {

		return new Iterator<Character>() {

			int cur = off;
			int end = off + len;

			@Override
			public boolean hasNext() {
				return cur < end;
			}

			@Override
			public Character next() {
				if (cur >= end)
					throw new java.util.NoSuchElementException();
				return value[cur++];
			}

		};
	}

	/**
	 * Replaces all occurrences of {@code oldChar} in this array with
	 * {@code newChar}.
	 *
	 * @param oldChar the old character
	 * @param newChar the new character
	 * @return this
	 */
	public CharArray replace(int oldChar, int newChar) {
		char oc = (char) oldChar;
		char nc = (char) newChar;
		if (oc == nc)
			return this;
		int end = off + len;
		for (int i = off; i < end; i++)
			if (value[i] == oc)
				value[i] = nc;
		return this;
	}

	/**
	 * Converts all of the characters in this {@code CharArray} to lower case using
	 * the rules of the root where only the 'a-z' converted to 'A-Z'.
	 *
	 * @return this
	 * @see java.lang.String#toLowerCase(java.util.Locale)
	 */
	public CharArray toLowerCase() {
		int end = off + len;
		int dist = (int) 'a' - (int) 'A';
		for (int i = off; i < end; i++)
			if (value[i] >= 'a' && value[i] <= 'z')
				value[i] -= dist;
		return this;
	}

	/**
	 * /** Converts all of the characters in this {@code CharArray} to upper case
	 * using the rules of the root where only the 'A-Z' converted to 'a-z'.
	 *
	 * @return this
	 * @see java.lang.String#toUpperCase(java.util.Locale)
	 */
	public CharArray toUpperCase() {
		int end = off + len;
		int dist = (int) 'a' - (int) 'A';
		for (int i = off; i < end; i++)
			if (value[i] >= 'A' && value[i] <= 'Z')
				value[i] += dist;
		return this;
	}

	/**
	 * Converts this character array to a new character array.
	 *
	 * @return a newly allocated character array whose length is the length of this
	 *         array and whose contents are initialized to contain the character
	 *         sequence represented by this array.
	 */
	public char[] toCharArray() {
		char[] copyValue = value;
		if (value.length > 0) {
			copyValue = new char[len];
			System.arraycopy(value, off, copyValue, 0, len);
		}
		return copyValue;
	}

	/**
	 * Returns a <code>CharArray</code> that is a root of this array. The root array
	 * has the public access like {@link #charAt(int)} and
	 * {@link #setChar(int, int)} to visit on the whole {@link #value} from
	 * <code>0</code> index to <code>value.length-1</code>.
	 * <p>
	 * Updates on characters of root-array has effect on this array while so does on
	 * root array if updates the this array.
	 *
	 * @return the specified array
	 *
	 */
	public CharArray rootArray() {
		return ((0 == off) && (value.length == len)) ? this : new CharArray(value);
	}

	/**
	 * Returns a <code>CharArray</code> that is a subArray of this array. The
	 * subArray starts with the <code>char</code> value at the specified index and
	 * ends with the <code>char</code> value at index <tt>end - 1</tt>. The length
	 * (in <code>char</code>s) of the returned array is <tt>end - start</tt>, so if
	 * <tt>start == end</tt> then an empty array is returned.
	 * <p>
	 * Updates on characters of sub-array has effect on parent-array while so does
	 * on sub-array if updates the parent-array.
	 *
	 * @param start the start index, inclusive
	 * @param end   the end index, exclusive
	 * @return the specified array
	 * @throws IndexOutOfBoundsException if <tt>start</tt> or <tt>end</tt> are
	 *                                   negative, if <tt>end</tt> is greater than
	 *                                   <tt>length()</tt>, or if <tt>start</tt> is
	 *                                   greater than <tt>end</tt>
	 */
	public CharArray subArray(int start, int end) {
		int subLen = end - start;
		if (start < 0 || end > len || subLen < 0)
			throw new IndexOutOfBoundsException();
		return ((start == off) && (subLen == len)) ? this : new CharArray(value, start + off, subLen);
	}

	/**
	 * Returns a string that is a substring of this char array. The substring begins
	 * with the character at the specified index and extends to the end of this
	 * sequence.
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * "unhappy".substring(2) returns "happy"
	 * "Harbison".substring(3) returns "bison"
	 * "emptiness".substring(9) returns "" (an empty string)
	 * </pre>
	 * 
	 * </blockquote>
	 *
	 * @param start the beginning index, inclusive.
	 * @return the specified substring.
	 * @throws IndexOutOfBoundsException if {@code beginIndex} is negative or larger
	 *                                   than the length of this {@code CharArray}
	 *                                   object.
	 */
	public String substring(int start) {
		if (start < 0 || start > len)
			throw new IndexOutOfBoundsException();
		return new String(value, start + off, len - start);
	}

	/**
	 * Returns a new {@code String} that contains a subsequence of characters
	 * currently contained in this sequence. The substring begins at the specified
	 * {@code start} and extends to the character at index {@code end - 1}.
	 *
	 * @param start The beginning index, inclusive.
	 * @param end   The ending index, exclusive.
	 * @return The new string.
	 * @throws IndexOutOfBoundsException if {@code start} or {@code end} are
	 *                                   negative or greater than {@code length()},
	 *                                   or {@code start} is greater than
	 *                                   {@code end}.
	 */
	public String substring(int start, int end) {
		int subLen = end - start;
		if (start < 0 || end > len || subLen < 0)
			throw new IndexOutOfBoundsException();
		return new String(value, start + off, subLen);
	}

	/**
	 * Returns a <code>CharSequence</code> that is a subsequence of this sequence.
	 * The subsequence starts with the <code>char</code> value at the specified
	 * index and ends with the <code>char</code> value at index <tt>end - 1</tt>.
	 * The length (in <code>char</code>s) of the returned sequence is
	 * <tt>end - start</tt>, so if <tt>start == end</tt> then an empty sequence is
	 * returned.
	 * <p>
	 * The contents of the character array are not copied; subsequent modification
	 * of the character array has affect on the returned sequence.
	 * 
	 * @see #subArray(int, int)
	 * @param start the start index, inclusive
	 * @param end   the end index, exclusive
	 * @return the specified subsequence
	 * @throws IndexOutOfBoundsException if <tt>start</tt> or <tt>end</tt> are
	 *                                   negative, if <tt>end</tt> is greater than
	 *                                   <tt>length()</tt>, or if <tt>start</tt> is
	 *                                   greater than <tt>end</tt>
	 * 
	 */
	@Override
	public CharArray subSequence(int start, int end) {
		return subArray(start, end);
	}

	/**
	 * Returns the reader on this char array. All operation has the effect on this
	 * array.
	 * 
	 * @return reader on this char array from <code>off</code> to
	 *         <code>off + end - 1</code>
	 */
	public Reader openReader() {
		return new CharArrayReader(value, off, len);
	}

	/**
	 * Returns the writer on this char array. All operation has the effect on this
	 * array.
	 * 
	 * @return writer on this char array from <code>off</code> to
	 *         <code>off + end - 1</code>
	 */
	public Writer openWriter() {
		return new SpecifyCharArrayWriter(value, off, len);
	}

	/**
	 * Writes all contents of this char array to the specified writer argument, as
	 * if by calling the writer's write method using
	 * <code>writer.write(value, off, len)</code>.
	 *
	 * @param writer the writer to which to write the value
	 * @return this
	 * @throws IOException if an I/O error occurs
	 */
	public CharArray writeTo(Writer writer) throws IOException {
		writer.write(value, off, len);
		return this;
	}

	/**
	 * Read some contents into this char array from the specified reader argument,
	 * as if by calling the reader's write method using
	 * <code>reader.read(value, off, len)</code>, but fully read. If the characters
	 * from the reader are not enough, an <code>EOFException</code> occurs.
	 *
	 * @param reader the reader from which to read the value from
	 * @return this
	 * @throws IOException if an I/O error occurs
	 */
	public CharArray readFrom(Reader reader) throws IOException {
		int end = off + len;
		int begin = off;
		int s = -1;
		while (begin < end && (s = reader.read(value, begin, len)) >= 0)
			begin += s;
		if (begin < end)
			throw new java.io.EOFException();
		return this;
	}

	/**
	 * Clone a copy of the {@link CharArray}. Clone has the same values in specified
	 * sequence while changes might happen on {@link #value} and {@link #offset()}
	 * when only the specified range in the {@link #value} is copied.
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public CharArray clone() {
		return new CharArray(toCharArray());
	}

	/**
	 * Compares two char arrays lexicographically. <br>
	 * The comparison is based on the Unicode value of each character in the char
	 * arrays. The character sequence represented by this {@code CharArray} object
	 * is compared lexicographically to the character sequence represented by the
	 * argument string. The result is a negative integer if this {@code CharArray}
	 * object lexicographically precedes the argument char array. The result is a
	 * positive integer if this {@code CharArray} object lexicographically follows
	 * the argument char array. The result is zero if the char arrays are equal;
	 * {@code compareTo} returns {@code 0} exactly when the {@link #equals(Object)}
	 * method would return {@code true}.
	 * <p>
	 * This is the definition of lexicographic ordering. If two char arrays are
	 * different, then either they have different characters at some index that is a
	 * valid index for both strings, or their lengths are different, or both. If
	 * they have different characters at one or more index positions, let <i>k</i>
	 * be the smallest such index; then the string whose character at position
	 * <i>k</i> has the smaller value, as determined by using the &lt; operator,
	 * lexicographically precedes the other string. In this case, {@code compareTo}
	 * returns the difference of the two character values at position {@code k} in
	 * the two string -- that is, the value: <blockquote>
	 * 
	 * <pre>
	 * this.charAt(k) - anotherString.charAt(k)
	 * </pre>
	 * 
	 * </blockquote> If there is no index position at which they differ, then the
	 * shorter string lexicographically precedes the longer string. In this case,
	 * {@code compareTo} returns the difference of the lengths of the strings --
	 * that is, the value: <blockquote>
	 * 
	 * <pre>
	 * this.length() - other.length()
	 * </pre>
	 * 
	 * </blockquote>
	 *
	 * @param other the {@code CharArray} to be compared.
	 * @return the value {@code 0} if the argument string is equal to this char
	 *         array; a value less than {@code 0} if this char array is
	 *         lexicographically less than the char array argument; and a value
	 *         greater than {@code 0} if this char array is lexicographically
	 *         greater than the char array argument.
	 */
	@Override
	public int compareTo(CharArray other) {
		int lim = Math.min(this.len, other.len);
		for (int k = 0; k < lim; k++) {
			char c1 = this.value[k + this.off];
			char c2 = other.value[k + other.off];
			if (c1 != c2)
				return c1 - c2;
		}
		return this.len - other.len;
	}

	/**
	 * Compares this char array to the specified {@code CharSequence}. The result is
	 * {@code true} if and only if this {@code CharArray} represents the same
	 * sequence of char values as the specified sequence.
	 *
	 * @param cs The sequence to compare this {@code CharArray} against
	 * @return {@code true} if this {@code CharArray} represents the same sequence
	 *         of char values as the specified sequence, {@code false} otherwise
	 */
	public boolean contentEquals(CharSequence cs) {
		// Argument is a CharArray
		if (cs.getClass() == getClass())
			return equals(cs);
		if (len != cs.length())
			return false;
		for (int i = 0; i < len; i++)
			if (value[i + off] != cs.charAt(i))
				return false;
		return true;
	}

	/**
	 * Compare this with an {@link Object} and they are equal only when there
	 * classes are same and they are match on the managed region in array.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (null == obj || obj.getClass() != getClass())
			return false;
		CharArray other = (CharArray) obj;
		if (other.len != this.len)
			return false;
		for (int i = 0; i < len; i++)
			if (this.value[i + this.off] != other.value[i + other.off])
				return false;
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int h = 0;
		for (int i = 0; i < len; i++)
			h = 31 * h + value[i];
		return h;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// Create a copy, don't share the array
		return new String(value, off, len);
	}

	/**
	 * Returns the serialized object of the array to resolve the read and write
	 * method.
	 * 
	 * @return the serialized object
	 */
	private Object writeReplace() {
		return new SerialData(this);
	}

	/**
	 * Serialize this char array. Changes might happen on {@link #value} and
	 * {@link #offset()} when only the specified range in the {@link #value} is
	 * serialized.
	 */
	private static final long serialVersionUID = 4794942912535853845L;

	/**
	 * To serialize or deserialize a {@link CharArray}
	 * 
	 * @author XuYanhang
	 *
	 */
	private static class SerialData implements java.io.Externalizable {

		/**
		 * Serialize the char array.
		 */
		private static final long serialVersionUID = -6056865473256948403L;

		/**
		 * Char array to serialize
		 */
		transient CharArray array;

		/**
		 * Initialize the data
		 * 
		 * @param array the {@link CharArray} to write
		 */
		SerialData(CharArray array) {
			super();
			this.array = array;
		}

		/**
		 * Save the state of the {@link CharArray} instance to a stream (that is,
		 * serialize it).
		 *
		 * @serialData the length value and only the ranged character while the extra
		 *             char parts out the range ignored.
		 */
		@Override
		public void writeExternal(java.io.ObjectOutput out) throws IOException {
			out.writeInt(array.len);
			int end = array.off + array.len;
			for (int p = array.off; p < end; p++)
				out.writeChar(array.value[p]);
		}

		/**
		 * The readObject is called to restore the state of the {@link CharArray} from a
		 * stream.
		 */
		@Override
		public void readExternal(java.io.ObjectInput in) throws IOException {
			int len = in.readInt();
			char[] value = new char[len];
			for (int i = 0; i < len; i++)
				value[i] = in.readChar();
			array = new CharArray(value);
		}

		/**
		 * Resolve the read data from {@link #readExternal(java.io.ObjectInput)}.
		 */
		Object readResolve() {
			return array;
		}

	}

}
