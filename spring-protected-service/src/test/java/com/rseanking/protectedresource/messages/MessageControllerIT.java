package com.rseanking.protectedresource.messages;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MessageControllerIT {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldGetAdminMessageForAdmin() throws Exception {
        mockMvc.perform(get("/adminMessage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", equalTo("message for the admin!")));
    }

    @Test(expected = NestedServletException.class)
    @WithMockUser(roles = "USER")
    public void shouldNotGetAdminMessageForUser() throws Exception {
        mockMvc.perform(get("/adminMessage"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldGetUserMessageForUser() throws Exception {
        mockMvc.perform(get("/userMessage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", equalTo("message for the user!")));
    }

    @Test(expected = NestedServletException.class)
    @WithMockUser(roles = "ADMIN")
    public void shouldNotGetUserMessageForAdmin() throws Exception {
        mockMvc.perform(get("/userMessage"));
    }

}