package org.xuyh.graph;

/**
 * 
 * @author XuYanhang
 *
 */
public final class WeightLine implements java.io.Serializable {
	public static WeightLine of(Point p1, Point p2) {
		return of(p1, p2, 1);
	}

	public static WeightLine of(Point p1, Point p2, int w) {
		if ((p1.compareTo(p2) ^ p2.compareTo(p1)) >= 0)
			throw new IllegalArgumentException("Illegal compare");
		return new WeightLine(p1, p2, w);
	}

	public final Point p1;
	public final Point p2;
	public final int w;

	private WeightLine(Point p1, Point p2, int w) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.w = w;
	}

	/**
	 * Reverse the line direction.
	 *
	 * @return a reversed line of (p2,p1)
	 */
	public WeightLine reverse() {
		return new WeightLine(p2, p1, w);
	}

	/**
	 * Keep the line vertexes same while the direction from a little one to another.
	 *
	 * @return a absolute direction line with <code>p1&lt;=p2</code>
	 */
	public WeightLine abs() {
		return p1.compareTo(p2) <= 0 ? this : reverse();
	}

	@Override
	public int hashCode() {
		return (((p1.hashCode() * 31) ^ p2.hashCode()) * 31) ^ w;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeightLine o = (WeightLine) obj;
		return p1.equals(o.p1) && p2.equals(o.p2) && w == o.w;
	}

	@Override
	public String toString() {
		return "Line{" + p1 + "->" + p2 + "," + w + "}";
	}

	private static final long serialVersionUID = -7534403020529926098L;
}
