/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.controller;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	public AppInetAddress[] getAppOsAllInetAddrs(HttpServletRequest request) throws Throwable {
		ArrayList<AppInetAddress> inetAddrs = new ArrayList<>();
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			Iterator<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses().iterator();
			while (interfaceAddresses.hasNext()) {
				InterfaceAddress interfaceAddress = interfaceAddresses.next();
				if (null == interfaceAddress.getAddress())
					continue;
				inetAddrs.add(new AppInetAddress(networkInterface, interfaceAddress));
			}
		}
		return inetAddrs.toArray(new AppInetAddress[inetAddrs.size()]);
	}

	@RequestMapping(value = "/osenvir/memory", // PATH
			method = RequestMethod.GET, // METHOD
			produces = { "application/json" })
	@ApiOperation(tags = "List OS memory status", value = "List OS memory status")
	public AppEnvirMemory getAppOsEnvirMemory(HttpServletRequest request) throws Throwable {
		return new AppEnvirMemory();
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
	 * IP address
	 */
	public static class AppInetAddress {

		public String ip;
		public int ipVersion;
		public String mac;
		public int prefix;
		public String mask;
		public String broadcast;
		public String name;

		public AppInetAddress(NetworkInterface netWorkInterface, InterfaceAddress interfaceAddress) throws Exception {
			super();
			// Load IP and IPVersion
			this.ip = interfaceAddress.getAddress().getHostAddress();
			if (interfaceAddress.getAddress() instanceof Inet4Address)
				ipVersion = 4;
			else if (interfaceAddress.getAddress() instanceof Inet6Address)
				ipVersion = 6;
			// Load mac
			byte[] bmac = netWorkInterface.getHardwareAddress();
			if (null != bmac) {
				StringBuilder macBuilder = new StringBuilder(bmac.length * 3 - 1);
				for (int i = 0; i < bmac.length; i++) {
					if (i != 0)
						macBuilder.append(':');
					macBuilder.append(Character.forDigit((bmac[i] >> 4) & 0Xf, 16));
					macBuilder.append(Character.forDigit(bmac[i] & 0Xf, 16));
				}
				this.mac = macBuilder.toString();
			}
			// Load prefix and mask
			this.prefix = interfaceAddress.getNetworkPrefixLength();
			if (4 == ipVersion) {
				int imask = 0Xffffffff << (32 - this.prefix);
				StringBuilder maskBuilder = new StringBuilder();
				maskBuilder.append((imask >> 24) & 0Xff).append('.');
				maskBuilder.append((imask >> 16) & 0Xff).append('.');
				maskBuilder.append((imask >> 8) & 0Xff).append('.');
				maskBuilder.append(imask & 0Xff);
				this.mask = maskBuilder.toString();
			}
			// Load broadcast
			if (null != interfaceAddress.getBroadcast())
				this.broadcast = interfaceAddress.getBroadcast().getHostAddress();
			// Load name
			this.name = netWorkInterface.getName();
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
