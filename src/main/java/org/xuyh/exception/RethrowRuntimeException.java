/*
 * Copyright (c) 2023-2023 XuYanhang.
 */
package org.xuyh.exception;

import java.util.Objects;

/**
 * {@link RethrowRuntimeException} is a {@link RuntimeException} on exception.
 *
 * @author XuYanhang
 * @since 2023-03-21
 */
public class RethrowRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -7881981618345734813L;

    /**
     * Initialize method.
     *
     * @param exception the exception to catch
     */
    public RethrowRuntimeException(Exception exception) {
        super(Objects.requireNonNull(exception));
    }

    /**
     * Returns the cause exception
     *
     * @return the cause exception
     */
    public Exception getException() {
        return (Exception) super.getCause();
    }
}
