/*
 * Copyright (c) 2023-2023 XuYanhang
 *
 */
package org.xuyh.function;

/**
 * A function but can throw errors.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @author XuYanhang
 * @since 2023-03-13
 */
@FunctionalInterface
public interface ThrowFunction<T, R> {
    R apply(T t) throws Throwable;
}
