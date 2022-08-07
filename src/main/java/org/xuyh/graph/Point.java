/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.graph;

/**
 * Point in integer coordinates. It is comparable when a point is larger than
 * another only when the first different coordinate value at same coordinate index
 * is larger than another.
 *
 * @author XuYanhang
 * @since 2022-08-15
 */
public interface Point extends java.io.Serializable, Comparable<Point> {

	/**
	 * Construct a Point with specified coordinates array
	 *
	 * @param arr coordinates array
	 * @return a point on these coordinates
	 */
	public static Point of(int...arr) {
		switch(arr.length) {
		case 0: return Point0D.INSTANCE;
		case 1: return new Point1D(arr[0]);
		case 2: return new Point2D(arr[0], arr[1]);
		case 3: return new Point3D(arr[0], arr[1], arr[2]);
		default: return new PointND(arr);
		}
	}

	/**
	 * List the points in array functions.
	 * @return array format of the coordinate of this point.
	 */
	public int[] toArray();
}
