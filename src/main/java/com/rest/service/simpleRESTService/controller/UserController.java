package com.rest.service.simpleRESTService.controller;

import com.rest.service.simpleRESTService.dto.UserDTO;
import com.rest.service.simpleRESTService.entity.UserCounter;
import com.rest.service.simpleRESTService.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "View an information about user", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved information"),
            @ApiResponse(code = 404, message = "User you were trying to reach is not found")
    })
    @GetMapping("/users/{login}")
    ResponseEntity<UserDTO> getUserInfo(@ApiParam(value = "User login", required = true) @PathVariable String login) {
        return userService.getUserInfo(login);
    }

    @ApiOperation(value = "Get user counter entry for user", response = UserCounter.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved entry"),
            @ApiResponse(code = 400, message = "User doesn't exist in database")
    })
    @GetMapping("/users/counter/{login}")
    ResponseEntity<UserCounter> getUserCounter(@ApiParam(value = "User login", required = true) @PathVariable String login) {
        return userService.getUserCounter(login);
    }
}
