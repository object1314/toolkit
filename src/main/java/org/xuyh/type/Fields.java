/*
 * Copyright (c) 2023-2023 XuYanhang
 *
 */
package org.xuyh.type;

import java.lang.reflect.Field;

/**
 * Tool to do with field.
 *
 * @author XuYanhang
 * @since 2023-03-01
 */
public final class Fields {
    /**
     * Returns the field by name.
     *
     * @param cla  field declared in type or subtype
     * @param name field name
     * @return field or null if not found
     */
    public static Field getField(Class<?> cla, String name) {
        // Check the public fields
        Field field = findField(cla.getFields(), name);
        if (field != null) {
            return field;
        }
        // Check the private fields
        for (; cla != null; cla = cla.getSuperclass()) {
            field = findField(cla.getDeclaredFields(), name);
            if (field != null) {
                return field;
            }
        }
        return null;
    }

    /**
     * Find a field with specified name in a list of fields.
     *
     * @param fields container of fields
     * @param name   target field name
     * @return target field or <code>null</code> if not found.
     */
    private static Field findField(Field[] fields, String name) {
        if (fields == null) {
            return null;
        }
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Returns the value of a static field in a type.
     *
     * @param cla  type contains the field
     * @param name the field name
     * @param <T>  the field value type
     * @return the field value
     * @throws IllegalArgumentException when failed
     */
    @SuppressWarnings("unchecked")
    public static <T> T getStaticValue(Class<?> cla, String name) {
        Field field = getField(cla, name);
        if (field == null) {
            throw new IllegalArgumentException("Field not found");
        }
        Accesses.setAccessible(field);
        try {
            return (T) field.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Sets the value on a static field.
     *
     * @param cla   type contains the field
     * @param name  the field name
     * @param value the field value
     * @throws IllegalArgumentException when failed
     */
    public static void setStaticValue(Class<?> cla, String name, Object value) {
        Field field = getField(cla, name);
        if (field == null) {
            throw new IllegalArgumentException("Field not found");
        }
        Accesses.setAccessible(field);
        try {
            field.set(null, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns the value of a field for an object or <code>null</code> if not found.
     * Be careful that if the field value <code>null</code> self, as same with not found.
     *
     * @param obj  instance contains the field
     * @param name the field name
     * @param <T>  the field value type
     * @return the field value
     * @throws IllegalArgumentException when failed
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object obj, String name) {
        Field field = getField(obj.getClass(), name);
        if (field == null) {
            throw new IllegalArgumentException("Field not found");
        }
        Accesses.setAccessible(field);
        try {
            return (T) field.get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Sets the value on a field.
     *
     * @param obj   instance contains the field
     * @param name  the field name
     * @param value the field value
     * @throws IllegalArgumentException when failed
     */
    public static void setValue(Object obj, String name, Object value) {
        Field field = getField(obj.getClass(), name);
        if (field == null) {
            throw new IllegalArgumentException("Field not found");
        }
        Accesses.setAccessible(field);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private Fields() {
    }
}
