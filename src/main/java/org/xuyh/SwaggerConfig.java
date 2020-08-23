package org.xuyh;

import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@org.springframework.context.annotation.Configuration
@springfox.documentation.swagger2.annotations.EnableSwagger2
public class SwaggerConfig {

	@org.springframework.context.annotation.Bean
	public Docket defaultApi() {
		return new Docket(DocumentationType.SWAGGER_2) // SWAGGER2_DocumentationType
				.genericModelSubstitutes(DeferredResult.class) // genericModelSubstitutes
				.useDefaultResponseMessages(false) // useDefaultResponseMessages
				.forCodeGeneration(false) // forCodeGeneration
				.pathMapping("/")// pathMapping
				.select() // Initiates_selection
				.build() // Build
				.apiInfo(apiInfo());
	}

	@org.springframework.context.annotation.Bean
	public Docket sysApi() {
		return new Docket(DocumentationType.SWAGGER_2) // SWAGGER2_DocumentationType
				.groupName("sys") // groupName
				.genericModelSubstitutes(DeferredResult.class) // genericModelSubstitutes
				.useDefaultResponseMessages(false) // useDefaultResponseMessages
				.forCodeGeneration(false) // forCodeGeneration
				.pathMapping("/") // pathMapping
				.select() // Initiates_selection
				.paths(path -> !path.startsWith("api")) // Selector
				.build() // Build
				.apiInfo(apiInfo());
	}

	@org.springframework.context.annotation.Bean
	public Docket appApi() {
		return new Docket(DocumentationType.SWAGGER_2) // SWAGGER2_DocumentationType
				.groupName("app") // groupName
				.genericModelSubstitutes(DeferredResult.class) // genericModelSubstitutes
				.useDefaultResponseMessages(false) // useDefaultResponseMessages
				.forCodeGeneration(false) // forCodeGeneration
				.pathMapping("/") // pathMapping
				.select() // Initiates_selection
				.paths(path -> path.startsWith("api")) // Selector
				.build() // Build
				.apiInfo(apiInfo());
	}

	private springfox.documentation.service.ApiInfo apiInfo() {
		return new springfox.documentation.builders.ApiInfoBuilder() // Builder
				.title("ToolKitsManager") // Title
				.description("Platform's REST API") // Description
				.version(ApplicationVersion.version) // Version
				.build();
	}

}
