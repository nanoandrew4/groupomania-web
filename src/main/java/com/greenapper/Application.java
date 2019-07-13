package com.greenapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@SpringBootApplication
@EnableResourceServer
@ComponentScan("com.greenapper")
public class Application extends ResourceServerConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = {
			"/v2/api-docs",
			"/swagger-resources",
			"/swagger-resources/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui.html",
			"/webjars/**"
	};

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().
				antMatchers(AUTH_WHITELIST).permitAll().  // whitelist Swagger UI resources
				antMatchers("/**").authenticated();  // require authentication for any endpoint that's not whitelisted
	}

	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}
}