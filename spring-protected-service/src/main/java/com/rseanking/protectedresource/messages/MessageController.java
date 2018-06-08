package com.rseanking.protectedresource.messages;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping(value = "/adminMessage", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminMessage() {
        return "{\"message\": \"message for the admin!\"}";
    }

    @RequestMapping(value = "/userMessage", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String userMessage() {
        return "{\"message\": \"message for the user!\"}";
    }
}
