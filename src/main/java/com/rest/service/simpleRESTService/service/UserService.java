package com.rest.service.simpleRESTService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.service.simpleRESTService.controller.handler.RestTemplateResponseErrorHandler;
import com.rest.service.simpleRESTService.dto.ErrorResponseDTO;
import com.rest.service.simpleRESTService.dto.UserDTO;
import com.rest.service.simpleRESTService.dto.UserResponseDTO;
import com.rest.service.simpleRESTService.entity.UserCounter;
import com.rest.service.simpleRESTService.repository.UserCounterRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class UserService {
    private final UserCounterRepository userCounterRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserService(UserCounterRepository userCounterRepository, RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.userCounterRepository = userCounterRepository;
        this.restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @SneakyThrows
    public ResponseEntity getUserInfo(String login) {
        ResponseEntity<String> response = restTemplate.getForEntity("https://api.github.com/users/" + login, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            ErrorResponseDTO errorResponseDTO = objectMapper.readValue(response.getBody(), ErrorResponseDTO.class);
            return ResponseEntity.status(response.getStatusCode().value()).body(errorResponseDTO);
        }
        UserResponseDTO userResponseDTO = objectMapper.readValue(response.getBody(), UserResponseDTO.class);
        updateUserCounterByLogin(login);

        UserDTO userDTO = UserDTO.builder()
                .id(userResponseDTO.getId())
                .login(userResponseDTO.getLogin())
                .name(userResponseDTO.getName())
                .type(userResponseDTO.getType())
                .avatarUrl(userResponseDTO.getAvatarUrl())
                .createdAt(userResponseDTO.getCreatedAt())
                .calculations(makeCalculations(userResponseDTO.getFollowers(), userResponseDTO.getPublicRepos()))
                .build();
        return ResponseEntity.ok(userDTO);
    }

    public ResponseEntity getUserCounter(String login) {
        UserCounter userCounter = userCounterRepository.findById(login)
                .orElse(null);
        if (userCounter == null) {
            ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                    .message("User doesn't exist in database")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
        }
        return ResponseEntity.ok(userCounter);
    }

    private BigDecimal makeCalculations(Integer followers, Integer publicRepos) {
        if (followers.equals(0)) {
            return BigDecimal.valueOf(0);
        }
        BigDecimal result = BigDecimal.valueOf(6).setScale(10, RoundingMode.FLOOR);
        return result.divide(BigDecimal.valueOf(followers), RoundingMode.FLOOR).multiply(BigDecimal.valueOf(2 + publicRepos));
    }

    private void updateUserCounterByLogin(String login) {
        UserCounter userCounter = userCounterRepository.findById(login)
                .orElse(new UserCounter(login, 0));
        userCounter.setRequestCount(userCounter.getRequestCount() + 1);
        userCounterRepository.save(userCounter);
    }
}
