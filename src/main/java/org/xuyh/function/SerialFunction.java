/*
 * Copyright (c) 2023-2023 XuYanhang
 *
 */
package org.xuyh.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * A {@link Function} but {@link Serializable}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @author XuYanhang
 * @see Function
 * @see Serializable
 * @since 2023-03-13
 */
@FunctionalInterface
public interface SerialFunction<T, R> extends Function<T, R>, Serializable {
}
