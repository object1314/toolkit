/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xuyh.net.LocalInetAddress;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller to manage this application's global settings.
 * 
 * @author XuYanhang
 *
 */
@RestController
@RequestMapping("/api/v1/app")
@Api(tags = "Application Controller", value = "Application Controller")
public class ApplicationController extends HttpRestControllerHandler implements EnvironmentAware {

	private Environment environment;

	/**
	 * 
	 */
	public ApplicationController() {
		super();
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@RequestMapping(value = "/bootoptions", // PATH
			method = RequestMethod.GET, // METHOD
			produces = { "application/json" })
	@ApiOperation(tags = "List boot options", value = "List boot options")
	public AppBootOptions getAppBootOptions(HttpServletRequest request) throws Throwable {
		return new AppBootOptions(environment);
	}

	@RequestMapping(value = "/errorcodes", // PATH
			method = RequestMethod.GET, // METHOD
			produces = { "application/json" })
	@ApiOperation(tags = "List all error codes", value = "List all error codes")
	public AppErrorCode[] getAppErrorCodes(HttpServletRequest request) throws Throwable {
		HttpRestErrorHandler.Code[] sources = HttpRestErrorHandler.Code.values();
		AppErrorCode[] targets = new AppErrorCode[sources.length];
		for (int i = 0; i < sources.length; i++)
			targets[i] = new AppErrorCode(sources[i]);
		return targets;
	}

	@RequestMapping(value = "/inetaddrs", // PATH
			method = RequestMethod.GET, // METHOD
			produces = { "application/json" })
	@ApiOperation(tags = "List inet addresses", value = "List inet addresses")
	public LocalInetAddress[] getAppOsAllInetAddrs(HttpServletRequest request) throws Throwable {
		List<LocalInetAddress> addresses = LocalInetAddress.listAddresses();
		return addresses.toArray(new LocalInetAddress[addresses.size()]);
	}

	@RequestMapping(value = "/jvm/memory", // PATH
			method = RequestMethod.GET, // METHOD
			produces = { "application/json" })
	@ApiOperation(tags = "List JVM memory status", value = "List JVM memory status")
	public AppEnvirMemory getAppJVMMemory(HttpServletRequest request) throws Throwable {
		return new AppEnvirMemory();
	}

	@RequestMapping(value = "/jvm/gc", // PATH
			method = RequestMethod.POST, // METHOD
			produces = {})
	@ApiOperation(tags = "Do JVM Garbage Collect", value = "Do JVM Garbage Collect")
	public void garbageCollectAppJVMMemory(HttpServletRequest request) throws Throwable {
		Runtime.getRuntime().gc();
	}

	/**
	 * Boot option
	 */
	public static class AppBootOptions {

		private PropertyResolver propertyResolver;

		public AppBootOptions(PropertyResolver propertyResolver) {
			super();
			this.propertyResolver = propertyResolver;
		}

		public String getAppName() {
			return propertyResolver.getProperty("project.name");
		}

		public String getAppVersion() {
			return propertyResolver.getProperty("project.version");
		}

		public String getWebSocketServerEnable() {
			return propertyResolver.getProperty("websocket.server.enable");
		}

		public String getWebSocketServerPort() {
			return propertyResolver.getProperty("websocket.server.port");
		}

		public AppBootHttpProxyOption[] getHttpProxys() {
			ArrayList<AppBootHttpProxyOption> proxys = new ArrayList<>();
			if (Boolean.parseBoolean(propertyResolver.getProperty("http.proxy.enable", "false"))) {
				for (int i = 0; i < 12; i++) {
					String prefix = "http.proxy." + i;
					if (Boolean.parseBoolean(propertyResolver.getProperty(prefix + ".enable", "false"))) {
						String servletPath = propertyResolver.getProperty(prefix + ".servlet_path");
						String targetUri = propertyResolver.getProperty(prefix + ".target_uri");
						proxys.add(new AppBootHttpProxyOption(servletPath, targetUri));
					}
				}
			}
			return proxys.toArray(new AppBootHttpProxyOption[proxys.size()]);
		}

	}

	/**
	 * HTTP proxy configuration
	 *
	 */
	public static class AppBootHttpProxyOption {

		public String servletPath;
		public String targetUri;

		public AppBootHttpProxyOption(String servletPath, String targetUri) {
			super();
			this.servletPath = servletPath;
			this.targetUri = targetUri;
		}
	}

	/**
	 * Error code
	 */
	public static class AppErrorCode {

		private HttpRestErrorHandler.Code code;

		public AppErrorCode(HttpRestErrorHandler.Code code) {
			super();
			this.code = code;
		}

		/**
		 * @return the error status
		 */
		public int getStatus() {
			return code.status;
		}

		/**
		 * @return the error type
		 */
		public String getType() {
			return code.type;
		}

	}

	/**
	 * Memory monitor
	 */
	public static class AppEnvirMemory {

		public long getFreeMemory() {
			return Runtime.getRuntime().freeMemory();
		}

		public long getTotalMemory() {
			return Runtime.getRuntime().totalMemory();
		}

		public long getMaxMemory() {
			return Runtime.getRuntime().maxMemory();
		}
	}

}
