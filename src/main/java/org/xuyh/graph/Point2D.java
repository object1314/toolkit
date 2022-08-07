/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.graph;

/**
 * Point in two dimension with two coordinates
 *
 * @author XuYanhang
 * @since 2022-08-15
 *
 */
public final class Point2D extends PointSwapper {

	/**
	 * The X coordinate of this <code>Point</code>
	 */
	public final int x;

	/**
	 * The Y coordinate of this <code>Point</code>
	 */
	public final int y;

	/**
	 * Build this point in specified coordinates
	 *
	 * @param x The X coordinate to set
	 * @param y The Y coordinate to set
	 */
	public Point2D(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public int[] toArray() {
		return new int[] { x, y };
	}

	@Override
	public int compareTo(Point o) {
		int c = 0;
		if (o instanceof Point2D) {
			c = Integer.compare(x, ((Point2D)o).x);
			if (c != 0) return c;
			return Integer.compare(y, ((Point2D)o).y);
		}
		return super.compareTo(o);
	}

	@Override
	public int hashCode() {
		return x ^ (y * 31);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return super.equals(obj);
		Point2D other = (Point2D) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "Point(" + x + "," + y + ")";
	}

	private static final long serialVersionUID = -2645355037032533193L;
}
