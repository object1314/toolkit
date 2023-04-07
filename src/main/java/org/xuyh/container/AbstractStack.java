/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.container;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class provides a skeletal implementation of the {@link Stack} interface
 * to minimize the effort required to implement this interface.
 * <p>
 * To implement a {@link Stack}, programmers should implement these methods
 * below on extending this class:<br>
 * {@link #capacity()}<br>
 * {@link #push(Object)}<br>
 * {@link #pop()}<br>
 * {@link #iterator()}
 * <p>
 * Besides, override on these methods is necessary when the subclass have a
 * simple or atomic way:<br>
 * {@link #capacity()}<br>
 * {@link #search(Object)}<br>
 * {@link #clear()}
 * <p>
 * Of course, other overrides can be applied when there are better ways for
 * sub-class.
 *
 * @param <E> Generic type
 * @author XuYanhang
 * @since 2020-08-14
 */
public abstract class AbstractStack<E> implements Stack<E> {
    /**
     * Sole constructor. (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected AbstractStack() {
        super();
    }

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
    @Override
    public abstract long capacity();

    /**
     * Push an element on the top of the stack.
     *
     * @param e the element to push
     */
    @Override
    public abstract void push(E e);

    /**
     * Pop the top element in the stack.
     *
     * @return the popped top element
     */
    @Override
    public abstract E pop();

    /**
     * Returns an iterator over elements in the stack.
     *
     * @return an {@link Iterator}
     */
    @Override
    public abstract Iterator<E> iterator();

    /**
     * Peek the top element in the stack. When the element not found, a
     * {@link NoSuchElementException} thrown. <code>null</code> value is expected.
     * There affords a way on iterator.
     *
     * @return the top element
     * @throws NoSuchElementException when the stack is empty.
     * @see #iterator()
     */
    @Override
    public E peek() {
        Iterator<E> ite = iterator();
        if (ite.hasNext())
            return ite.next();
        throw new NoSuchElementException();
    }

    /**
     * Remove the top element in the stack.
     *
     * @see #pop()
     */
    @Override
    public void remove() {
        pop();
    }

    /**
     * Search the first element in the stack from top. There affords a way on
     * iterator from top to bottom but the returned height value is calculated where
     * the for-each on this iterator is always full. The override is necessary when
     * a subclass needs a simple way.
     * <p>
     * Two elements are same when:<br>
     * 1. Two elements are both <code>null</code> value <br>
     * 2. The method of {@link #equals(Object) equals} return <code>true</code>
     *
     * @param e the element to search
     * @return the position by height of the target element in the stack from
     * <code>1</code> of bottom element to {@link #size()} of top element,
     * or <code>-1</code> if the target element is not found
     * @see #iterator()
     */
    @Override
    public long search(E e) {
        boolean found = false;
        Iterator<E> ite = iterator();
        while (ite.hasNext()) {
            E ele = ite.next();
            if (null == ele ? null == e : ele.equals(e)) {
                found = true;
                break;
            }
        }
        if (!found) {
            return -1;
        }
        long height = 1;
        while (ite.hasNext()) {
            ite.next();
            height++;
        }
        return height;
    }

    /**
     * Clear the stack. There affords a way by remove the top element one by one
     * until all the stack is empty.
     *
     * @see #empty()
     * @see #remove()
     */
    @Override
    public void clear() {
        while (!empty()) remove();
    }

    /**
     * Measure the size of the stack. There affords a complex way to implement it.
     * The override is necessary when the subclass maybe need a large capacity and
     * have a simple implement way.
     *
     * @return the size of the stack
     * @see #iterator()
     */
    @Override
    public long size() {
        long size = 0;
        for (E ignored : this)
            size++;
        return size;
    }

    /**
     * Measure if the stack is empty, or rather if the size of the stack is 0.
     *
     * @return <code>true</code> if the stack is empty or <code>false</code> if not
     * @see #size()
     */
    @Override
    public boolean empty() {
        return iterator().hasNext();
    }

    /**
     * The maximum size of array to allocate. Some VMs reserve some header words in
     * an array. Attempts to allocate larger arrays may result in OutOfMemoryError:
     * Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Returns an array containing all the elements in this stack. The array is
     * in order of the stack from bottom element to top one.
     *
     * @param componentType the element type in the array, who are expected to be a
     *                      super-type of all elements in the stack
     * @return an array containing all the elements in this stack
     * @throws ArrayStoreException if the runtime type of the specified array is not
     *                             a super-type of the runtime type of every element
     *                             in this list
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(Class<? extends T> componentType) {
        Class<?> ctype = null == componentType ? Object.class : componentType;
        int size = (int) Long.min(MAX_ARRAY_SIZE, size());
        Iterator<E> ite = iterator();
        T[] array = (T[]) newArray(ctype, size);
        int cursor = size - 1;
        do {
            for (; cursor >= 0 && ite.hasNext(); cursor--)
                array[cursor] = (T) ite.next();
            if (!ite.hasNext()) break;
            // The result array is smaller than expected
            if (size == MAX_ARRAY_SIZE) break;
            size = size < 8 ? 8 : (size + (size >> 1));
            if (size < 0 || size > MAX_ARRAY_SIZE)
                size = MAX_ARRAY_SIZE;
            cursor = size - array.length - 1;
            T[] newArray = (T[]) newArray(ctype, size);
            System.arraycopy(array, 0, newArray, cursor + 1, array.length);
            array = newArray;
        } while (true);
        // The result array is larger than expected
        if (cursor >= 0) {
            T[] newArray = (T[]) newArray(ctype, array.length - cursor - 1);
            System.arraycopy(array, cursor + 1, newArray, 0, newArray.length);
            array = newArray;
        }
        return array;
    }

    /**
     * Make new array in a component type and size.
     *
     * @param ctype component type of the array, <code>null</code> check action
     *              should be done before this method
     * @param size  size of the array, region check action should be done before
     *              this method
     * @return the new array of the component type and size
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] newArray(Class<? extends T> ctype, int size) {
        return ctype == Object.class ? (T[]) new Object[size] : (T[]) java.lang.reflect.Array.newInstance(ctype, size);
    }

    /**
     * Compares the specified object with this list for equality. Returns
     * <tt>true</tt> if and only if the specified object is also a stack, both
     * stacks contain the same elements in the same order.
     *
     * @param obj the object to be compared for equality with this stack
     * @return <tt>true</tt> if the specified object is equal to this stack
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Stack)) return false;
        Iterator<E> tIt = iterator();
        Iterator<?> oIt = ((Stack<?>) obj).iterator();
        while (tIt.hasNext() && oIt.hasNext()) {
            E te = tIt.next();
            Object oe = oIt.next();
            if (!(null == te ? null == oe : te.equals(oe))) return false;
        }
        return !(tIt.hasNext() || oIt.hasNext());
    }

    /**
     * Returns the hash code value of this stack. The hash code is defined according
     * to the elements in the stack, so that when two stacks are equal, that there
     * hash code values are equal too.
     *
     * @return the hash code value for this stack
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        int h = 1;
        for (E e : this)
            h = 31 * h + (e == null ? 0 : e.hashCode());
        return h;
    }

    /**
     * Returns a string representation of this stack. The string representation
     * consists of a list of the stack's elements in the order they are returned by
     * its iterator, enclosed in square brackets (<tt>"[]"</tt>). Adjacent elements
     * are separated by the characters <tt>", "</tt> (comma and space). Elements are
     * converted to strings as by {@link String#valueOf(Object)}.
     * <p>
     * <b>CAUTION</b><br>
     * Some error might be thrown from this {@link #toString()} especially when:<br>
     * 1. There exist two or more objects refer to each other<br>
     * 2. The String needs too large space when the size of the stack is too large
     *
     * @return a string representation of this stack
     */
    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext()) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            E e = it.next();
            sb.append(e == this ? "(__self)" : e);
            if (!it.hasNext()) return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }
}
