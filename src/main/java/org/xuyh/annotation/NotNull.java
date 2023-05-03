/*
 * Copyright (c) 2023-2023 XuYanhang.
 */

package org.xuyh.annotation;

import java.lang.annotation.*;

/**
 * Defines some elements can not be null.
 *
 * @author XuYanhang
 * @since 2023-05-03
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface NotNull {
}
