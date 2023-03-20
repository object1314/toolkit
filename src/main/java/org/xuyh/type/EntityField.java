/*
 * Copyright (c) 2023-2023 XuYanhang.
 */
package org.xuyh.type;

/**
 * Field operator on a typical entity field. It includes type, name properties and can be read or wrote on entity.
 *
 * @param <E> the type of the entity
 * @param <F> the type of the field
 * @author XuYanhang
 * @since 2023-03-19
 */
public interface EntityField<E, F> {
    /**
     * Returns the type of the field.
     *
     * @return The type of the field
     */
    Class<F> getType();

    /**
     * Returns the name of the field.
     *
     * @return the name of the field
     */
    String getName();

    /**
     * Reads the value of a field.
     *
     * @param entity the entity to get value
     * @return the field get value of the entity
     */
    F read(E entity);

    /**
     * Writes the value of a field.
     *
     * @param entity the entity to set value
     * @param value  the field value to set
     */
    void write(E entity, F value);
}
