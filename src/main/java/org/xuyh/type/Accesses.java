/*
 * Copyright (c) 2023-2023 XuYanhang
 *
 */
package org.xuyh.type;

import java.lang.reflect.AccessibleObject;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Tool to do with {@link AccessibleObject} like fields, methods and constructors.
 *
 * @author XuYanhang
 * @since 2023-03-01
 */
public final class Accesses {
    /**
     * Runs the function on privileged.
     *
     * @param runnable task require privileged.
     */
    public static void resolve(Runnable runnable) {
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * Access controller on accessible object. Sets an {@link AccessibleObject} to be accessible.
     *
     * @param accessibleObject accessible object
     */
    public static void setAccessible(AccessibleObject accessibleObject) {
        setAccessible(accessibleObject, true);
    }

    /**
     * Access controller on accessible object. Sets an {@link AccessibleObject} to be accessible or not.
     *
     * @param accessibleObject accessible object
     * @param accessible       the target accessible to set on
     */
    public static void setAccessible(AccessibleObject accessibleObject, boolean accessible) {
        if (accessibleObject.isAccessible() ^ accessible) {
            resolve(() -> accessibleObject.setAccessible(true));
        }
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private Accesses() {
    }
}
