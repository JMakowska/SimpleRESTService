package com.rest.service.simpleRESTService.service;

import com.rest.service.simpleRESTService.dto.UserResponseDTO;
import com.rest.service.simpleRESTService.dto.UserDTO;
import com.rest.service.simpleRESTService.entity.UserCounter;
import com.rest.service.simpleRESTService.repository.UserCounterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Log4j2
@Service
@AllArgsConstructor
public class UserService {
    private final UserCounterRepository userCounterRepository;
    private final RestTemplate restTemplate;

    public UserDTO getUserInfo(String login) {
        ResponseEntity<UserResponseDTO> response = restTemplate.getForEntity("https://api.github.com/users/" + login, UserResponseDTO.class);
        if (!response.getStatusCode().equals(200)) {
            log.debug("Not found");
            // TODO: Dodać obiekt z message i go zwrócić
        }
        UserResponseDTO userResponseDTO = response.getBody();
        UserCounter userCounter = userCounterRepository.findById(login)
                .orElse(new UserCounter(login, 0));
        userCounter.setRequestCount(userCounter.getRequestCount()+1);
        userCounterRepository.save(userCounter);

        return UserDTO.builder()
                .id(userResponseDTO.getId())
                .login(userResponseDTO.getLogin())
                .name(userResponseDTO.getName())
                .type(userResponseDTO.getType())
                .avatarUrl(userResponseDTO.getAvatarUrl())
                .createdAt(userResponseDTO.getCreatedAt())
                .calculations(makeCalculations(userResponseDTO.getFollowers(), userResponseDTO.getPublicRepos()))
                .build();
    }

    private BigDecimal makeCalculations(Integer followers, Integer publicRepos) {
        BigDecimal result = BigDecimal.valueOf(6).setScale(10, RoundingMode.FLOOR);
        return result.divide(BigDecimal.valueOf(followers), RoundingMode.FLOOR).multiply(BigDecimal.valueOf(2 + publicRepos));
    }
}
