/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.container;

/**
 * An {@link ObjectPool} is a pool-resource to manage a batch of objects. These
 * objects are parallel and reusable.
 * <p>
 * Besides, it mostly is safe in a concurrent environment. For a manage
 * {@link Thread} on the {@link ObjectPool}, the codes are mostly like this:
 *
 * <pre>
 *     try(ObjectPool pool = <i>initial_a_pool</i>;) {
 *         <i>// Other codes those might be in a concurrent environment</i>
 *     }
 * </pre>
 * <p>
 * For any use block, the codes are mostly like this:
 *
 * <pre>
 *     boolean isGood = true;
 *     Object obj = pool.borrowObject();
 *     try {
 *         <i>// Codes to do with the borrowed obj </i>
 *     } finally {
 *         if (isGood) {
 *             pool.returnObject(obj);
 *         } else {
 *             pool.invalidateObject(obj);
 *         }
 *     }
 * </pre>
 *
 * @param <T> Generic type
 * @author XuYanhang
 * @since 2020-08-15
 */
public interface ObjectPool<T> extends AutoCloseable {
    /**
     * Borrow an object from the pool. An object can be used out of the pool only
     * when it is borrowed.
     *
     * @return the borrowed object in the pool
     */
    T borrowObject();

    /**
     * Return an object into the pool. After the return action, the object is
     * expected to be unused out of the pool.
     *
     * @param obj the object to return into the pool
     */
    void returnObject(T obj);

    /**
     * Invalidate an object of the pool.
     *
     * @param obj the object to invalidate
     */
    void invalidateObject(T obj);

    /**
     * Returns capacity of the pool.
     *
     * @return the pool capacity
     */
    int readPoolCapacity();

    /**
     * Returns the amount of the idle objects in the pool.
     *
     * @return the amount of the idle objects in the pool
     */
    int readIdleObjectAmount();

    /**
     * Clear all pooled objects in the pool.
     */
    void clear();

    /**
     * Close this object pool so that all of the resources are released.
     *
     * @throws Exception if this pool failed close
     * @see AutoCloseable#close()
     */
    @Override
    void close() throws Exception;
}
