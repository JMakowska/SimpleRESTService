package com.rest.service.simpleRESTService.controller;

import com.rest.service.simpleRESTService.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.rest.service.simpleRESTService.service.UserService;

@Log4j2
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{login}")
    UserDTO getUserInfo(@PathVariable String login) {
        log.debug("");
        return userService.getUserInfo(login);
    }
}
