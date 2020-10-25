/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * Simple tool to analyze class type and its declared fields and methods. It's a
 * quick way to do dictionary on classes.
 * 
 * @author XuYanhang
 * @since 2020-10-24
 *
 */
public class ClassPrinter {

	/**
	 * Print stream on the classes message.
	 */
	private PrintStream out;

	/**
	 * Flag on count to tab.
	 */
	private int tab;

	/**
	 * Create an printer on classes by System.out.
	 */
	public ClassPrinter() {
		this(System.out);
	}

	/**
	 * Create an analyzer of Stream.out.
	 */
	public ClassPrinter(PrintStream out) {
		super();
		this.out = out;
		this.tab = 0;
	}

	/**
	 * Print a class. And get this printer to adjust again.
	 * 
	 * @see #printPackage(Class)
	 * @see #printClass(Class)
	 * @return this
	 */
	public ClassPrinter print(Class<?> cla) {
		printPackage(cla);
		printClass(cla);
		out.flush();
		return this;
	}

	/**
	 * Print packages.
	 */
	private void printPackage(Class<?> cla) {
		if (tab != 0) {
			return;
		}
		Package pack = cla.getPackage();
		if (null == pack) {
			return;
		}
		printAnnotations(pack.getAnnotations());
		out.println("package " + pack.getName() + ";");
		out.println();
	}

	/**
	 * Print class.
	 * 
	 * @see #printClassHead(Class)
	 * @see #printFields(Class)
	 * @see #printConstructors(Class)
	 * @see #printMethods(Class)
	 * @see #printInnerClasses(Class)
	 * @see #printClassTail(Class)
	 */
	private void printClass(Class<?> cla) {
		printClassHead(cla);
		tab++;
		printFields(cla);
		printConstructors(cla);
		printMethods(cla);
		printInnerClasses(cla);
		tab--;
		printClassTail(cla);
	}

	/**
	 * Print the head of class.
	 */
	private void printClassHead(Class<?> cla) {
		printAnnotations(cla.getAnnotations());
		printTab();
		printModifier(cla.getModifiers());
		if (!cla.isInterface()) {
			out.print("class ");
		}
		out.print(cla.getSimpleName());
		Type superCla = cla.getGenericSuperclass();
		if (null != superCla) {
			out.print(" extends " + superCla.getTypeName());
		}
		Type[] interfaces = cla.getGenericInterfaces();
		if (interfaces.length > 0) {
			if (cla.isInterface()) {
				out.print(" extends ");
			} else {
				out.print(" implements ");
			}
			for (int i = 0; i < interfaces.length; i++) {
				out.print(interfaces[i].getTypeName());
				if (i < interfaces.length - 1) {
					out.print(", ");
				}
			}
		}
		out.println(" {");
		out.println();
	}

	/**
	 * Print the fields of class.
	 */
	private void printFields(Class<?> cla) {
		for (Field field : cla.getDeclaredFields()) {
			printAnnotations(field.getAnnotations());
			int mod = field.getModifiers();
			printTab();
			printModifier(mod);
			out.print(field.getGenericType().getTypeName() + " " + field.getName());
			if (Modifier.isStatic(mod)) {
				field.setAccessible(true);
				try {
					Object value = field.get(null);
					out.print(" = " + parseObjValue(value));
				} catch (Exception e) {
				}
			}
			out.println(";");
			out.println();
		}
	}

	/**
	 * Print the constructors of class.
	 */
	private void printConstructors(Class<?> cla) {
		for (Constructor<?> constructor : cla.getDeclaredConstructors()) {
			printAnnotations(constructor.getAnnotations());
			printTab();
			printModifier(constructor.getModifiers());
			out.print(cla.getSimpleName());
			out.print("(");
			printParameters(constructor.getParameters());
			out.print(")");
			printExceptions(constructor.getExceptionTypes());
			out.println(";");
			out.println();
		}
	}

	/**
	 * Print the methods of class.
	 */
	private void printMethods(Class<?> cla) {
		Method[] methods = cla.getDeclaredMethods();
		for (Method method : methods) {
			printAnnotations(method.getAnnotations());
			printTab();
			printModifier(method.getModifiers());
			out.print(method.getGenericReturnType().getTypeName() + " " + method.getName());
			out.print("(");
			printParameters(method.getParameters());
			out.print(")");
			printExceptions(method.getExceptionTypes());
			if (cla.isAnnotation()) {
				Object value = method.getDefaultValue();
				if (null != value) {
					out.print(" default " + parseObjValue(value));
				}
			}
			out.println(";");
			out.println();
		}
	}

	/**
	 * Print the inner classes of class.
	 */
	private void printInnerClasses(Class<?> cla) {
		for (Class<?> innerCla : cla.getDeclaredClasses()) {
			printClass(innerCla);
		}
	}

	/**
	 * Print the tail of class.
	 */
	private void printClassTail(Class<?> cla) {
		printTab();
		out.println("}");
		out.println();
	}

	/**
	 * Print the modifiers String.
	 */
	private void printModifier(int modifier) {
		String modifiers = Modifier.toString(modifier);
		out.print(modifiers);
		if (modifiers.length() > 0) {
			out.print(" ");
		}
	}

	/**
	 * Print parameters.
	 */
	private void printParameters(Parameter[] parameters) {
		for (int i = 0; i < parameters.length; i++) {
			printParamAnnotations(parameters[i].getAnnotations());
			out.print(parameters[i].getParameterizedType().getTypeName() + " " + parameters[i].getName());
			if (i < parameters.length - 1) {
				out.print(", ");
			}
		}
	}

	/**
	 * Print the annotations on parameters.
	 */
	private void printParamAnnotations(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			out.print(parseAnnotation(annotation) + " ");
		}
	}

	/**
	 * Print the annotations on packages, Classes, fields, constructors or methods.
	 */
	private void printAnnotations(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			printTab();
			out.println(parseAnnotation(annotation));
		}
	}

	/**
	 * Print the exceptions.
	 */
	private void printExceptions(Class<?>[] exes) {
		if (exes.length > 0) {
			out.print(" throws ");
			for (int i = 0; i < exes.length; i++) {
				out.print(exes[i].getTypeName());
				if (i < exes.length - 1) {
					out.print(", ");
				}
			}
		}
	}

	/**
	 * Print tabs.
	 */
	private void printTab() {
		for (int i = 0; i < tab; i++) {
			out.print("\t");
		}
	}

	/**
	 * Parse an annotation value to String.
	 */
	private String parseAnnotation(Annotation annotation) {
		StringBuilder sb = new StringBuilder();
		Class<? extends Annotation> type = annotation.annotationType();
		sb.append("@" + type.getName());
		Method[] methods = type.getDeclaredMethods();
		if (methods.length > 0) {
			sb.append("(");
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				sb.append(method.getName() + "=");
				Object value = method.getDefaultValue();
				try {
					value = method.invoke(annotation);
				} catch (Exception e) {
				}
				sb.append(parseObjValue(value));
				if (i < methods.length - 1) {
					sb.append(", ");
				}
			}
			sb.append(")");
		}
		return sb.toString();
	}

	/**
	 * Parse an object value to String.
	 */
	private String parseObjValue(Object obj) {
		if (obj == null) {
			return "null";
		}
		if (obj instanceof Annotation) {
			return parseAnnotation((Annotation) obj);
		}
		if (obj instanceof Class<?>) {
			return ((Class<?>) obj).getName() + ".Class";
		}
		Class<?> type = obj.getClass();
		if (type == Character.class || type == Character.TYPE) {
			return "\'" + parseString(obj.toString()) + "\'";
		}
		if (type == String.class) {
			return "\"" + parseString(obj.toString()) + "\"";
		}
		if (type.isEnum()) {
			return type.getTypeName() + "." + obj;
		}
		StringBuffer result = new StringBuffer();
		if (type.isArray()) {
			result.append('{');
			int len = Array.getLength(obj);
			for (int i = 0; i < len; i++) {
				result.append(parseObjValue(Array.get(obj, i)));
				if (i < len - 1) {
					result.append(", ");
				}
			}
			result.append('}');
		} else {
			result.append(parseString(obj.toString()));
		}
		return result.toString();
	}

	/**
	 * Filter on some special chars.
	 */
	private String parseString(String string) {
		if (string == null) {
			return "null";
		}
		char b;
		char c = '\0';
		int i;
		int len = string.length();
		StringBuffer sb = new StringBuffer(len);
		String t;
		for (i = 0; i < len; i += 1) {
			b = c;
			c = string.charAt(i);
			switch (c) {
			case '\\':
			case '"':
				sb.append('\\');
				sb.append(c);
				break;
			case '/':
				if (b == '<') {
					sb.append('\\');
				}
				sb.append(c);
				break;
			default:
				if (c < ' ' || (c >= '\ue000' && c <= '\uf8ff')) {
					switch (c) {
					case '\b':
						sb.append('\\');
						sb.append('b');
						break;
					case '\t':
						sb.append('\\');
						sb.append('t');
						break;
					case '\n':
						sb.append('\\');
						sb.append('n');
						break;
					case '\f':
						sb.append('\\');
						sb.append('f');
						break;
					case '\r':
						sb.append('\\');
						sb.append('r');
						break;
					default:
						t = Integer.toHexString((int) c);
						int tLength = t.length();
						sb.append('\\');
						sb.append('u');
						sb.append(tLength < 4 ? '0' : t.charAt(tLength - 4));
						sb.append(tLength < 3 ? '0' : t.charAt(tLength - 3));
						sb.append(tLength < 2 ? '0' : t.charAt(tLength - 2));
						sb.append(t.charAt(tLength - 1));
					}
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

}
