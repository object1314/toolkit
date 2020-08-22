/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.container;

/**
 * A singly linked node is a node in a singly linked list and wraps an object
 * value and refers to a next node or a nil node. This is an interface who
 * should be implemented by it's caller.
 * 
 * @author XuYanhang
 * @since 2020-08-22
 *
 * @param <T> Generic type
 */
public interface SinglyLinkedNode<T> {

	/**
	 * Returns the value this node wrap in it.
	 * 
	 * @return the value this node wrap in it
	 */
	public T getValue();

	/**
	 * Returns the next node this node refers to. The result might be a nil node
	 * where the {@link #isNil()} is <code>true</code> or a <code>null</code> value
	 * who is default defined as a nil node.
	 * 
	 * @return the next node this node refers to
	 */
	public SinglyLinkedNode<T> getNext();

	/**
	 * Returns if the node is a nil one. A nil node means the end in a list.
	 * 
	 * @return true when the node is a nil one or false when not
	 */
	public default boolean isNil() {
		return false;
	}

}
