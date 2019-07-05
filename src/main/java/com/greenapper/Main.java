package com.greenapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@ComponentScan("com.greenapper")
public class Main {
	public static void main(String... args) {
		SpringApplication.run(Main.class, args);
	}
}