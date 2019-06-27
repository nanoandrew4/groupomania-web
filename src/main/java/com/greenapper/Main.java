package com.greenapper;

import com.greenapper.handlers.CampaignFormHandlerMethodArgumentResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
@ComponentScan("com.greenapper")
public class Main implements WebMvcConfigurer {

	@Override
	// Adds the custom form argument resolver so that the abstract CampaignForm can be used in controller method signatures
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new CampaignFormHandlerMethodArgumentResolver());
	}

	public static void main(String... args) {
		SpringApplication.run(Main.class, args);
	}
}
