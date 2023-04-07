/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuration on Swagger-UI.
 *
 * @author XuYanhang
 * @since 2020-12-31
 */
@org.springframework.context.annotation.Configuration
@springfox.documentation.swagger2.annotations.EnableSwagger2
public class SwaggerConfig {
    @Value("${project.name}")
    private String projectName;

    @Value("${project.version}")
    private String projectVersion;

    /**
     * New instance from Spring Boot
     */
    public SwaggerConfig() {
        super();
    }

    @Bean
    @Scope("singleton")
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2) // Generate a Docket
                .groupName(projectName) // GroupName
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
                .title(projectName) // Title
                .description("Platform's REST API on " + projectName) // Description
                .contact(contact) // Contact
                .version(projectVersion) // Version
                .build(); // Out
    }
}
