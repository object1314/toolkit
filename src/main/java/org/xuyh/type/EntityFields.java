/*
 * Copyright (c) 2023-2023 XuYanhang.
 */
package org.xuyh.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.xuyh.function.SerialFunction;
import org.xuyh.function.SerialBiConsumer;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Factory to generate {@link EntityField}.
 *
 * @author XuYanhang
 * @see EntityField
 * @since 2023-03-19
 */
public final class EntityFields {
    /**
     * The builder to build {@link EntityField}.
     *
     * @param <E> Entity type
     * @param <F> Field type
     */
    public static class Builder<E, F> {
        private Class<?> entityType;
        private String name;
        private Class<F> type;
        private SerialFunction<E, F> getter;
        private SerialBiConsumer<E, F> setter;
        private boolean readable = true;
        private boolean writable = true;

        /**
         * Sets the entity type including the field.
         *
         * @param entityType the entity type
         * @return this
         */
        public Builder<E, F> in(Class<?> entityType) {
            Objects.requireNonNull(entityType);
            if (this.entityType == null) {
                this.entityType = entityType;
                return this;
            }
            if (this.entityType.isAssignableFrom(entityType)) {
                this.entityType = entityType;
                return this;
            }
            if (entityType.isAssignableFrom(this.entityType)) {
                return this;
            }
            throw new IllegalArgumentException("Entity type conflict");
        }

        /**
         * Sets the name of the field
         *
         * @param name the name of the field
         * @return this
         */
        public Builder<E, F> name(String name) {
            Objects.requireNonNull(name);
            if (this.name != null && !this.name.equals(name)) {
                throw new IllegalArgumentException("Name conflict");
            }
            this.name = name;
            return this;
        }

        /**
         * Sets the type of the field
         *
         * @param type the type of the field
         * @return this
         */
        public Builder<E, F> type(Class<?> type) {
            Objects.requireNonNull(type);
            if (this.type != null && this.type != type) {
                throw new IllegalArgumentException("Type conflict");
            }
            this.type = (Class<F>) type;
            return this;
        }

        /**
         * Sets the field on the builder.
         *
         * @param field the field
         * @return this
         */
        public Builder<E, F> field(Field field) {
            in(field.getDeclaringClass());
            name(field.getName());
            type(field.getType());
            return this;
        }

        /**
         * Sets on the getter method in lambda coding like <code>Entity::getField</code>.
         *
         * @param getter getter method lambda
         * @return this
         */
        public Builder<E, F> getter(SerialFunction<E, F> getter) {
            this.getter = Objects.requireNonNull(getter);
            return this;
        }

        /**
         * Sets on the setter method in lambda coding like <code>Entity::setField</code>.
         *
         * @param setter setter method lambda
         * @return this
         */
        public Builder<E, F> setter(SerialBiConsumer<E, F> setter) {
            this.setter = Objects.requireNonNull(setter);
            return this;
        }

        /**
         * After this call, the calling on {@link EntityField#read} has an {@link UnsupportedOperationException}
         *
         * @return this
         */
        public Builder<E, F> disableRead() {
            this.readable = false;
            return this;
        }

        /**
         * After this call, the calling on {@link EntityField#write} has an {@link UnsupportedOperationException}
         *
         * @return this
         */
        public Builder<E, F> disableWrite() {
            this.writable = false;
            return this;
        }

        /**
         * Parse the {@link #getter}
         */
        private void parseGetter() {
            if (getter == null) {
                return;
            }
            SerializedLambda lambda = SerializedLambdas.parse(getter);
            if (entityType == null) {
                in(SerializedLambdas.getImplClass(lambda, getter.getClass().getClassLoader()));
            }
            Method method = Methods.getMethod(entityType, lambda.getImplMethodName());
            if (method == null) {
                throw new IllegalStateException("Method not found");
            }
            type(method.getReturnType());
            String prefix = type == Boolean.TYPE ? IS_PREFIX : GET_PREFIX;
            name(parseMethodName(method.getName(), prefix));
        }

        /**
         * Parse the {@link #setter}
         */
        private void parseSetter() {
            if (setter == null) {
                return;
            }
            SerializedLambda lambda = SerializedLambdas.parse(setter);
            if (entityType == null) {
                in(SerializedLambdas.getImplClass(lambda, setter.getClass().getClassLoader()));
            }
            Method method = Methods.getMethodByName(entityType, lambda.getImplMethodName());
            if (method == null) {
                throw new IllegalStateException("Method not found");
            }
            if (method.getParameterTypes().length != 1) {
                throw new IllegalStateException("Method expected one argument");
            }
            name(parseMethodName(method.getName(), SET_PREFIX));
            type(method.getParameterTypes()[0]);
        }

        /**
         * Check the {@link #entityType}, {@link #name} and {@link #type} exists.
         * But only if the {@link #type} not exists, we'll try to parse it.
         */
        private void checkBasic() {
            if (entityType == null) {
                throw new IllegalStateException("Entity type required");
            }
            if (name == null) {
                throw new IllegalStateException("Field name required");
            }
            if (type != null) {
                return;
            }
            Field field = Fields.getField(entityType, name);
            if (field != null) {
                field(field);
            } else {
                throw new IllegalStateException("Field type required");
            }
        }

        /**
         * Build an {@link EntityField}.
         *
         * @return an instance of {@link EntityField}
         */
        public EntityField<E, F> build() {
            parseGetter();
            parseSetter();
            checkBasic();
            Function<E, F> reader = readable ? (this.getter == null ? new FieldReader<>(entityType, name, type) : getter) : null;
            BiConsumer<E, F> writer = writable ? (this.setter == null ? new FieldWriter<>(entityType, name, type) : setter) : null;
            return new EntityFieldImpl<>(type, name, reader, writer);
        }
    }

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";
    private static final String SET_PREFIX = "set";

    public static <E, F> Builder<E, F> builder() {
        return new Builder<>();
    }

    /**
     * Creates an instance of {@link EntityField} by the entity type and field name.
     *
     * @param entityType class or subclass declaring the field
     * @param name       field name
     * @param <E>        Entity type
     * @param <F>        Field type
     * @return an instance of {@link EntityField}
     */
    public static <E, F> EntityField<E, F> of(Class<?> entityType, String name) {
        return new Builder<E, F>().in(entityType).name(name).build();
    }

    /**
     * Creates an instance of {@link EntityField} by the field.
     *
     * @param field field
     * @param <E>   Entity type
     * @param <F>   Field type
     * @return an instance of {@link EntityField}
     */
    public static <E, F> EntityField<E, F> of(Field field) {
        return new Builder<E, F>().field(field).build();
    }

    /**
     * Creates an instance of {@link EntityField} by the entity type and field.
     *
     * @param entityType class or subclass declaring the field
     * @param field      field
     * @param <E>        Entity type
     * @param <F>        Field type
     * @return an instance of {@link EntityField}
     */
    public static <E, F> EntityField<E, F> of(Class<?> entityType, Field field) {
        return new Builder<E, F>().in(entityType).field(field).build();
    }

    /**
     * Creates an instance of {@link EntityField} by the entity getter.
     *
     * @param getter getter coding like <code>Entity::getField</code>
     * @param <E>    Entity type
     * @param <F>    Field type
     * @return an instance of {@link EntityField}
     */
    public static <E, F> EntityField<E, F> of(SerialFunction<E, F> getter) {
        return new Builder<E, F>().getter(getter).build();
    }

    /**
     * Creates an instance of {@link EntityField} by the entity type and getter.
     *
     * @param entityType class or subclass declaring the field
     * @param getter     getter coding like <code>Entity::getField</code>
     * @param <E>        Entity type
     * @param <F>        Field type
     * @return an instance of {@link EntityField}
     */
    public static <E, F> EntityField<E, F> of(Class<?> entityType, SerialFunction<E, F> getter) {
        return new Builder<E, F>().in(entityType).getter(getter).build();
    }

    private static String parseMethodName(String methodName, String prefix) {
        if (!methodName.startsWith(prefix) || methodName.length() <= prefix.length()) {
            throw new IllegalArgumentException("Not a method expected starts with " + prefix + " but " + methodName);
        }
        return Character.toLowerCase(methodName.charAt(prefix.length())) + methodName.substring(prefix.length() + 1);
    }

    private static String buildMethodName(String name, String prefix) {
        return prefix + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    @AllArgsConstructor
    private static class EntityFieldImpl<E, F> implements EntityField<E, F> {
        @Getter
        private final Class<F> type;
        @Getter
        private final String name;
        private final Function<E, F> reader;
        private final BiConsumer<E, F> writer;

        @Override
        public F read(E entity) {
            if (reader == null) {
                throw new UnsupportedOperationException();
            }
            return reader.apply(entity);
        }

        @Override
        public void write(E entity, F value) {
            if (writer == null) {
                throw new UnsupportedOperationException();
            }
            writer.accept(entity, value);
        }
    }

    private static class FieldReader<E, F> implements Function<E, F> {
        private final Class<F> type;
        private final Method method;
        private final Field field;

        FieldReader(Class<?> entityType, String name, Class<F> type) {
            this.type = type;
            String prefix = type == Boolean.TYPE ? IS_PREFIX : GET_PREFIX;
            String methodName = buildMethodName(name, prefix);
            method = Methods.getMethod(entityType, methodName);
            if (method != null) {
                if (method.getReturnType() != type) {
                    throw new IllegalStateException("Type conflict when located on method");
                }
                field = null;
                return;
            }
            field = Fields.getField(entityType, name);
            if (field == null) {
                throw new IllegalStateException("Can't locate read method");
            }
            if (field.getType() != type) {
                throw new IllegalStateException("Type conflict when located on field");
            }
        }

        @Override
        public F apply(E entity) {
            return method != null ? readOnMethod(entity) : readOnField(entity);
        }

        F readOnMethod(E entity) {
            Accesses.setAccessible(method, true);
            try {
                return type.cast(method.invoke(entity));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }

        F readOnField(E entity) {
            Accesses.setAccessible(field, true);
            try {
                return type.cast(field.get(entity));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static class FieldWriter<E, F> implements BiConsumer<E, F> {
        private final Method method;
        private final Field field;

        FieldWriter(Class<?> entityType, String name, Class<F> type) {
            String methodName = buildMethodName(name, SET_PREFIX);
            method = Methods.getMethod(entityType, methodName, type);
            if (method != null) {
                field = null;
                return;
            }
            field = Fields.getField(entityType, name);
            if (field == null) {
                throw new IllegalStateException("Can't locate write method");
            }
            if (field.getType() != type) {
                throw new IllegalStateException("Type conflict when located on field");
            }
            if (Modifier.isFinal(field.getModifiers())) {
                throw new IllegalStateException("Final modifier when located on field");
            }
        }

        @Override
        public void accept(E entity, F value) {
            if (method != null) {
                writeOnMethod(entity, value);
            } else {
                writeOnField(entity, value);
            }
        }

        void writeOnMethod(E entity, F value) {
            Accesses.setAccessible(method, true);
            try {
                method.invoke(entity, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }

        void writeOnField(E entity, F value) {
            Accesses.setAccessible(field, true);
            try {
                field.set(entity, value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private EntityFields() {
    }
}
