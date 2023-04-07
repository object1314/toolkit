/*
 * Copyright (c) 2020-2023 XuYanhang
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
 * @param <E> Generic type
 * @author XuYanhang
 * @since 2020-08-14
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
    long capacity();

    /**
     * Peek the top element in the stack.
     *
     * @return the top element
     */
    E peek();

    /**
     * Push an element on the top of the stack.
     *
     * @param e the element to push
     */
    void push(E e);

    /**
     * Pop the top element in the stack.
     *
     * @return the popped top element
     */
    E pop();

    /**
     * Remove the top element in the stack.
     */
    void remove();

    /**
     * Search an element in the stack.
     *
     * @param e the element to search
     * @return the position of the target element
     */
    long search(E e);

    /**
     * Clear the stack. After the clear action, the stack should be empty.
     */
    void clear();

    /**
     * Measure the size of the stack.
     *
     * @return the size of the stack
     */
    long size();

    /**
     * Measure if the stack is empty.
     *
     * @return <code>true</code> if the stack is empty or <code>false</code> if not
     */
    boolean empty();

    /**
     * Returns an iterator over elements in the stack.
     *
     * @return an {@link Iterator}
     */
    Iterator<E> iterator();

    /**
     * Returns an array containing all the elements in this stack.
     *
     * @return an array containing all the elements in this stack
     */
    <T> T[] toArray(Class<? extends T> componentType);
}
