/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.container;

import java.util.function.Supplier;

/**
 * This is only a factory to provide most used implementation of the
 * {@link ObjectPool} in this package.
 * <p>
 * Interface {@link ObjectPool}<br>
 * Implementation for a easy way {@link SimpleObjectPool}<br>
 * 
 * @author XuYanhang
 * @since 2020-08-15
 *
 */
public class ObjectPoolFactory {

	/**
	 * Build an {@link ObjectPool} in the way of {@link SimpleObjectPool}.
	 * 
	 * @param objectFactory factory to produce objects, <code>null</code> value not
	 *                      allowed
	 * @param capacity      capacity of the pool, negative value rejected and the
	 *                      real capacity value of the pool might be different from
	 *                      the parameter
	 * @throws NullPointerException     when the input objectFactory value is
	 *                                  <code>null</code>
	 * @throws IllegalArgumentException when the input capacity is a negative value
	 * @return the builded {@link ObjectPool}
	 */
	public static <T> ObjectPool<T> buildSimplePool(Supplier<T> objectFactory, int capacity) {
		return new SimpleObjectPool<>(objectFactory, capacity);
	}

	/**
	 * This factory should be open to outer caller.
	 */
	private ObjectPoolFactory() {
		super();
	}

}
