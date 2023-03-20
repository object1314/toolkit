/*
 * Copyright (c) 2023-2023 XuYanhang.
 */
package org.xuyh.function;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 * A {@link BiConsumer} but {@link Serializable}.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @author XuYanhang
 * @see BiConsumer
 * @see Serializable
 * @since 2023-03-20
 */
@FunctionalInterface
public interface SerialBiConsumer<T, U> extends BiConsumer<T, U>, Serializable {
}
