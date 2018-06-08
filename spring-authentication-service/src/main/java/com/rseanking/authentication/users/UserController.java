package com.rseanking.authentication.users;

import static org.springframework.security.core.authority.AuthorityUtils.authorityListToSet;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping(value = "/user", produces = "application/json")
    private Map<String, Object> user(OAuth2Authentication user) {
        final Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("user", user.getUserAuthentication().getPrincipal());
        userInfo.put("authorities", authorityListToSet(user.getUserAuthentication().getAuthorities()));

        return userInfo;
    }

}
