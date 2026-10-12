package com.gacha.studygacha.controllers;

import com.gacha.studygacha.dtos.requests.CreateUserRequest;
import com.gacha.studygacha.dtos.responses.UserResponse;
import com.gacha.studygacha.exceptions.UsernameAlreadyExistsException;
import com.gacha.studygacha.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {
        UserResponse response = new UserResponse(
                1L,
                "test",
                0,
                0,
                Instant.now()
        );

        when(userService.create(any(CreateUserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                              "username": "test"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.gachaTickets").value(0));
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                        {
                          "username": ""
                        }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturnConflictWhenUsernameAlreadyExists() throws Exception {
        when(userService.create(any(CreateUserRequest.class)))
                .thenThrow(new UsernameAlreadyExistsException("test"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "username": "test"
                            }
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("USERNAME_ALREADY_EXISTS"));
    }
}
