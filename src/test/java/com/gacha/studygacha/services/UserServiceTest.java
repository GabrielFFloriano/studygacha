package com.gacha.studygacha.services;

import com.gacha.studygacha.dtos.requests.CreateUserRequest;
import com.gacha.studygacha.dtos.responses.UserResponse;
import com.gacha.studygacha.exceptions.UsernameAlreadyExistsException;
import com.gacha.studygacha.models.User;
import com.gacha.studygacha.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser() {
        // ARRANGE
        CreateUserRequest request = new CreateUserRequest("test");

        when(userRepository.existsByUsername("test"))
            .thenReturn(false);

        User savedUser = User.builder()
                .id(1L)
                .username("test")
                .createdAt(Instant.now())
                .build();

        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

        // ACT
        UserResponse response = userService.create(request);

        // ASSERT
        assertEquals(1L, response.id());
        assertEquals("test", response.username());
        assertEquals(0, response.gachaTickets());

        verify(userRepository).existsByUsername("test");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        CreateUserRequest request =
                new CreateUserRequest("gabriel");

        when(userRepository.existsByUsername("gabriel"))
                .thenReturn(true);

        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> userService.create(request)
        );

        verify(userRepository)
                .existsByUsername("gabriel");

        verify(userRepository, never())
                .save(any(User.class));
    }
}
