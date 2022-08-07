/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.graph;

/**
 * Point in zero dimension with no coordinate
 *
 * @author XuYanhang
 * @since 2022-08-15
 *
 */
public final class Point0D extends PointSwapper {

	/**
	 * Singleton instance for this point type
	 */
	public static Point0D INSTANCE = new Point0D();

	/**
	 * Value return on {@link toArray}
	 */
	private final int[] arr = new int[0];

	/**
	 * Build this point in specified coordinates
	 */
	private Point0D() {
		super();
	}

	@Override
	public int[] toArray() {
		return arr;
	}

	@Override
	public int compareTo(Point o) {
		int[] arr = o.toArray();
		for (int i = 0; i < arr.length; ++i)
			if (arr[i] != 0)
				return arr[i] > 0 ? -1 : 1;
		return 0;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		return getClass() == obj.getClass() ? true : super.equals(obj);
	}

	@Override
	public String toString() {
		return "Point()";
	}

	private static final long serialVersionUID = 4801896294429175495L;
}
