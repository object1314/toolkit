/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.type;

import java.io.Reader;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * A wrapped class to execute noshorn script(like a JavaScript).
 * 
 * @author XuYanhang
 * @since 2020-10-27
 *
 */
public class NoshornExecutor {

	/**
	 * The script engine manager. Singleton is OK.
	 */
	private static ScriptEngineManager manager = new ScriptEngineManager();

	/**
	 * Execute noshorn a script and gets the result. Usually used an expression to
	 * execute directly.
	 * 
	 * @param script the script to execute
	 * @return execute result. The type usually used Number, Boolean, String, Map
	 *         and Void.
	 * @throws ScriptException if failed to execute the script
	 */
	@SuppressWarnings("unchecked")
	public static <T> T executeScript(String script) throws ScriptException {
		return (T) manager.getEngineByName("nashorn").eval(script);
	}

	/**
	 * Noshorn script engine. Also an instance of {@link Invocable}.
	 */
	private final ScriptEngine engine;

	/**
	 * Create an executor
	 */
	public NoshornExecutor() {
		super();
		engine = manager.getEngineByName("nashorn");
	}

	/**
	 * Returns the global binding objects. These global objects are in this executor
	 * scope(ENGINE_SCOPE).
	 * 
	 * @return the global binding objects
	 */
	public Map<String, Object> getGlobals() {
		return engine.getBindings(ScriptContext.ENGINE_SCOPE);
	}

	/**
	 * Returns a global binding object or <code>null</code> if not exists.
	 * 
	 * @param name the global binding name
	 * @return a global binding object or <code>null</code> if no such element
	 */
	public Object getGlobal(String name) {
		return engine.getBindings(ScriptContext.ENGINE_SCOPE).get(name);
	}

	/**
	 * Bind a global object into this executor.
	 * 
	 * @param name  the global name to bind
	 * @param value the global value to bind
	 * @return this
	 */
	public NoshornExecutor bindGlobal(String name, Object value) {
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put(name, value);
		engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		return this;
	}

	/**
	 * Batch bind some global objects into this executor.
	 * 
	 * @param globals globals to merge into bindings
	 * @return this
	 */
	public NoshornExecutor bindGlobals(Map<String, ? extends Object> globals) {
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.putAll(globals);
		engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		return this;
	}

	/**
	 * Unbind a global object from this executor.
	 * 
	 * @param name the global binding name to unbind
	 * @return this
	 */
	public NoshornExecutor unbindGlobal(String name) {
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.remove(name);
		engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		return this;
	}

	/**
	 * Unbind some global objects from this executor.
	 * 
	 * @param name the global binding names to unbind
	 * @return this
	 */
	public NoshornExecutor unbindGlobals(Iterable<String> names) {
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		for (String name : names)
			bindings.remove(name);
		engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		return this;
	}

	/**
	 * Include a script into this executor. Usually it's binded into global scope.
	 * 
	 * @param script the script to include
	 * @return this
	 * @throws ScriptException on any execute error in including this script
	 */
	public NoshornExecutor include(String script) throws ScriptException {
		engine.eval(script);
		return this;
	}

	/**
	 * Includes a script into this executor. Usually it's binded into global scope.
	 * 
	 * @param scriptReader the script to include on reader
	 * @return this
	 * @throws ScriptException on any execute error in including this script
	 */
	public NoshornExecutor include(Reader scriptReader) throws ScriptException {
		engine.eval(scriptReader);
		return this;
	}

	/**
	 * Includes a script and gets the execute result.
	 * 
	 * @param script the script to include and execute
	 * @return the execute result
	 * @throws ScriptException on any execute error in including this script
	 */
	@SuppressWarnings("unchecked")
	public <T> T includeExecute(String script) throws ScriptException {
		return (T) engine.eval(script);
	}

	/**
	 * Includes a script and gets the execute result.
	 * 
	 * @param scriptReader the script reader to include and execute
	 * @return the execute result
	 * @throws ScriptException on any execute error in including this script
	 */
	@SuppressWarnings("unchecked")
	public <T> T includeExecute(Reader scriptReader) throws ScriptException {
		return (T) engine.eval(scriptReader);
	}

	/**
	 * Invokes a function who is binded in this executor global scope.
	 * 
	 * @param method the function name to invoke
	 * @param args   arguments of the function
	 * @return execute result
	 * @throws NoSuchMethodException if no such a function in this executor
	 * @throws ScriptException       on any execute error in execute this function
	 */
	@SuppressWarnings("unchecked")
	public <T> T invokeFunction(String method, Object... args) throws NoSuchMethodException, ScriptException {
		return (T) ((Invocable) engine).invokeFunction(method, args);
	}

	/**
	 * Invokes a method who is binded in <code>this</code> object.
	 * 
	 * @param thiz   caller object, a <code>this</code> object to run the method
	 * @param method the method name to invoke
	 * @param args   arguments of the function
	 * @return execute result
	 * @throws NoSuchMethodException if no such a method in this executor
	 * @throws ScriptException       on any execute error in execute this method
	 */
	@SuppressWarnings("unchecked")
	public <T> T invokeMethod(Object thiz, String method, Object... args)
			throws NoSuchMethodException, ScriptException {
		return (T) ((Invocable) engine).invokeMethod(thiz, method, args);
	}

	/**
	 * Executes a script and gets it to a noshorn object.
	 * 
	 * @param script the script language source to be executed
	 * @return a NoshornObject after executed
	 * @throws ScriptException on any execute error in execute this script
	 * @see #includeExecute(String)
	 * @see NoshornObject
	 */
	public NoshornObject evalNoshornObject(String script) throws ScriptException {
		Object thiz = engine.eval(script);
		return new NoshornObject(this, (Map<?, ?>) thiz);
	}

	/**
	 * Executes a script and gets it to a noshorn object.
	 * 
	 * @param scriptReader the script reader source to be executed
	 * @return a NoshornObject after executed
	 * @throws ScriptException on any execute error in execute this script
	 * @see #includeExecute(Reader)
	 * @see NoshornObject
	 */
	public NoshornObject evalNoshornObject(Reader scriptReader) throws ScriptException {
		Object thiz = engine.eval(scriptReader);
		return new NoshornObject(this, (Map<?, ?>) thiz);
	}

	/**
	 * Fetches a property from this executor global scope and cast it as a
	 * {@link NoshornObject}
	 * 
	 * @param name the global binding name
	 * @return a NoshornObject
	 * @see #getGlobal(String)
	 * @see NoshornObject
	 */
	public NoshornObject fetchGlobalNoshornObject(String name) {
		Object thiz = engine.getBindings(ScriptContext.ENGINE_SCOPE).get(name);
		return new NoshornObject(this, (Map<?, ?>) thiz);
	}

	/**
	 * Invokes a global function and gets the result on cast as a
	 * {@link NoshornObject}
	 * 
	 * @param method the function name points on the target
	 * @param args   arguments to execute push
	 * @return a NoshornObject after execute finish
	 * @throws NoSuchMethodException if no such a function in this executor
	 * @throws ScriptException       on any execute error in execute this function
	 * @see #invokeFunction(String, Object...)
	 * @see NoshornObject
	 */
	public NoshornObject invokeFunctionToNoshornObject(String method, Object... args)
			throws NoSuchMethodException, ScriptException {
		Object thiz = ((Invocable) engine).invokeFunction(method, args);
		return new NoshornObject(this, (Map<?, ?>) thiz);
	}

}
