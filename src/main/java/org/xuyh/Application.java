package org.xuyh;

import org.springframework.boot.SpringApplication;

//Sprint Boot Auto Configuration
@org.springframework.boot.autoconfigure.EnableAutoConfiguration
@org.springframework.context.annotation.ComponentScan(basePackages = "org.xuyh")
public class Application implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(Application.class, args);
	}

}
