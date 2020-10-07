/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Entrance class of the spring application.
 * 
 * @author XuYanhang
 *
 */
//Sprint Boot Auto Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.xuyh")
public class Application implements WebMvcConfigurer {

	/**
	 * Entrance main method.
	 * 
	 * @param args start arguments
	 * @throws Throwable throw these exceptions into stack
	 */
	public static void main(String[] args) throws Throwable {
		new ApplicationRunner().registServicePID().registBanner().run(args);
	}

}
