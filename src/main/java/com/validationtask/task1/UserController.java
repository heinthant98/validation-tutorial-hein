package com.validationtask.task1;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @PostMapping("/users")
    public Map<String, String> createUser(@RequestBody @Valid UserRequest userRequest) {
        return Map.of("message", "successfully created");
    }
}
