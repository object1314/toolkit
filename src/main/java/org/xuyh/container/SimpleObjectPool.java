/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.container;

import java.util.function.Supplier;

/**
 * This is a simple and concurrent safe but non-strict {@link ObjectPool}, while
 * implements all the optional methods from {@link ObjectPool}. The
 * {@link SimpleObjectPool} provides a quick way to manage the objects in the
 * pool by {@link LinkedStack} so that the operated elements are always newest
 * ones.
 *
 * @param <T> Generic type
 * @author XuYanhang
 * @since 2020-08-15
 */
public class SimpleObjectPool<T> implements ObjectPool<T> {
    /**
     * Factory to produce objects
     */
    private final Supplier<T> objectFactory;

    /**
     * Container to manage these objects
     */
    private final LinkedStack<T> manager;

    /**
     * Initialize an instance of the {@link SimpleObjectPool}.
     *
     * @param objectFactory factory to produce objects, <code>null</code> value not
     *                      allowed
     * @param capacity      capacity of the pool, negative value rejected and the
     *                      real capacity value of the pool might be different from
     *                      the parameter
     * @throws NullPointerException     when the input objectFactory value is
     *                                  <code>null</code>
     * @throws IllegalArgumentException when the input capacity is a negative value
     */
    public SimpleObjectPool(Supplier<T> objectFactory, int capacity) {
        super();
        if (null == objectFactory) throw new NullPointerException("objectFactory");
        if (capacity < 0) throw new IllegalArgumentException("capacity");
        this.objectFactory = objectFactory;
        this.manager = new LinkedStack<>(capacity);
    }

    /**
     * Borrow an object from the pool. The result will be first fetch from the idle
     * objects of the pool. If a target fetched, then remove it from idle setting
     * and return it to caller. While nothing get(no idle objects) then directly
     * create a new object by the {@link #objectFactory} and return it to caller.
     *
     * @return the borrowed object in the pool
     */
    @Override
    public T borrowObject() {
        T obj = manager.pop();
        return null == obj ? objectFactory.get() : obj;
    }

    /**
     * Return an object into the pool. The operation succeeds only when the object
     * is not <code>null</code> and the pool is not full. If it fails, nothing will
     * happen.
     * <p>
     * For a {@link SimpleObjectPool}, the return operation is unnecessary when
     * needn't reuse it anymore.
     *
     * @param obj the object to return into the pool
     */
    @Override
    public void returnObject(T obj) {
        if (null != obj) manager.push(obj);
    }

    /**
     * Invalidate an object of the pool. For this pool, no action needs when the
     * {@link #returnObject(Object)} never use for it.
     *
     * @param obj the object to invalidate
     * @see #returnObject(Object)
     */
    public void invalidateObject(T obj) {
    }

    /**
     * Returns capacity of the pool.
     *
     * @return the pool capacity
     */
    @Override
    public int readPoolCapacity() {
        return (int) manager.capacity();
    }

    /**
     * Returns the amount of the idle objects in the pool.
     *
     * @return the amount of the idle objects in the pool
     */
    @Override
    public int readIdleObjectAmount() {
        return (int) manager.size();
    }

    /**
     * Clear the objects in the pool.
     */
    @Override
    public void clear() {
        manager.clear();
    }

    /**
     * Close the pool just by clear the objects.
     *
     * @see #clear()
     */
    @Override
    public void close() {
        clear();
    }

    /**
     * For GC.
     */
    @Override
    protected void finalize() throws Throwable {
        manager.clear();
        super.finalize();
    }
}
