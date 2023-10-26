package com.secretkeeper.handlers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainHandler {
    @GetMapping("/users")
    public String usersEndPoint() {
        return "ONLY users can see this";
    }
}
