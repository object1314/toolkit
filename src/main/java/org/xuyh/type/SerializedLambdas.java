/*
 * Copyright (c) 2023-2023 XuYanhang.
 */
package org.xuyh.type;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Tool to resolve {@link SerializedLambda}. The lambda function allowed coding like <code>Entity::method</code>.
 *
 * @author XuYanhang
 * @since 2023-03-19
 */
public final class SerializedLambdas {
    /**
     * Parses a function coding like <code>Entity::method</code> to {@link SerializedLambda}.
     *
     * @param function a function in lambda coding like <code>Entity::method</code>
     * @return an instance of {@link SerializedLambda}
     */
    public static SerializedLambda parse(Object function) {
        SerializedLambda lambda;
        try {
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            Accesses.setAccessible(method);
            lambda = (SerializedLambda) method.invoke(function);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException("Lambda function only allows like Entity::method", e);
        }
        String methodName = lambda.getImplMethodName();
        if (methodName.startsWith("lambda$")) {
            throw new IllegalArgumentException("Lambda function only allows like Entity::method");
        }
        return lambda;
    }

    /**
     * Returns the caller class on a {@link SerializedLambda}
     *
     * @param lambda an instance of {@link SerializedLambda}
     * @param classLoader ClassLoader to search class
     * @return the class or <code>null</code> if not found
     */
    public static Class<?> getImplClass(SerializedLambda lambda, ClassLoader classLoader) {
        String className = lambda.getImplClass().replace('/', '.');
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private SerializedLambdas() {
    }
}
