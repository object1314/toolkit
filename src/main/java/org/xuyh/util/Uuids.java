/*
 * Copyright (c) 2023-2023 XuYanhang
 *
 */
package org.xuyh.util;

import com.fasterxml.uuid.Generators;

/**
 * Toolkit on generator Uuids.
 *
 * @author XuYanhang
 * @since 2023-03-13
 */
public final class Uuids {
    /**
     * Generates a UUID string in timeBase type.
     *
     * @return a UUID string
     */
    public static String newUuid() {
        return Generators.timeBasedGenerator().generate().toString();
    }
}
