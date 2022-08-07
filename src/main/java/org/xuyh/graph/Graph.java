/*
 * Copyright (c) 2022-2022 XuYanhang
 * 
 */
package org.xuyh.graph;

import java.util.List;

/**
 * Graph is a complex data structure, composed of vertexes and edges, as G(V,E).
 * V(G) is a finite collection with vertexes, and E(G) is a finite collection
 * with edges. E(G) maybe empty.
 * 
 * @author XuYanhang
 * @since 2022-08-15
 *
 */
public interface Graph extends java.io.Serializable, Cloneable {

	/**
	 * Construct method on put a vertex, and returns if put finish. It maybe failed
	 * when it has a same vertex.
	 *
	 * @param p vertex to put, a point who can't be <code>null</code>
	 * @return <code>true</code> as success and <code>false</code> as failed
	 */
	public boolean putVertex(Point p);

	/**
	 * Construct method on remove a vertex, and returns if finish. It maybe failed
	 * when it has no such a vertex.
	 *
	 * @param p vertex to remove, a point who can't be <code>null</code>
	 * @return <code>true</code> as success and <code>false</code> as failed
	 */
	public boolean delVertex(Point p);

	/**
	 * Construct method on put an edge, and returns if put finish. It maybe failed
	 * when it has a same edge.
	 * <p>
	 * For a Digraph, <code>E(p1, p2)</code> and <code>E(p2, p1)</code> are not same
	 * while they are same for an Undigraph.
	 * <p>
	 * For a weight graph, the weight is default <code>1</code> and for an un-weight
	 * graph, the weight is forever <code>1</code>.
	 *
	 * @param p1 edge start, a point who can't be <code>null</code>
	 * @param p2 edge target, a point who can't be <code>null</code>
	 * @return <code>true</code> as success and <code>false</code> as failed
	 */
	public boolean putEdge(Point p1, Point p2);

	/**
	 * Construct method on put an edge with specified weight, and returns if put
	 * finish. It maybe failed when it has a same edge. Notices that if the edge
	 * exists, no changed happen including the edge weight.
	 * <p>
	 * For a Digraph, <code>E(p1, p2)</code> and <code>E(p2, p1)</code> are not same
	 * while they are same for an Undigraph.
	 * <p>
	 * For a weight graph, the weight is default <code>1</code> and for an un-weight
	 * graph, the weight is forever <code>1</code>.
	 *
	 * @param p1 edge start, a point who can't be <code>null</code>
	 * @param p2 edge target, a point who can't be <code>null</code>
	 * @param w edge weight, a positive integer value
	 * @return <code>true</code> as success and <code>false</code> as failed
	 */
	public boolean putEdge(Point p1, Point p2, int w);

	/**
	 * Construct method on reset an edge with specified weight, and returns if put
	 * finish. It maybe failed when the graph has no such an edge.
	 * <p>
	 * For a Digraph, <code>E(p1, p2)</code> and <code>E(p2, p1)</code> are not same
	 * while they are same for an Undigraph.
	 * <p>
	 * For a weight graph, the weight is default <code>1</code> and for an un-weight
	 * graph, the weight is forever <code>1</code>.
	 *
	 * @param p1 edge start, a point who can't be <code>null</code>
	 * @param p2 edge target, a point who can't be <code>null</code>
	 * @param w edge weight, a positive integer value
	 * @return <code>true</code> as success and <code>false</code> as failed
	 */
	public boolean resetWeight(Point p1, Point p2, int w);

	/**
	 * Construct method on remove an edge with specified weight, and returns if
	 * finish. It maybe failed when the graph has no such an edge.
	 * <p>
	 * For a Digraph, <code>E(p1, p2)</code> and <code>E(p2, p1)</code> are not same
	 * while they are same for an Undigraph.
	 * <p>
	 * For a weight graph, the weight is default <code>1</code> and for an un-weight
	 * graph, the weight is forever <code>1</code>.
	 *
	 * @param p1 edge start, a point who can't be <code>null</code>
	 * @param p2 edge target, a point who can't be <code>null</code>
	 * @return <code>true</code> as success and <code>false</code> as failed
	 */
	public boolean delEdge(Point p1, Point p2);

	/**
	 * Returns the size of the vertexes.
	 *
	 * @return size of vertexes
	 */
	public int vertexSize();

	/**
	 * Returns the size of the edges.
	 *
	 * @return size of edges
	 */
	public int edgeSize();

	/**
	 * Checks if the graph contains the vertex.
	 *
	 * @param p vertex to search, a point who can't be <code>null</code>
	 * @return <code>true</code> as found and <code>false</code> as not found
	 */
	public boolean containsVertex(Point p);

	/**
	 * Checks if the graph contains the edge.
	 *
	 * @param p1 edge start, a point who can't be <code>null</code>
	 * @param p2 edge target, a point who can't be <code>null</code>
	 * @return <code>true</code> as found and <code>false</code> as not found
	 */
	public boolean containsEdge(Point p1, Point p2);

	/**
	 * Returns the edge of the given two vertex, or <code>null</code> if not exists.
	 *
	 * @param p1 edge start, a point who can't be <code>null</code>
	 * @param p2 edge target, a point who can't be <code>null</code>
	 * @return the specified edge or <code>null</code> if not exists.
	 */
	public WeightLine getEdge(Point p1, Point p2);

	/**
	 * Returns all edges on the vertex. For a Digraph, if contains all edges start
	 * at the vertex and target at the vertex. But for a digraph, all edges are
	 * same.
	 *
	 * @param p the vertex to list from
	 * @return edges on the vertex, all <code>null</code> if the vertex not exists
	 */
	public List<WeightLine> edgesOnVertex(Point p);

	/**
	 * Returns all edges start from the vertex. For a Digraph, if contains all edges
	 * start at the vertex. But for a digraph, all edges are same.
	 *
	 * @param p the vertex to list from
	 * @return edges from the vertex, all <code>null</code> if the vertex not exists
	 */
	public List<WeightLine> edgesFromVertex(Point p);

	/**
	 * Returns all edges target to the vertex. For a Digraph, if contains all edges
	 * target at the vertex. But for a digraph, all edges are same.
	 *
	 * @param p the vertex to list from
	 * @return edges to the vertex, all <code>null</code> if the vertex not exists
	 */
	public List<WeightLine> edgeToVertex(Point p);

	/**
	 * List all vertexes on this graph.
	 *
	 * @return all vertexes on this graph.
	 */
	public List<Point> listVertexes();

	/**
	 * List all edges on this graph.
	 *
	 * @return all edges on this graph.
	 */
	public List<WeightLine> listEdges();

	/**
	 * Breadth-First-Search for a path from start vertex to end vertex.
	 *
	 * @param p1 start position, a vertex expected exist in the graph
	 * @param p2 target position, a vertex expected exist in the graph
	 * @return the path from start position(excluded) to target position(included)
	 *         in turn. Maybe empty when the p1 and p2 are same. Maybe <code>null
	 *         </code> when the path not exists.
	 */
	public List<WeightLine> bfs(Point p1, Point p2);

	/**
	 * Depth-First-Search for a path from start vertex to end vertex.
	 *
	 * @param p1 start position, a vertex expected exist in the graph
	 * @param p2 target position, a vertex expected exist in the graph
	 * @return the path from start position(excluded) to target position(included)
	 *         in turn. Maybe empty when the p1 and p2 are same. Maybe <code>null
	 *         </code> when the path not exists.
	 */
	public List<WeightLine> dfs(Point p1, Point p2);

	/**
	 * Breadth-First-Search for a path from start vertex to other vertexes in
	 * specified steps and list these reachable vertexes.
	 *
	 * @param p start vertex, a vertex expected exist in the graph
	 * @param steps steps to search the target
	 * @return the path from start position(excluded) to target position(included)
	 *         in turn. Maybe empty when the p1 and p2 are same, or can't reach in steps.
	 *         Maybe <code>null </code> when the path not exists.
	 */
	public List<Point> listReachableInSteps(Point p, int steps);

	/**
	 * Breadth-First-Search for a path from other vertexes to target vertex in
	 * specified steps and list these inverse reachable vertexes.
	 *
	 * @param p start vertex, a vertex expected exist in the graph
	 * @param steps steps to search the target
	 * @return the path from start position(excluded) to target position(included)
	 *         in turn. Maybe empty when the p1 and p2 are same, or can't reach in steps.
	 *         Maybe <code>null </code> when the path not exists.
	 */
	public List<Point> listInverseReachableInSteps(Point p, int steps);
}
