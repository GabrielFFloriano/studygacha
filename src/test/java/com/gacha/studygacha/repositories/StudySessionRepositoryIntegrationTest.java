package com.gacha.studygacha.repositories;

import com.gacha.studygacha.models.StudySession;
import com.gacha.studygacha.models.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
public class StudySessionRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Test
    void shouldPersistStudySession() {
        User user = userRepository.saveAndFlush(User.builder().username("integration-test").build());

        StudySession session = studySessionRepository.saveAndFlush(StudySession.builder()
                .user(user)
                .startedAt(Instant.parse("2026-10-13T18:00:00Z"))
                .endedAt(Instant.parse("2026-10-13T19:00:00Z"))
                .durationMinutes(60)
                .ticketsEarned(5)
                .build()
        );

        assertNotNull(session.getId());
        assertEquals(user.getId(), session.getUser().getId());
        assertEquals(60, session.getDurationMinutes());
        assertEquals(5, session.getTicketsEarned());
    }

    @Test
    void shouldFindStudySessionWithCorrectUser() {
        User user = userRepository.saveAndFlush(User.builder().username("test-user").build());

        StudySession session = studySessionRepository.saveAndFlush(StudySession.builder()
                .user(user)
                .startedAt(Instant.parse("2026-10-13T18:00:00Z"))
                .endedAt(Instant.parse("2026-10-13T18:30:00Z"))
                .durationMinutes(30)
                .ticketsEarned(2)
                .build()
        );

        StudySession foundSession = studySessionRepository.findById(session.getId()).orElseThrow();

        assertEquals(user.getId(), foundSession.getUser().getId());
        assertEquals("test-user", foundSession.getUser().getUsername());
    }

    @Test
    void shouldRejectStudySessionWithZeroDuration() {
        User user = userRepository.saveAndFlush(User.builder().username("constraint-test").build());

        StudySession invalidSession = StudySession.builder()
                .user(user)
                .startedAt(Instant.parse("2026-10-13T18:00:00Z"))
                .endedAt(Instant.parse("2026-10-13T18:00:00Z"))
                .durationMinutes(0)
                .ticketsEarned(0)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> studySessionRepository.saveAndFlush(invalidSession));
    }
}
