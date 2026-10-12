package com.gacha.studygacha.repositories;

import com.gacha.studygacha.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
class UserRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPersistUser() {
        User user = User.builder().username("test").build();

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("test", saved.getUsername());
        assertEquals(0, saved.getGachaTickets());
    }
}
