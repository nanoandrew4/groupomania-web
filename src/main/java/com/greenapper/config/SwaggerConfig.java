package com.greenapper.config;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.greenapper.controllers"))
				.paths(PathSelectors.any())
				.build()
				.produces(Sets.newHashSet("application/json"))
				.consumes(Sets.newHashSet("application/json"))
				.useDefaultResponseMessages(false)
				.apiInfo(apiInfoBuilder());
	}

	private ApiInfo apiInfoBuilder() {
		return new ApiInfo("Groupomania API", "API for Groupomania endpoints", "v1.1",
						   null, null, null, null, Collections.emptyList());
	}
}