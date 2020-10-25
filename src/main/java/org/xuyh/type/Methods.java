/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tool to do with method.
 * 
 * @author XuYanhang
 * @since 2020-10-24
 *
 */
public final class Methods {

	/**
	 * Get method by method member signature. If failed then <code>null</code>
	 * returned instead of an <tt>Exception</tt>.
	 */
	public static Method getMethod(Class<?> cla, String name, Class<?>... paraTypes) {

		Method[] methods = cla.getMethods();

		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getName().equals(name) && Arrays.equals(method.getParameterTypes(), paraTypes)) {
				return method;
			}
		}

		return null;
	}

	/**
	 * Get method only by method member name. If there if exists more than one
	 * methods then only get first one on search and if failed then
	 * <code>null</code> returned instead of an <tt>Exception</tt>.
	 */
	public static Method getMethodByName(Class<?> cla, String name) {

		Method[] methods = cla.getMethods();

		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getName().equals(name)) {
				return method;
			}
		}

		return null;
	}

	/**
	 * Get methods only by method member name.
	 */
	public static Method[] getMethodsByName(Class<?> cla, String name) {

		ArrayList<Method> targetMethods = new ArrayList<>();
		Method[] methods = cla.getMethods();

		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getName().equals(name)) {
				targetMethods.add(method);
			}
		}

		return targetMethods.toArray(new Method[targetMethods.size()]);
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Methods() {
		super();
	}

}
