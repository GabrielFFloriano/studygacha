package com.gacha.studygacha.services;

import com.gacha.studygacha.dtos.requests.CreateUserRequest;
import com.gacha.studygacha.dtos.responses.UserResponse;
import com.gacha.studygacha.exceptions.UsernameAlreadyExistsException;
import com.gacha.studygacha.models.User;
import com.gacha.studygacha.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }

        User user = User.builder()
                .username(request.username())
                .build();
        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getGachaTickets(),
                savedUser.getStudyMinutesBalance(),
                savedUser.getCreatedAt()
        );
    }
}
