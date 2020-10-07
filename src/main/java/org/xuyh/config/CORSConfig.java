/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.config;

import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Configuration on CORS.
 * 
 * @author XuYanhang
 *
 */
@ConditionalOnProperty("cors.enable")
@org.springframework.context.annotation.Configuration
public class CORSConfig implements WebMvcConfigurer {

	@Value("${cors.config}")
	private String configUrl;

	/**
	 * 
	 */
	public CORSConfig() {
		super();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		Node confNode = loadXmlConfNode();
		NodeList corsNodes = confNode.getChildNodes();
		int corsLength = corsNodes.getLength();
		for (int i = 0; i < corsLength; i++) {
			Node corsNode = corsNodes.item(i);
			if (corsNode.getNodeName().equalsIgnoreCase("cors"))
				registXmlCors(corsNode, registry);
		}
	}

	private Node loadXmlConfNode() {
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new DefaultResourceLoader(null).getResource(configUrl).getInputStream());
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		NodeList confNodes = doc.getChildNodes();
		int length = confNodes.getLength();
		Node confNode = null;
		for (int i = 0; i < length; i++) {
			Node tempConfNode = confNodes.item(i);
			if (tempConfNode.getNodeName().equalsIgnoreCase("configuration")) {
				confNode = tempConfNode;
			}
		}
		if (null == confNode)
			throw new IllegalStateException("no configuration label declared");

		return confNode;
	}

	private void registXmlCors(Node corsNode, CorsRegistry registry) {
		/*
		 * Properties of a CROS
		 */
		String pathMapping = "";
		LinkedList<String> allowedMethods = new LinkedList<>();
		LinkedList<String> allowedOrigins = new LinkedList<>();
		LinkedList<String> allowHeaders = new LinkedList<>();
		LinkedList<String> exposedHeaders = new LinkedList<>();
		Boolean allowCredentials = null;
		Long maxAge = null;
		/*
		 * Parse and check the properties
		 */
		NodeList corsPropNodes = corsNode.getChildNodes();
		int propLength = corsPropNodes.getLength();
		for (int p = 0; p < propLength; p++) {
			/*
			 * Parse and check property
			 */
			Node propNode = corsPropNodes.item(p);
			String propName = propNode.getNodeName();
			if (propName.equalsIgnoreCase("pathMapping")) {
				pathMapping = propNode.getTextContent().trim();
			} else if (propName.equalsIgnoreCase("allowedMethods")) {
				NodeList methodNodes = propNode.getChildNodes();
				int methodLength = methodNodes.getLength();
				for (int x = 0; x < methodLength; x++) {
					Node methodNode = methodNodes.item(x);
					if (methodNode.getNodeName().equalsIgnoreCase("method")) {
						String method = methodNode.getTextContent().trim();
						if (!method.isEmpty())
							allowedMethods.add(method);
					}
				}
			} else if (propName.equalsIgnoreCase("allowedOrigins")) {
				NodeList originNodes = propNode.getChildNodes();
				int originLength = originNodes.getLength();
				for (int x = 0; x < originLength; x++) {
					Node originNode = originNodes.item(x);
					if (originNode.getNodeName().equalsIgnoreCase("origin")) {
						String origin = originNode.getTextContent().trim();
						if (!origin.isEmpty())
							allowedOrigins.add(origin);
					}
				}
			} else if (propName.equalsIgnoreCase("allowedHeaders")) {
				NodeList headerNodes = propNode.getChildNodes();
				int headerLength = headerNodes.getLength();
				for (int x = 0; x < headerLength; x++) {
					Node headerNode = headerNodes.item(x);
					if (headerNode.getNodeName().equalsIgnoreCase("header")) {
						String header = headerNode.getTextContent().trim();
						if (!header.isEmpty())
							allowHeaders.add(header);
					}
				}
			} else if (propName.equalsIgnoreCase("exposedHeaders")) {
				NodeList headerNodes = propNode.getChildNodes();
				int headerLength = headerNodes.getLength();
				for (int x = 0; x < headerLength; x++) {
					Node headerNode = headerNodes.item(x);
					if (headerNode.getNodeName().equalsIgnoreCase("header")) {
						String header = headerNode.getTextContent().trim();
						if (!header.isEmpty())
							exposedHeaders.add(header);
					}
				}
			} else if (propName.equalsIgnoreCase("allowCredentials")) {
				String allowCredentialsStr = propNode.getTextContent().trim();
				if (allowCredentialsStr.equalsIgnoreCase("true"))
					allowCredentials = Boolean.TRUE;
				else if (allowCredentialsStr.equalsIgnoreCase("false"))
					allowCredentials = Boolean.FALSE;
			} else if (propName.equalsIgnoreCase("maxAge")) {
				String maxAgeStr = propNode.getTextContent().trim();
				if (!maxAgeStr.isEmpty())
					maxAge = Long.parseLong(maxAgeStr);
			}
		}
		if (pathMapping.isEmpty())
			throw new IllegalStateException("Empty pathMapping");
		/*
		 * Registry properties
		 */
		CorsRegistration registration = registry.addMapping(pathMapping);
		if (!allowedMethods.isEmpty())
			registration.allowedMethods(allowedMethods.toArray(new String[allowedMethods.size()]));
		if (!allowedOrigins.isEmpty())
			registration.allowedOrigins(allowedOrigins.toArray(new String[allowedOrigins.size()]));
		if (!exposedHeaders.isEmpty())
			registration.exposedHeaders(exposedHeaders.toArray(new String[exposedHeaders.size()]));
		if (null != allowCredentials)
			registration.allowCredentials(allowCredentials);
		if (null != maxAge)
			registration.maxAge(maxAge);
	}

}
