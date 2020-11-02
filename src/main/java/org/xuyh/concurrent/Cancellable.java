/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.concurrent;

/**
 * A {@link Cancellable} represents a process or an operation that can be
 * canceled.
 *
 * @author XuYanhang
 * @since 2020-10-29
 */
@FunctionalInterface
public interface Cancellable {

	/**
	 * Cancel the action or free a resource.
	 */
	public void cancel();

}
