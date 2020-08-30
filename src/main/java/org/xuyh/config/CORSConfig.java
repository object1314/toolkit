package org.xuyh.config;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@org.springframework.context.annotation.Configuration
public class CORSConfig implements WebMvcConfigurer {

	/**
	 * 
	 */
	public CORSConfig() {
		super();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowCredentials(false)
				.allowedMethods("*")
				.allowedOrigins("*")
				.allowedHeaders("*");
	}

	private void parse() throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ClassPathResource("conf/cors.xml").getInputStream());
		Node conf = doc.getElementsByTagName("configuration").item(0);
		NodeList corses = conf.getChildNodes();
		int length = corses.getLength();
		for (int i = 0; i < length; i++) {
			Node cors = corses.item(i);
		}
	}

	public static class ParseError extends Error {

		/**
		 * 
		 */
		public ParseError() {
			super();
		}

		/**
		 * @param message
		 * @param cause
		 * @param enableSuppression
		 * @param writableStackTrace
		 */
		public ParseError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		/**
		 * @param message
		 * @param cause
		 */
		public ParseError(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * @param message
		 */
		public ParseError(String message) {
			super(message);
		}

		/**
		 * @param cause
		 */
		public ParseError(Throwable cause) {
			super(cause);
		}

	}
}
