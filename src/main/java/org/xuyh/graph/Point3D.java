/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.graph;

/**
 * Point in three dimension with three coordinates
 *
 * @author XuYanhang
 * @since 2022-08-15
 *
 */
public final class Point3D extends PointSwapper {

	/**
	 * The X coordinate of this <code>Point</code>
	 */
	public final int x;

	/**
	 * The Y coordinate of this <code>Point</code>
	 */
	public final int y;

	/**
	 * The Z coordinate of this <code>Point</code>
	 */
	public final int z;

	/**
	 * Build this point in specified coordinates
	 *
	 * @param x The X coordinate to set
	 * @param y The Y coordinate to set
	 * @param z The Z coordinate to set
	 */
	public Point3D(int x, int y, int z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public int[] toArray() {
		return new int[] { x, y, z };
	}

	@Override
	public int compareTo(Point o) {
		int c = 0;
		if (o instanceof Point3D) {
			c = Integer.compare(x, ((Point3D)o).x);
			if (c != 0) return c;
			c = Integer.compare(y, ((Point3D)o).y);
			if (c != 0) return c;
			return Integer.compare(z, ((Point3D)o).z);
		}
		return super.compareTo(o);
	}

	@Override
	public int hashCode() {
		return x ^ ((y ^ (z * 31)) * 31);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return super.equals(obj);
		Point3D other = (Point3D) obj;
		return x == other.x && y == other.y && z == other.z;
	}

	@Override
	public String toString() {
		return "Point(" + x + "," + y + "," + z + ")";
	}

	private static final long serialVersionUID = -2645355037032533193L;
}
