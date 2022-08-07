/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.graph;

/**
 * Point in one dimension with one coordinate
 *
 * @author XuYanhang
 * @since 2022-08-15
 *
 */
public final class Point1D extends PointSwapper {

	/**
	 * The X coordinate of this <code>Point</code>
	 */
	public final int x;

	/**
	 * Build this point in specified coordinates
	 *
	 * @param x The X coordinate to set
	 */
	public Point1D(int x) {
		super();
		this.x = x;
	}

	@Override
	public int[] toArray() {
		return new int[] { x };
	}

	@Override
	public int compareTo(Point o) {
		if (o instanceof Point1D)
			return Integer.compare(x, ((Point1D)o).x);
		return super.compareTo(o);
	}

	@Override
	public int hashCode() {
		return x;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return super.equals(obj);
		Point1D other = (Point1D) obj;
		return x == other.x;
	}

	@Override
	public String toString() {
		return "Point(" + x + ")";
	}

	private static final long serialVersionUID = 414926565182087794L;
}
