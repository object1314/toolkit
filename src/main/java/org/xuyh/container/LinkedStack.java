/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.container;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Linked stack implementation of the {@link Stack} interface. Implements the
 * basic optional stack operations, and permits all elements (including
 * {@code null}). Mention that the {@link #iterator() iterator} from this stack
 * who doesn't provide the {@link Iterator#remove() remove} implementation.
 * <p>
 * The stack is thread safe where any update operation is in a lock. Any query
 * operation is just a slice on an instant stack where the stack could be
 * changed in query method and the result won't change for any update operation
 * on the stack after this instant.
 * <p>
 * The exception throws as less than possible while some strategy will be adapted in
 * this stack. The check operation is necessary when programmers use this stack.
 *
 * @param <E> Generic type
 * @author XuYanhang
 * @see AbstractStack
 * @since 2020-08-14
 */
public class LinkedStack<E> extends AbstractStack<E> implements Stack<E>, java.io.Serializable {
    /**
     * The maximum capacity of a {@link LinkedStack stack} in this type.
     */
    public static final long MAX_CAPACITY = Integer.MAX_VALUE - 8;

    /**
     * An object lock on the operation of this stack.
     */
    private transient final Object lock = new Object();

    /**
     * Capacity of this stack region from <code>zero</code> to
     * {@link #MAX_CAPACITY}. The elements count in this stack will be never larger
     * that it. A stack in capacity of <code>zero</code> means an empty and
     * unchangeable one.
     */
    private final long capacity;

    /**
     * For a {@link LinkedStack stack}, A top node should be keep.
     */
    private transient volatile Node<E> top;

    /**
     * Create a stack in default capacity of {@link #MAX_CAPACITY}.
     */
    public LinkedStack() {
        this(MAX_CAPACITY);
    }

    /**
     * Create a stack in a give capacity. Any input capacity parameter is allowed
     * while it will be adjusted between <code>zero</code> and {@link #MAX_CAPACITY}.
     *
     * @param capacity the {@link #capacity} to initial
     */
    public LinkedStack(long capacity) {
        super();
        if (capacity <= 0L)
            this.capacity = 0L;
        else
            this.capacity = Long.min(capacity, MAX_CAPACITY);
    }

    /**
     * Returns the capacity of this stack. The result below for a {@link LinkedStack
     * stack} will always be <code>true</code>:
     *
     * <pre>
     *     stack.capacity() &gt;= stack.size();
     * </pre>
     *
     * @return the {@link #capacity}
     */
    public long capacity() {
        return capacity;
    }

    /**
     * Peek the top element in the stack. When the element not found, a
     * <code>null</code> value is expected.
     *
     * @return the top element or <code>null</code> if the stack is empty
     */
    @Override
    public E peek() {
        Node<E> t = this.top;
        return null == t ? null : t.element;
    }

    /**
     * Push an element on the top of the stack. In this stack strategy, a
     * <code>null</code> element is allowed. If the stack is full, the operation is
     * ignored and nothing happens here.
     *
     * @param e the element to push
     */
    @Override
    public void push(E e) {
        tryPush(e);
    }

    /**
     * Push an element on the top of the stack. In this stack strategy, a
     * <code>null</code> element is allowed. If the stack is full, the operation is
     * failed and a <code>false</code> value returned.
     *
     * @param e the element to push
     * @return <code>true</code> if the push action succeeds
     */
    public boolean tryPush(E e) {
        synchronized (lock) {
            if (size() < capacity) this.top = new Node<>(e, this.top);
            else return false;
        }
        return true;
    }

    /**
     * Pop the top element in the stack. If the stack is empty before, nothing will
     * happen.
     *
     * @return the popped top element or <code>null</code> if failed.
     */
    @Override
    public E pop() {
        Node<E> t = unlink();
        return null == t ? null : t.element;
    }

    /**
     * Remove the top element in the stack. If the stack is empty before, nothing
     * will happen.
     */
    @Override
    public void remove() {
        unlink();
    }

    private Node<E> unlink() {
        Node<E> t;
        synchronized (lock) {
            t = this.top;
            if (null != t) {
                this.top = t.below;
            }
        }
        return t;
    }

    /**
     * Search the first element in the stack from top.
     * <p>
     * Two elements are same when:<br>
     * 1. Two elements are both <code>null</code> value <br>
     * 2. The method of {@link #equals(Object) equals} return <code>true</code>
     *
     * @param e the element to search
     * @return the position by height of the target element in the stack from
     * <code>1</code> of bottom element to {@link #size()} of top element,
     * or <code>-1</code> if the target element is not found
     */
    @Override
    public long search(E e) {
        Node<E> cur = this.top;
        while (null != cur) {
            if (null == cur.element ? null == e : cur.element.equals(e)) {
                return cur.height;
            }
            cur = cur.below;
        }
        return -1;
    }

    /**
     * Search the first element in the stack from top.
     * <p>
     * If the input {@link Comparator comparator} parameter is a <code>null</code>
     * value, then the default {@link #search(Object) search} method will be used
     * instead. Otherwise, two elements are same only when a <code>zero</code> value
     * returned from the {@link Comparator#compare(Object, Object) compare} method.
     * However, any exception in the {@link Comparator#compare(Object, Object)
     * compare} won't be resolved in this {@link #search(Object, Comparator) search}
     * method.
     *
     * @param e the element to search
     * @param c the comparator
     * @return the position by height of the target element in the stack from
     * <code>1</code> of bottom element to {@link #size()} of top element,
     * or <code>-1</code> if the target element is not found
     * @see #search(Object)
     */
    public long search(E e, Comparator<E> c) {
        if (null == c)
            return search(e);
        Node<E> cur = this.top;
        while (null != cur) {
            if (c.compare(e, cur.element) == 0) {
                return cur.height;
            }
            cur = cur.below;
        }
        return -1;
    }

    /**
     * Clear the stack. After the clear action, the size of the stack is
     * <code>zero</code>.
     */
    @Override
    public void clear() {
        synchronized (lock) {
            this.top = null;
        }
    }

    /**
     * Measure the size of the stack. It's the height of the top element, or
     * <code>zero</code> if the stack is empty.
     *
     * @return the size of the stack
     * @see #empty()
     */
    @Override
    public long size() {
        Node<E> t = this.top;
        return null == t ? 0 : t.height;
    }

    /**
     * Measure if the stack is empty, or rather if the size of the stack is 0.
     *
     * @return <code>true</code> if the stack is empty or <code>false</code> if not
     * @see #size()
     */
    @Override
    public boolean empty() {
        return null == this.top;
    }

    /**
     * Returns an iterator over elements in the stack. The result iterator is just a
     * slice of an instant stack and only supply the query methods. After the
     * iterator is created, any update operation in the original {@link LinkedStack
     * stack} has no effect on it.
     *
     * @return an {@link Iterator} just for query
     */
    @Override
    public Iterator<E> iterator() {
        return new LSIte<>(this.top);
    }

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
        Node<E> cur = this.top;
        int size = null == cur ? 0 : cur.height;
        Class<?> ctype = null == componentType ? Object.class : componentType;
        T[] array = ctype == Object.class ? (T[]) new Object[size]
                : (T[]) java.lang.reflect.Array.newInstance(ctype, size);
        for (int i = size - 1; i >= 0; i--, cur = cur.below)
            array[i] = (T) cur.element;
        return array;
    }

    /**
     * Saves the state of this {@code LinkedStack} instance to a stream (that is,
     * serializes it).
     *
     * @serialData The capacity of the stack, the size of it and all of its elements
     * (each an Object) in the order from bottom to top
     */
    private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException {
        // Write out any hidden serialization magic and the capacity field
        oos.defaultWriteObject();

        // Write out size
        Object[] array = toArray(Object.class);
        int size = array.length;
        oos.writeLong(size);

        // Write out all elements in the proper order.
        for (Object o : array) oos.writeObject(o);
    }

    /**
     * Reconstitutes this {@code LinkedStack} instance from a stream (that is,
     * deserializes it).
     */
    private void readObject(java.io.ObjectInputStream ois) throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic and the capacity field
        ois.defaultReadObject();

        // Read in size
        long size = ois.readLong();

        // Read in all elements in the proper order
        for (long i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E e = (E) ois.readObject();
            this.top = new Node<E>(e, top);
        }
    }

    /**
     * To serialize or deserialize a {@link LinkedStack}.
     *
     * @see #writeObject(java.io.ObjectOutputStream)
     * @see #readObject(java.io.ObjectInputStream)
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = -3390145798796115268L;

    /**
     * @param <E>
     * @author XuYanhang
     */
    private static class Node<E> {
        final E element;
        final Node<E> below;
        final int height;

        /**
         * Initial a {@link Node}.
         *
         * @param element the {@link #element} to set
         * @param below   the {@link #below} to set, and {@link #height} initialize from
         *                it
         */
        Node(E element, Node<E> below) {
            super();
            this.element = element;
            this.below = below;
            this.height = (null == below) ? 1 : (below.height + 1);
        }
    }

    private static class LSIte<E> implements Iterator<E> {
        private Node<E> cur;

        /**
         * @param top first element
         */
        LSIte(Node<E> top) {
            super();
            this.cur = top;
        }

        @Override
        public boolean hasNext() {
            return null != cur;
        }

        @Override
        public E next() {
            Node<E> c = stepIfPossible();
            if (null == c)
                throw new java.util.NoSuchElementException("next");
            return c.element;
        }

        /**
         * Step to next node if possible and get the current valid node, or
         * <code>null</code> if the end node arrived.
         */
        private Node<E> stepIfPossible() {
            Node<E> c;
            if (null != (c = this.cur))
                this.cur = c.below;
            return c;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            if (null == action)
                throw new NullPointerException("action");
            Node<E> n;
            while ((n = stepIfPossible()) != null)
                action.accept(n.element);
        }
    }
}
