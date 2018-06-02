package com.rseanking.authentication.users;

import static com.rseanking.authentication.utils.AuthenticationUtil.buildValidRequestParameters;
import static com.rseanking.authentication.utils.AuthenticationUtil.httpBasicCreds;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.rseanking.user.User;
import com.rseanking.user.UserRepository;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserControllerIT {
	@LocalServerPort
	private int port;
	@Autowired
	private MockMvc mvc;
	@Autowired
	private UserRepository userRepository;

	private User user;

	@Before
	public void setUp() {
		user = new User();
		user.setUsername("testroleadmin");
		user.setPasword("{noop}test");
		userRepository.save(user);
	}

	@After
	public void tearDown() {
		userRepository.deleteAll();
	}

	@Test
	public void shouldGetUserData() throws Exception {
		// Given
		final String token = authenticateUser(user);
		final String userUrl = "http://localhost:" + port + "/user";
		final ResultActions userResultAction = mvc.perform(get(userUrl).header("Authorization", token));

		// Then
		userResultAction.andExpect(jsonPath("$.user.user.username", equalTo(user.getUsername())));
	}

	private String authenticateUser(final User user) throws Exception, UnsupportedEncodingException {
		final String authenticationUrl = "http://localhost:" + port + "/oauth/token";
		final ResultActions authenticationActionResult = mvc.perform(post(authenticationUrl)
				.with(httpBasicCreds()).params(buildValidRequestParameters(user)));

		final String authenticationBody = authenticationActionResult.andReturn().getResponse().getContentAsString();

		Map<String, Object> authenticationResult = new JacksonJsonParser().parseMap(authenticationBody);

		return "Bearer " + (String) authenticationResult.get("access_token");
	}

}
