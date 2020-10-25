/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.function.Predicate;

/**
 * Tool to do with class type.
 * 
 * @author XuYanhang
 * @since 2020-10-24
 *
 */
public final class Classes {

	/**
	 * Find bean from an array by type. The result will return first matcher or
	 * <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T findBeanOfClass(Object[] beans, Class<? extends T> cla) {
		for (int i = 0; i < beans.length; i++) {
			Object arg = beans[i];
			if (null == arg) {
				continue;
			}
			if (isInstanceOf(arg, cla)) {
				return (T) arg;
			}
		}
		return null;
	}

	/**
	 * Analyze if a object can be the type from class
	 */
	public static boolean isInstanceOf(Object value, Class<?> cla) {
		if (cla == Void.TYPE || cla == Void.class) {
			return value == null;
		}
		if (cla.isPrimitive()) {
			if (null == value) {
				return false;
			}
			cla = filterComplexClass(cla);
		}
		return cla.isInstance(value);
	}

	/**
	 * If a class is a primitive type then we cast it to pack type
	 */
	public static Class<?> filterComplexClass(Class<?> cla) {
		if (!cla.isPrimitive()) {
			return cla;
		}
		if (cla == Void.TYPE) {
			cla = Void.class;
		} else if (cla == Double.TYPE) {
			cla = Double.class;
		} else if (cla == Float.TYPE) {
			cla = Float.class;
		} else if (cla == Long.TYPE) {
			cla = Long.class;
		} else if (cla == Integer.TYPE) {
			cla = Integer.class;
		} else if (cla == Short.TYPE) {
			cla = Short.class;
		} else if (cla == Byte.TYPE) {
			cla = Byte.class;
		} else if (cla == Character.TYPE) {
			cla = Character.class;
		} else if (cla == Boolean.TYPE) {
			cla = Boolean.class;
		}
		return cla;
	}

	/**
	 * Cast a bean to a bean of type while return null instead of ClassCastException
	 */
	public static <T> T cast(Class<? extends T> cla, Object bean) {
		if (null != bean && cla.isInstance(bean)) {
			return cla.cast(bean);
		}
		return null;
	}

	/**
	 * Read the accessible fields on the cla type by a filter
	 */
	public static LinkedList<Field> loadAccessibleFields(Class<?> cla, Predicate<Field> filter) {
		if (null == cla || null == filter)
			throw new NullPointerException();
		LinkedList<Field> fields = new LinkedList<>();
		loadAccessibleFields(cla, fields, filter);
		return fields;
	}

	/**
	 * Load the accessible fields on the cla type into a List by a filter
	 */
	private static void loadAccessibleFields(Class<?> cla, LinkedList<Field> fields, Predicate<Field> filter) {
		for (Field field : cla.getDeclaredFields()) {
			if (filter.test(field)) {
				field.setAccessible(true);
				fields.add(field);
			}
		}
		Class<?> superCla = cla.getSuperclass();
		if (null != superCla) {
			loadAccessibleFields(superCla, fields, filter);
		}
	}

	/**
	 * Find the super class both two classes belongs to. Failed when the class
	 * contains interface then get the Object.class instead.
	 */
	public static Class<?> findJointSuperCla(Class<?> cla1, Class<?> cla2) {
		if (cla1.isInterface() || cla2.isInterface()) {
			return Object.class;
		}
		for (Class<?> iCla = cla1; iCla != null; iCla = iCla.getSuperclass()) {
			for (Class<?> jCla = cla2; jCla != null; jCla = jCla.getSuperclass()) {
				if (iCla.isAssignableFrom(jCla)) {
					return iCla;
				}
				if (jCla.isAssignableFrom(iCla)) {
					return jCla;
				}
			}
		}
		return Object.class;
	}

	/**
	 * Assign the shared field values from a bean to another bean
	 */
	public static int assignJointFieldValues(Object from, Object to) {
		Class<?> cla = findJointSuperCla(from.getClass(), to.getClass());
		LinkedList<Field> fields = loadAccessibleFields(cla,
				field -> (field.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) == 0);
		int status = 0;
		for (Field field : fields) {
			try {
				Object value = field.get(from);
				field.set(to, value);
			} catch (Exception e) {
				status--;
			}
		}
		return status;
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Classes() {
		super();
	}

}
