/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Undirected graph
 *
 * @author XuYanhang
 * @since 2022-08-15
 *
 */
public class Digraph implements Graph {

	private final TreeMap<Point, Vertex> vertexes;

	/**
	 * Construct an empty Undigraph with no vertexes and no edges
	 */
	public Digraph() {
		super();
		this.vertexes = new TreeMap<>();
	}

	@Override
	public boolean putVertex(Point p) {
		if (vertexes.containsKey(p)) return false;
		vertexes.put(p, new Vertex(p));
		return true;
	}

	@Override
	public boolean delVertex(Point p) {
		Vertex vertex = vertexes.remove(p);
		if (vertex == null) return false;
		for (Point op : vertex.iedges.keySet())
			vertexes.get(op).delEdge(p);
		for (Point op : vertex.edges.keySet())
			vertexes.get(op).delIEdge(p);
		return false;
	}

	@Override
	public boolean putEdge(Point p1, Point p2) {
		return putEdge(p1, p2, 1);
	}

	@Override
	public boolean putEdge(Point p1, Point p2, int w) {
		Vertex v1 = vertexes.get(p1);
		Vertex v2 = vertexes.get(p2);
		if (v1 == v2 || v1 == null || v2 == null) return false;
		if (v1.existEdge(p2)) return false;
		v1.putEdge(p2, w);
		v2.putIEdge(p1, w);
		return true;
	}

	@Override
	public boolean resetWeight(Point p1, Point p2, int w) {
		Vertex v1 = vertexes.get(p1);
		Vertex v2 = vertexes.get(p2);
		if (v1 == v2 || v1 == null || v2 == null) return false;
		if (!v1.existEdge(p2)) return false;
		v1.putEdge(p2, w);
		v2.putIEdge(p1, w);
		return true;
	}

	@Override
	public boolean delEdge(Point p1, Point p2) {
		Vertex v1 = vertexes.get(p1);
		Vertex v2 = vertexes.get(p2);
		if (v1 == v2 || v1 == null || v2 == null) return false;
		if (!v1.existEdge(p2)) return false;
		v1.delEdge(p2);
		v2.delIEdge(p1);
		return true;
	}

	@Override
	public int vertexSize() {
		return vertexes.size();
	}

	@Override
	public int edgeSize() {
		int size = 0;
		for (Vertex v : vertexes.values())
			size += v.edges.size();
		return size;
	}

	@Override
	public boolean containsVertex(Point p) {
		return vertexes.containsKey(p);
	}

	@Override
	public boolean containsEdge(Point p1, Point p2) {
		return getEdge(p1, p2) != null;
	}

	@Override
	public WeightLine getEdge(Point p1, Point p2) {
		Vertex vertex = vertexes.get(p1);
		return vertex == null ? null : vertex.getEdge(p2);
	}

	@Override
	public List<WeightLine> edgesOnVertex(Point p) {
		Vertex vertex = vertexes.get(p);
		if (vertex == null) return null;
		ArrayList<WeightLine> lines = new ArrayList<>(vertex.edges.size() + vertex.iedges.size());
		lines.addAll(vertex.edges.values());
		lines.addAll(vertex.iedges.values());
		return lines;
	}

	@Override
	public List<WeightLine> edgesFromVertex(Point p) {
		Vertex vertex = vertexes.get(p);
		return vertex == null ? null : new ArrayList<>(vertex.edges.values());
	}

	@Override
	public List<WeightLine> edgeToVertex(Point p) {
		Vertex vertex = vertexes.get(p);
		return vertex == null ? null : new ArrayList<>(vertex.iedges.values());
	}

	@Override
	public List<Point> listVertexes() {
		return new ArrayList<>(vertexes.keySet());
	}

	@Override
	public List<WeightLine> listEdges() {
		ArrayList<WeightLine> edges = new ArrayList<>();
		for (Vertex v : vertexes.values()) {
			for (WeightLine e : v.edges.values()) {
				edges.add(e);
			}
		}
		return edges;
	}

	@Override
	public List<WeightLine> bfs(Point p1, Point p2) {
		Vertex v1 = vertexes.get(p1);
		Vertex v2 = vertexes.get(p2);
		if (v1 == null || v2 == null) return null;
		TreeMap<Point, WeightLine> pathMap = new TreeMap<>();
		pathMap.put(v1.point, null);
		LinkedList<Vertex> queue = new LinkedList<>();
		queue.offer(v1);
		while (!queue.isEmpty()) {
			Vertex cur = queue.poll();
			if (cur == v2) return castPath(v1.point, v2.point, pathMap);
			for (Map.Entry<Point, WeightLine> pl : cur.edges.entrySet()) {
				Point pn = pl.getKey();
				WeightLine ln = pl.getValue();
				if (pathMap.containsKey(pn)) continue;
				Vertex vn = vertexes.get(pn);
				pathMap.put(pn, ln);
				queue.offer(vn);
			}
		}
		return null;
	}

	@Override
	public List<WeightLine> dfs(Point p1, Point p2) {
		Vertex v1 = vertexes.get(p1);
		Vertex v2 = vertexes.get(p2);
		if (v1 == null || v2 == null) return null;
		TreeMap<Point, WeightLine> pathMap = new TreeMap<>();
		pathMap.put(v1.point, null);
		LinkedList<Vertex> stack = new LinkedList<>();
		stack.push(v1);
		while (!stack.isEmpty()) {
			Vertex cur = stack.poll();
			if (cur == v2) return castPath(v1.point, v2.point, pathMap);
			for (Map.Entry<Point, WeightLine> pl : cur.edges.entrySet()) {
				Point pn = pl.getKey();
				WeightLine ln = pl.getValue();
				if (pathMap.containsKey(pn)) {
					continue;
				}
				Vertex vn = vertexes.get(pn);
				pathMap.put(pn, ln);
				stack.push(vn);
			}
		}
		return null;
	}

	@Override
	public List<Point> listReachableInSteps(Point p, int steps) {
		Vertex v = vertexes.get(p);
		if (v == null || steps < 0) return null;
		TreeMap<Point, Integer> pathMap = new TreeMap<>();
		pathMap.put(v.point, 0);
		LinkedList<Vertex> queue = new LinkedList<>();
		queue.offer(v);
		while (!queue.isEmpty()) {
			Vertex cur = queue.poll();
			int step = pathMap.get(cur.point);
			if (step == steps) continue;
			for (Point pn : cur.edges.keySet()) {
				if (pathMap.containsKey(pn)) continue;
				Vertex vn = vertexes.get(pn);
				pathMap.put(pn, step + 1);
				queue.offer(vn);
			}
		}
		return new ArrayList<>(pathMap.keySet());
	}

	@Override
	public List<Point> listInverseReachableInSteps(Point p, int steps) {
		Vertex v = vertexes.get(p);
		if (v == null || steps < 0) return null;
		TreeMap<Point, Integer> pathMap = new TreeMap<>();
		pathMap.put(v.point, 0);
		LinkedList<Vertex> queue = new LinkedList<>();
		queue.offer(v);
		while (!queue.isEmpty()) {
			Vertex cur = queue.poll();
			int step = pathMap.get(cur.point);
			if (step == steps) continue;
			for (Point pn : cur.iedges.keySet()) {
				if (pathMap.containsKey(pn)) continue;
				Vertex vn = vertexes.get(pn);
				pathMap.put(pn, step + 1);
				queue.offer(vn);
			}
		}
		return new ArrayList<>(pathMap.keySet());
	}

	private List<WeightLine> castPath(Point p1, Point p2, TreeMap<Point, WeightLine> pathMap) {
		LinkedList<WeightLine> path = new LinkedList<>();
		for (Point cur = p2; cur != p1;) {
			WeightLine edge = pathMap.get(cur);
			path.push(edge);
			cur = edge.p1;
		}
		return path;
	}

	private static final long serialVersionUID = 3980520255885384554L;

	private static class Vertex implements java.io.Serializable {

		final Point point;
		final TreeMap<Point, WeightLine> edges;
		final TreeMap<Point, WeightLine> iedges;

		Vertex(Point p) {
			super();
			point = p;
			edges = new TreeMap<>();
			iedges = new TreeMap<>();
		}

		boolean existEdge(Point p) {
			return edges.containsKey(p);
		}

		WeightLine getEdge(Point p) {
			return edges.get(p);
		}

		void putEdge(Point p, int w) {
			edges.put(p, WeightLine.of(point, p, w));
		}

		void putIEdge(Point p, int w) {
			iedges.put(p, WeightLine.of(point, p, w));
		}

		void delEdge(Point p) {
			edges.remove(p);
		}

		void delIEdge(Point p) {
			iedges.remove(p);
		}

		private static final long serialVersionUID = -4478003332196186768L;
	}
}
