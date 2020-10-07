/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.config;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

/**
 * Configuration on HTTP Proxy those are injected into SpringBoot directly.
 * 
 * @author XuYanhang
 *
 */
@ConditionalOnProperty("http.proxy.enable")
@org.springframework.context.annotation.Configuration
public class HttpProxyServletConfig implements EnvironmentAware {

	private PropertyResolver propertyResolver;

	/**
	 * 
	 */
	public HttpProxyServletConfig() {
		super();
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.propertyResolver = environment;
	}

	@ConditionalOnProperty("http.proxy.1.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean1() {
		return _registerServletRegistrationBeanN(1);
	}

	@ConditionalOnProperty("http.proxy.2.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean2() {
		return _registerServletRegistrationBeanN(2);
	}

	@ConditionalOnProperty("http.proxy.3.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean3() {
		return _registerServletRegistrationBeanN(3);
	}

	@ConditionalOnProperty("http.proxy.4.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean4() {
		return _registerServletRegistrationBeanN(4);
	}

	@ConditionalOnProperty("http.proxy.5.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean5() {
		return _registerServletRegistrationBeanN(5);
	}

	@ConditionalOnProperty("http.proxy.6.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean6() {
		return _registerServletRegistrationBeanN(6);
	}

	@ConditionalOnProperty("http.proxy.7.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean7() {
		return _registerServletRegistrationBeanN(7);
	}

	@ConditionalOnProperty("http.proxy.8.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean8() {
		return _registerServletRegistrationBeanN(8);
	}

	@ConditionalOnProperty("http.proxy.9.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean9() {
		return _registerServletRegistrationBeanN(0);
	}

	@ConditionalOnProperty("http.proxy.10.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean10() {
		return _registerServletRegistrationBeanN(10);
	}

	@ConditionalOnProperty("http.proxy.11.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean11() {
		return _registerServletRegistrationBeanN(11);
	}

	@ConditionalOnProperty("http.proxy.12.enable")
	@Bean
	public ServletRegistrationBean<ProxyServlet> registerServletRegistrationBean12() {
		return _registerServletRegistrationBeanN(12);
	}

	private ServletRegistrationBean<ProxyServlet> _registerServletRegistrationBeanN(int n) {

		String servletPath = propertyResolver.getProperty("http.proxy." + n + ".servlet_path", "").trim();
		String targetUri = propertyResolver.getProperty("http.proxy." + n + ".target_uri", "").trim();

		if (servletPath.isEmpty())
			throw new IllegalArgumentException("Illegal proxy servlet path of http.proxy." + n + ".servlet_path");
		if (targetUri.isEmpty())
			throw new IllegalArgumentException("Illegal proxy target uri of http.proxy." + n + ".target_uri");

		ServletRegistrationBean<ProxyServlet> servletRegistrationBean = new ServletRegistrationBean<>(
				new ProxyServlet(), true, servletPath);
		servletRegistrationBean.addInitParameter("targetUri", targetUri);
		servletRegistrationBean.addInitParameter(ProxyServlet.P_LOG,
				propertyResolver.getProperty("http.proxy.log.open", "false"));
		servletRegistrationBean.setName("http_proxy_" + n);

		return servletRegistrationBean;
	}

}
