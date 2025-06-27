package com.naveen.controller;

import com.naveen.entity.User;
import com.naveen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createOrUpdateUser(@RequestBody User user, Authentication authentication) {
        try {
            if(!authentication.getName().equals(user.getClerkId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User doesn't have permission to access the resource");
            }
            return userService.saveOrUpdateUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
