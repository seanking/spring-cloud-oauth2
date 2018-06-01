package com.rseanking.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIT {

	@LocalServerPort
	private int port;

	@Autowired
	private MockMvc mvc;

	@Test
	public void shouldRejectAuthenticationWithBadCredentials() throws Exception {
		// Given
		MultiValueMap<String, String> params = buildValidRequestParameters();
		params.set("password", "invalid_password");

		// When
		ResultActions perform = authenticate(params);

		// Then
		final String body = perform.andReturn().getResponse().getContentAsString();
		Map<String, Object> result = new JacksonJsonParser().parseMap(body);

		assertThat(result.get("error")).isEqualTo("invalid_grant");
		assertThat(result.get("error_description")).isEqualTo("Bad credentials");
	}

	@Test
	public void shouldRejectAuthenticationWithInvalidScope() throws Exception {
		// Given
		MultiValueMap<String, String> params = buildValidRequestParameters();
		params.set("scope", "invalid_scope");

		// When
		ResultActions perform = authenticate(params);

		// Then
		final String body = perform.andReturn().getResponse().getContentAsString();
		Map<String, Object> result = new JacksonJsonParser().parseMap(body);

		assertThat(result.get("error")).isEqualTo("invalid_scope");
		assertThat(result.get("error_description")).isEqualTo("Invalid scope: invalid_scope");
		assertThat(result.get("scope")).isEqualTo("webclient mobileclient");
	}
	
	@Test
	public void shouldRejectAuthenticationWithUnsupportedGrantType() throws Exception {
		// Given
		MultiValueMap<String, String> params = buildValidRequestParameters();
		params.set("grant_type", "unsupported_grant_type");

		// When
		ResultActions perform = authenticate(params);

		// Then
		final String body = perform.andReturn().getResponse().getContentAsString();
		Map<String, Object> result = new JacksonJsonParser().parseMap(body);

		assertThat(result.get("error")).isEqualTo("unsupported_grant_type");
		assertThat(result.get("error_description")).isEqualTo("Unsupported grant type: unsupported_grant_type");
	}

	@Test
	public void shouldAcceptAuthentication() throws Exception {
		// Given
		MultiValueMap<String, String> params = buildValidRequestParameters();

		ResultActions perform = authenticate(params);

		// Then
		final String body = perform.andReturn().getResponse().getContentAsString();
		Map<String, Object> result = new JacksonJsonParser().parseMap(body);

		assertThat(result.get("access_token")).isNotNull();
		assertThat(result.get("token_type")).isEqualTo("bearer");
		assertThat(result.get("refresh_token")).isNotNull();
		assertThat(result.get("expires_in")).isEqualTo(43199);
		assertThat(result.get("scope")).isEqualTo("webclient");
	}
	
	private MultiValueMap<String, String> buildValidRequestParameters() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "password");
		params.add("scope", "webclient");
		params.add("username", "testroleadmin");
		params.add("password", "test");
		
		return params;
	}
	
	private ResultActions authenticate(MultiValueMap<String, String> params) throws Exception {
		final String authenticationUrl = "http://localhost:" + port + "/oauth/token";
		ResultActions perform = mvc.perform(MockMvcRequestBuilders.post(authenticationUrl)
				.with(httpBasic("testclient", "thisisasecret")).params(params));
		return perform;
	}

}
