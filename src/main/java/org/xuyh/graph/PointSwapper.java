/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.graph;

/**
 * Point swapper on implement some union methods
 *
 * @author XuYanhang
 * @since 2022-08-15
 */
public abstract class PointSwapper implements Point {

	@Override
	public abstract int[] toArray();

	@Override
	public int compareTo(Point o) {
		int[] ta = toArray();
		int[] oa = o.toArray();
		int len = Math.min(ta.length, oa.length);
		for (int cur = 0; cur < len; ++cur) {
			int c = Integer.compare(ta[cur], oa[cur]);
			if (c != 0) return c;
		}
		for (int cur = 0; cur < ta.length; ++cur) {
			if (ta[cur] != 0) return ta[cur] > 0 ? 1 : -1;
		}
		for (int cur = 0; cur < oa.length; ++cur) {
			if (oa[cur] != 0) return oa[cur] > 0 ? -1 : 1;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		int[] arr = toArray();
		int hash = 0;
		for (int i = arr.length - 1; i >= 0; --i)
			hash = (31 * hash) ^ arr[i];
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Point))
			return false;
		int[] ta = this.toArray();
		int[] oa = ((Point) obj).toArray();
		int len = Math.min(ta.length, oa.length);
		for (int cur = 0; cur < len; ++cur) {
			if (ta[cur] != oa[cur]) return false;
		}
		for (int cur = 0; cur < ta.length; ++cur) {
			if (ta[cur] != 0) return false;
		}
		for (int cur = 0; cur < oa.length; ++cur) {
			if (oa[cur] != 0) return false;
		}
		return true;
	}

	@Override
	public String toString() {
		int[] arr = toArray();
		if (arr.length == 0) return "Point()";
		StringBuilder sb = new StringBuilder("Point(");
		for (int i = 0; i < arr.length; ++i) {
			sb.append(arr[i]).append(',');
		}
		sb.setCharAt(sb.length() - 1, ')');
		return sb.toString();
	}

	private static final long serialVersionUID = -7800397433653166750L;
}
