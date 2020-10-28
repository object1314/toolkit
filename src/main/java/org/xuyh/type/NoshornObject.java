/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

/**
 * An object wrapped in noshorn object, who can execute self methods as this
 * directly. Build it use {@link NoshornExecutor}.
 * 
 * @author XuYanhang
 * @since 2020-10-27
 *
 */
public final class NoshornObject {

	/**
	 * Global executor
	 */
	private final NoshornExecutor executor;
	/**
	 * Caller as this
	 */
	private Map<String, Object> thiz;

	/**
	 * Create a this object in noshorn object
	 * 
	 * @param executor the executor to execute
	 * @param thiz     the {@link #thiz} to set
	 */
	@SuppressWarnings("unchecked")
	NoshornObject(NoshornExecutor executor, Object thiz) {
		super();
		if (null == executor || null == thiz)
			throw new NullPointerException();
		this.executor = executor;
		this.thiz = (Map<String, Object>) thiz;
	}

	/**
	 * Returns this object, as the caller as this
	 * 
	 * @return the {@link #thiz}
	 */
	public Object source() {
		return thiz;
	}

	/**
	 * List all properties, methods on names of this object
	 * 
	 * @return property names and method names of this object
	 */
	public List<String> dir() {
		return new ArrayList<>(thiz.keySet());
	}

	/**
	 * Check if this object contains the specified property of method
	 * 
	 * @param name the property or method name
	 * @return <code>true</code> if this object contains the specified property of
	 *         method
	 */
	public boolean contains(String name) {
		return thiz.containsKey(name);
	}

	/**
	 * Returns the property or method value on specified or <code>null</code> if not
	 * exists
	 * 
	 * @param name the property or method name
	 * @return the specified property or method value or <code>null</code> if not
	 *         exists
	 */
	public Object get(String name) {
		return thiz.get(name);
	}

	/**
	 * Returns a property as a string value, might <code>null</code>
	 * 
	 * @param name the name of the string
	 * @return the string value, might <code>null</code>
	 */
	public String getString(String name) {
		Object pro = thiz.get(name);
		if (null == pro)
			return null;
		return pro.toString();
	}

	/**
	 * Returns a property as a string value, or in specified default value when the
	 * origin value is <code>null</code>
	 * 
	 * @param name         the name of the string
	 * @param defaultValue default string value
	 * @return the string value, or <code>defaultValue</code>
	 */
	public String getString(String name, String defaultValue) {
		Object pro = thiz.get(name);
		if (null == pro)
			return defaultValue;
		return pro.toString();
	}

	/**
	 * Returns a number value from specified name on property, might
	 * <code>null</code> when not exists or parsed failed
	 * 
	 * @param name the name of the number property
	 * @return the number value, might <code>null</code>
	 */
	public Number getNumber(String name) {
		Object pro = thiz.get(name);
		if (null == pro)
			return null;
		return Numbers.castNumber(pro);
	}

	/**
	 * Returns an integer number value from specified name on property, might
	 * <code>null</code> when not exists or parsed failed
	 * 
	 * @param name the name of the number property
	 * @return the integer number value, might <code>null</code>
	 */
	public Number getInteger(String name) {
		Object pro = thiz.get(name);
		if (null == pro)
			return null;
		return Numbers.castInteger(pro);
	}

	/**
	 * Returns an integer number value from specified name on property, Error occurs
	 * when not exists or parsed failed
	 * 
	 * @param name the name of the number property
	 * @return the number value
	 * @throws NullPointerException if the origin value not exists or cast failed
	 */
	public int getInt(String name) {
		return getNumber(name).intValue();
	}

	/**
	 * Returns an integer number value from specified name on property, or default
	 * value when not exists or parsed failed
	 * 
	 * @param name         the name of the number property
	 * @param defaultValue returns the value when not exists or parsed failed
	 * @return the number value or default value
	 */
	public int getInt(String name, int defaultValue) {
		Number number = getNumber(name);
		return null == number ? defaultValue : number.intValue();
	}

	/**
	 * Returns a long integer number value from specified name on property, Error
	 * occurs when not exists or parsed failed
	 * 
	 * @param name the name of the number property
	 * @return the number value
	 * @throws NullPointerException if the origin value not exists or cast failed
	 */
	public long getLong(String name) {
		return getNumber(name).longValue();
	}

	/**
	 * Returns a long integer number value from specified name on property, or
	 * default value when not exists or parsed failed
	 * 
	 * @param name         the name of the number property
	 * @param defaultValue returns the value when not exists or parsed failed
	 * @return the number value or default value
	 */
	public long getLong(String name, long defaultValue) {
		Number number = getNumber(name);
		return null == number ? defaultValue : number.longValue();
	}

	/**
	 * Returns an float number value from specified name on property, Error occurs
	 * when not exists or parsed failed
	 * 
	 * @param name the name of the number property
	 * @return the number value
	 * @throws NullPointerException if the origin value not exists or cast failed
	 */
	public float getFloat(String name) {
		return getNumber(name).floatValue();
	}

	/**
	 * Returns a float number value from specified name on property, or default
	 * value when not exists or parsed failed
	 * 
	 * @param name         the name of the number property
	 * @param defaultValue returns the value when not exists or parsed failed
	 * @return the number value or default value
	 */
	public float getFloat(String name, float defaultValue) {
		Number number = getNumber(name);
		return null == number ? defaultValue : number.floatValue();
	}

	/**
	 * Returns an double float number value from specified name on property, Error
	 * occurs when not exists or parsed failed
	 * 
	 * @param name the name of the number property
	 * @return the number value
	 * @throws NullPointerException if the origin value not exists or cast failed
	 */
	public double getDouble(String name) {
		return getNumber(name).doubleValue();
	}

	/**
	 * Returns a double float number value from specified name on property, or
	 * default value when not exists or parsed failed
	 * 
	 * @param name         the name of the number property
	 * @param defaultValue returns the value when not exists or parsed failed
	 * @return the number value or default value
	 */
	public double getDouble(String name, double defaultValue) {
		Number number = getNumber(name);
		return null == number ? defaultValue : number.doubleValue();
	}

	/**
	 * Returns a boolean value from specified name on property. Origin boolean value
	 * of <code>false</code> and <code>true</code> will be passed correctly. Integer
	 * number value will be cast non-zero value as <code>true</code> and cast zero
	 * as <code>false</code>. Object(Map or Collection) value will be cast as
	 * <code>true</code> only if it is not empty. Value <code>null</code> value will
	 * be cast as <code>false</code>. Empty String value considered as
	 * <code>false</code>. Others will be cast as <code>true</code>.
	 * 
	 * @param name the name of the boolean property
	 * @return the target boolean value
	 */
	public boolean getBoolean(String name) {
		Object pro = thiz.get(name);
		if (null == pro)
			return false;
		if (pro instanceof Boolean)
			return (Boolean) pro;
		if (pro instanceof Number) {
			if (Numbers.isInteger((Number) pro)) {
				if (pro instanceof BigInteger)
					return !((BigInteger) pro).equals(BigInteger.ZERO);
				return ((Number) pro).longValue() != 0;
			}
		}
		if (pro instanceof Map)
			return ((Map<?, ?>) pro).size() > 0;
		if (pro instanceof Collection)
			return ((Collection<?>) pro).size() > 0;
		return !pro.toString().isEmpty();
	}

	/**
	 * Returns the enumeration from the specified property on name, or
	 * <code>null</code> if not exists or failed parse
	 * 
	 * @param name the name of the enumeration property
	 * @param cla  the enumeration type
	 * @return the enumeration from the specified property on name, or
	 *         <code>null</code> if not exist or failed parse
	 */
	public <T extends Enum<T>> T getEnum(String name, Class<T> cla) {
		Object pro = thiz.get(name);
		if (null == pro)
			return null;
		String target = pro.toString();
		try {
			@SuppressWarnings("unchecked")
			T[] values = (T[]) cla.getMethod("values").invoke(null);
			for (T value : values) {
				if (target.equalsIgnoreCase(value.toString())) {
					return (T) value;
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Returns a Map object from the specified property on name, or
	 * <code>null</code> if not exists
	 * 
	 * @param name the name of the map property
	 * @return the property on Map type or <code>null</code> if not exists
	 * @throws ClassCastException when the property not a Map
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap(String name) {
		Object pro = thiz.get(name);
		if (null == pro)
			return null;
		return (Map<K, V>) pro;
	}

	/**
	 * Returns a List object from the specified property on name, or
	 * <code>null</code> if not exists
	 * 
	 * @param name the name of the list property
	 * @return the property on List type or <code>null</code> if not exists
	 * @throws ClassCastException when the property can not be a List
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String name) {
		Object pro = thiz.get(name);
		if (null == pro)
			return null;
		if (pro instanceof List)
			return (List<T>) pro;
		return new ArrayList<>(((Map<?, T>) pro).values());
	}

	/**
	 * Return a property as a {@link NoshornObject}
	 * 
	 * @param name the name of the object property
	 * @return a NoshornObject from this property name
	 * @throws NullPointerException if the property doesn't exists or is
	 *                              <code>null</code>
	 * @throws ClassCastException   if the property can no be a noshorn object
	 */
	public NoshornObject getNoshornObject(String name) {
		return new NoshornObject(executor, (Map<?, ?>) thiz.get(name));
	}

	/**
	 * Invoke a method in this object. The caller is this.
	 * 
	 * @param method the method name in this object
	 * @param args   the arguments
	 * @return execute result
	 * @throws UnsupportedOperationException of this method doesn't exist
	 * @throws IllegalStateException         on error happen in execute this method
	 */
	public <T> T invoke(String method, Object... args) {
		try {
			return executor.invokeMethod(thiz, method, args);
		} catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException(e);
		} catch (ScriptException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Invoke a method in this object. The caller is this. Casts the result as a
	 * NoshornObject.
	 * 
	 * @param method the method name in this object
	 * @param args   the arguments
	 * @return a NoshornObject on execute result
	 * @throws UnsupportedOperationException of this method doesn't exist
	 * @throws IllegalStateException         on error happen in execute this method
	 */
	public NoshornObject invokeAsNoshornObject(String method, Object... args) {
		try {
			return new NoshornObject(executor, executor.invokeMethod(thiz, method, args));
		} catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException(e);
		} catch (ScriptException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((thiz == null) ? 0 : thiz.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoshornObject other = (NoshornObject) obj;
		if (thiz == null) {
			if (other.thiz != null)
				return false;
		} else if (!thiz.equals(other.thiz))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return thiz.toString();
	}

}
