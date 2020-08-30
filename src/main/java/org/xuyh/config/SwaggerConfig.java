package org.xuyh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.async.DeferredResult;
import org.xuyh.ApplicationVersion;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@org.springframework.context.annotation.Configuration
@springfox.documentation.swagger2.annotations.EnableSwagger2
public class SwaggerConfig {

	/**
	 * 
	 */
	public SwaggerConfig() {
		super();
	}

	@Bean
	public Docket defaultApi() {
		return new Docket(DocumentationType.SWAGGER_2) // Generate a Docket
				.groupName("toolkit") // GroupName
				.genericModelSubstitutes(DeferredResult.class) // to apply generic model substitution
				.useDefaultResponseMessages(false) // to determine if the default response messages are used
				.forCodeGeneration(false) // determines the naming strategy used
				.pathMapping("/")// path that acts as a prefix to the API base path
				.select() // Initiates a builder for api selection.
				.apis(RequestHandlerSelectors.any()) // Filter apis
				.paths(PathSelectors.any()) // Filter paths
				.build() // Build
				.apiInfo(apiInfo()); // Indicates the api information
	}

	private ApiInfo apiInfo() {
		Contact contact = new Contact("XuYanhang", "https://github.com/object1314/", "object1314@yeah.net");
		return new ApiInfoBuilder() // Builder
				.title("toolkits") // Title
				.description("Platform's REST API on toolkit.") // Description
				.contact(contact) // Contact
				.version("V" + ApplicationVersion.version) // Version
				.build(); // Out
	}

}
