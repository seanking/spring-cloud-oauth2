package com.rseanking.authentication;

import static org.springframework.security.core.authority.AuthorityUtils.authorityListToSet;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableResourceServer
@EnableAuthorizationServer
public class Application {

	@RequestMapping(value = "/user", produces = "application/json")
	private Map<String, Object> user(OAuth2Authentication user) {
		final Map<String, Object> userInfo = new HashMap<>();

		userInfo.put("user", user.getUserAuthentication().getPrincipal());
		userInfo.put("authorities", authorityListToSet(user.getUserAuthentication().getAuthorities()));

		return userInfo;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}
