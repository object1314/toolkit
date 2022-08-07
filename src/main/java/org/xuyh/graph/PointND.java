/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.graph;

/**
 * Point in N dimension with unknown coordinates
 *
 * @author XuYanhang
 * @since 2022-08-15
 *
 */
public final class PointND extends PointSwapper {

	/**
	 * The coordinates of this <code>Point</code>
	 */
	private final int[] arr;

	/**
	 * Build this point in specified coordinates
	 *
	 * @param arr The coordinates to set
	 */
	public PointND(int... arr) {
		super();
		this.arr = arr.clone();
	}

	@Override
	public int[] toArray() {
		return arr.clone();
	}

	private static final long serialVersionUID = 2916314353457540932L;
}
