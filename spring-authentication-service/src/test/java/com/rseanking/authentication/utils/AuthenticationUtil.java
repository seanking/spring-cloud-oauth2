package com.rseanking.authentication.utils;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.rseanking.user.User;

public class AuthenticationUtil {
	
	static public RequestPostProcessor httpBasicCreds() {
		return SecurityMockMvcRequestPostProcessors.httpBasic("testclient", "thisisasecret");
	}

	static public MultiValueMap<String, String> buildValidRequestParameters(final User user) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "password");
		params.add("scope", "webclient");
		params.add("username", user.getUsername());
		params.add("password", user.getPasword().replace("{noop}", ""));
		
		return params;
	}
}
