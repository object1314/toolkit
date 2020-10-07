/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

/**
 * It is a custom {@link SpringApplication}, the runner for {@link Application}.
 * 
 * @author XuYanhang
 *
 */
public class ApplicationRunner extends SpringApplication {

	/**
	 * Create an ApplicationRunner.
	 */
	public ApplicationRunner() {
		super(new Class<?>[] { Application.class });
	}

	/**
	 * PID file is spring.pid.file in setting properties
	 * 
	 * @return this
	 */
	public ApplicationRunner registServicePID() {
		super.addListeners(new ApplicationListener<?>[] { new ApplicationPidFileWriter() });
		return this;
	}

	/**
	 * Register start begin banner.
	 * 
	 * @return this
	 */
	public ApplicationRunner registBanner() {
		super.setBanner(new Banner() {
			@Override
			public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
				printResourceText("START_BEGIN", "file:./startbegin.txt", "classpath:startbegin.txt");
			}
		});
		return this;
	}

	/**
	 * @see SpringApplication#run(String...)
	 */
	@Override
	public ConfigurableApplicationContext run(String... args) {
		try {
			ConfigurableApplicationContext context = super.run(args);
			printResourceText("START_SUCCESS", "file:./startsuccess.txt", "classpath:startsuccess.txt");
			return context;
		} catch (Throwable t) {
			printResourceText("START_FAILURE", "file:./startfailure.txt", "classpath:startfailure.txt");
			throw t;
		}
	}

	/**
	 * Print the text defined in resource URLs on logger.
	 * 
	 * @param defaultText  Default text if no text found in resource URLs.
	 * @param resourceUrls Resource URLs supported of file URL, HTTP URL and
	 *                     classpath URL and so on.
	 */
	private void printResourceText(String defaultText, String... resourceUrls) {
		ResourceLoader resourceLoader = super.getResourceLoader();
		if (null == resourceLoader)
			resourceLoader = new DefaultResourceLoader(getClassLoader());
		InputStream in = null;
		for (int i = 0; i < resourceUrls.length && null == in; i++) {
			try {
				in = resourceLoader.getResource(resourceUrls[i]).getInputStream();
			} catch (Exception e) {
			}
		}
		String text = defaultText;
		if (null != in) {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[256];
				int len = -1;
				while ((len = in.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				text = new String(bos.toByteArray());
			} catch (IOException e) {
				super.getApplicationLog().warn("Read text failed: " + defaultText);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		super.getApplicationLog().info(text);
	}

}
