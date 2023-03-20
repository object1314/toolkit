/*
 * Copyright (c) 2023-2023 XuYanhang.
 */
package org.xuyh.type;

import org.xuyh.exception.RethrowRuntimeException;

import java.util.function.Function;

/**
 * A tool to resolve throwable.
 *
 * @author XuYanhang
 * @since 2023-03-01
 */
public final class Throwables {
    /**
     * Rethrow an error or exception. If it's a checked exception, it will be rethrow in a {@link RethrowRuntimeException}.
     * Otherwise, it will be thrown directly.
     *
     * @param throwable error instance
     */
    public static void rethrow(Throwable throwable) {
        rethrow(throwable, RethrowRuntimeException::new);
    }

    /**
     * Rethrow an error or exception. If it's a checked exception, it will be cast to runtime exception and rethrow.
     * Otherwise, it will be thrown directly.
     *
     * @param throwable error instance
     * @param caster    cast a checked exception to runtime exception
     */
    public static void rethrow(Throwable throwable, Function<Exception, RuntimeException> caster) {
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        throw caster.apply((Exception) throwable);
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private Throwables() {
    }
}
