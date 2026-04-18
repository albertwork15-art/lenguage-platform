package com.SafariTalk.lenguage_platform;

import com.SafariTalk.lenguage_platform.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class LenguagePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(LenguagePlatformApplication.class, args);
	}

}
