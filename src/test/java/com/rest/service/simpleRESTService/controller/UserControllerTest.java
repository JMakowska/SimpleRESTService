package com.rest.service.simpleRESTService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.service.simpleRESTService.SimpleRestServiceApplication;
import com.rest.service.simpleRESTService.dto.ErrorResponseDTO;
import com.rest.service.simpleRESTService.entity.UserCounter;
import com.rest.service.simpleRESTService.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SimpleRestServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void getUserInfo_UserNotFound() throws Exception {
        String login = "someLogin";
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .message("Not Found")
                .documentationUrl("url")
                .build();
        ResponseEntity response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
        when(userService.getUserInfo(login)).thenReturn(response);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/users/" + login)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(errorResponseDTO)));
    }

    @Test
    public void getUserCounter_isStatusOk() throws Exception {
        String login = "someLogin";
        UserCounter userCounter = UserCounter.builder()
                .login("someLogin")
                .requestCount(2)
                .build();
        ResponseEntity response = ResponseEntity.ok(userCounter);
        when(userService.getUserCounter(login)).thenReturn(response);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/users/counter/" + login)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userCounter)));
    }
}
