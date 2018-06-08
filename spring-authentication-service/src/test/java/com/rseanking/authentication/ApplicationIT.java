package com.rseanking.authentication;

import com.rseanking.user.User;
import com.rseanking.user.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import static com.rseanking.authentication.utils.AuthenticationUtil.buildValidRequestParameters;
import static com.rseanking.authentication.utils.AuthenticationUtil.httpBasicCreds;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ApplicationIT {

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
    public void shouldRejectAuthenticationWithBadCredentials() throws Exception {
        // Given
        MultiValueMap<String, String> params = buildValidRequestParameters(user);
        params.set("password", "invalid_password");

        // When
        ResultActions action = authenticateUser(params);

        // Then
        action.andExpect(jsonPath("$.error", equalTo("invalid_grant")))
                .andExpect(jsonPath("$.error_description", equalTo("Bad credentials")));
    }

    @Test
    public void shouldRejectAuthenticationWithInvalidScope() throws Exception {
        // Given
        MultiValueMap<String, String> params = buildValidRequestParameters(user);
        params.set("scope", "invalid_scope");

        // When
        ResultActions action = authenticateUser(params);

        // Then
        action.andExpect(jsonPath("$.error", equalTo("invalid_scope")))
                .andExpect(jsonPath("$.error_description", equalTo("Invalid scope: invalid_scope")))
                .andExpect(jsonPath("$.scope", equalTo("webclient mobileclient")));
    }

    @Test
    public void shouldRejectAuthenticationWithUnsupportedGrantType() throws Exception {
        // Given
        MultiValueMap<String, String> params = buildValidRequestParameters(user);
        params.set("grant_type", "unsupported_grant_type");

        // When
        ResultActions action = authenticateUser(params);

        // Then
        action.andExpect(jsonPath("$.error", equalTo("unsupported_grant_type")))
                .andExpect(jsonPath("$.error_description", equalTo("Unsupported grant type: unsupported_grant_type")));
    }

    @Test
    public void shouldAcceptAuthentication() throws Exception {
        // Given
        MultiValueMap<String, String> params = buildValidRequestParameters(user);

        ResultActions action = authenticateUser(params);

        // Then
        action.andExpect(jsonPath("$.access_token", notNullValue()))
                .andExpect(jsonPath("$.token_type", equalTo("bearer")))
                .andExpect(jsonPath("$.refresh_token", notNullValue()))
                .andExpect(jsonPath("$.expires_in", equalTo(43199)))
                .andExpect(jsonPath("$.scope", equalTo("webclient")));
    }

    private ResultActions authenticateUser(MultiValueMap<String, String> params) throws Exception {
        final String authenticationUrl = "http://localhost:" + port + "/oauth/token";
        ResultActions perform = mvc.perform(post(authenticationUrl).with(httpBasicCreds()).params(params));
        return perform;
    }

}
