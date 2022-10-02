/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.util;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.IntFunction;

/**
 * Toolkit on containers who is not supported but required in {@link java.util.Collections}
 *
 * @author XuYanhang
 * @since 2022-10-03
 * @see java.util.Collections
 */
public final class Collections2 {
	/**
	 * Uses a dynamic way to store elements by function instead of array way so that it has an almost no limit capacity
	 * but only a fixed small storage. It does well on that we know the general formula for each element and the count
	 * of the elements. In this way, the result list is expected to be {@link RandomAccess} and read only.
	 *
	 * @param size the size of the target list, must not negative
	 * @param formula the formula to generate element by index, must exist(not null), will accept index in valid range
	 */
	public static <E> List<E> formulaList(int size, IntFunction<E> formula) {
		if (size < 0) {
			throw new IllegalArgumentException();
		}
		Objects.requireNonNull(formula);
		return new RandomReadableList<>(size, formula);
	}

	/**
	 * Creates a {@link Integer} by a range of integers. For example, we will create a nonnegative integersequence by
	 * <pre>
	 * rangeList(0, Integer.MAX_VALUE, 1);
	 * </pre> 
	 *
	 * @param begin begin value as first element
	 * @param count count of the value to range, must not negative
	 * @param step increase step on begin
	 * @return a ranged List with {@link Integer}
	 */
	public static List<Integer> rangeList(int begin, int count, int step) {
		return formulaList(count, (index) -> begin + index * step);
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Collections2() {
		super();
	}

	/**
	 * List for {@link Collections2#formulaList(int, IntFunction)}
	 * @author XuYanhang
	 * @since 2022-10-03
	 *
	 * @param <E> generic type
	 */
	private static class RandomReadableList<E> extends AbstractList<E> implements List<E>, RandomAccess {
		private final int size;
		private final IntFunction<E> formula;

		RandomReadableList(int size, IntFunction<E> formula) {
			super();
			this.size = size;
			this.formula = formula;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public E get(int index) {
			if (index < 0 || index >= size) {
				throw new IndexOutOfBoundsException();
			}
			return formula.apply(index);
		}
	}
}
