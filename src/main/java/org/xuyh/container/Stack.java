/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.container;

import java.util.Iterator;

/**
 * A stack is a container for some elements. It obeys the rule of FILO(First In
 * Last Out). Any newer element is toper than the older one and only the top
 * element of a stack could be operated.
 * <p>
 * The position of the element in a stack can be measured by height. The height
 * value regions from <code>one</code> to {@link #size()}, where the bottom
 * element measured by height of <code>one</code> while the top one measured of
 * {@link #size()}.
 * 
 * @author XuYanhang
 * @since 2020-08-14
 *
 * @param <E> Generic type
 */
public interface Stack<E> extends Iterable<E> {

	/**
	 * Returns the capacity of this stack. The result below for a {@link Stack
	 * stack} should always be <code>true</code>:
	 * 
	 * <pre>
	 *     stack.capacity() &gt;= stack.size();
	 * </pre>
	 * 
	 * @return the capacity of the stack
	 */
	public long capacity();

	/**
	 * Peek the top element in the stack.
	 * 
	 * @return the top element
	 */
	public E peek();

	/**
	 * Push an element on the top of the stack.
	 * 
	 * @param e the element to push
	 */
	public void push(E e);

	/**
	 * Pop the top element in the stack.
	 * 
	 * @return the popped top element
	 */
	public E pop();

	/**
	 * Remove the top element in the stack.
	 */
	public void remove();

	/**
	 * Search an element in the stack.
	 * 
	 * @param e the element to search
	 * @return the position of the target element
	 */
	public long search(E e);

	/**
	 * Clear the stack. After the clear action, the stack should be empty.
	 */
	public void clear();

	/**
	 * Measure the size of the stack.
	 * 
	 * @return the size of the stack
	 */
	public long size();

	/**
	 * Measure if the stack is empty.
	 * 
	 * @return <code>true</code> if the stack is empty or <code>false</code> if not
	 */
	public boolean empty();

	/**
	 * Returns an iterator over elements in the stack.
	 *
	 * @return an {@link Iterator}
	 */
	public Iterator<E> iterator();

	/**
	 * Returns an array containing all of the elements in this stack.
	 * 
	 * @return an array containing all of the elements in this stack
	 */
	public <T> T[] toArray(Class<? extends T> componentType);

}
