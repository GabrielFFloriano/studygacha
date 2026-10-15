package com.gacha.studygacha.services;

import com.gacha.studygacha.dtos.requests.CreateStudySessionRequest;
import com.gacha.studygacha.dtos.responses.StudySessionResponse;
import com.gacha.studygacha.exceptions.InvalidStudySessionException;
import com.gacha.studygacha.exceptions.UserNotFoundException;
import com.gacha.studygacha.models.StudySession;
import com.gacha.studygacha.models.User;
import com.gacha.studygacha.repositories.StudySessionRepository;
import com.gacha.studygacha.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudySessionServiceTest {

    @Mock
    private StudySessionRepository studySessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudySessionService studySessionService;

    @Test
    void shouldEarnFiveTicketsForSixtyMinutes() {
        User user = User.builder()
                .id(1L)
                .username("test")
                .gachaTickets(0)
                .studyMinutesBalance(0)
                .build();

        CreateStudySessionRequest request = new CreateStudySessionRequest(
                1L,
                Instant.parse("2026-10-12T18:00:00Z"),
                Instant.parse("2026-10-12T19:00:00Z")
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studySessionRepository.save(any(StudySession.class))).thenAnswer(invocation -> {
            StudySession session = invocation.getArgument(0);
            session.setId(1L);
            return session;
        });

        StudySessionResponse response = studySessionService.create(request);

        assertEquals(60, response.durationMinutes());
        assertEquals(5, response.ticketsEarned());

        assertEquals(5, user.getGachaTickets());
        assertEquals(0, user.getStudyMinutesBalance());

        verify(userRepository).findById(1L);
        verify(studySessionRepository).save(any(StudySession.class));
    }

    @Test
    void shouldUsePreviousMinuteBalanceWhenCalculatingTickets() {
        User user = User.builder()
                .id(1L)
                .username("test")
                .gachaTickets(3)
                .studyMinutesBalance(10)
                .build();

        CreateStudySessionRequest request = new CreateStudySessionRequest(
                1L,
                Instant.parse("2026-10-12T18:00:00Z"),
                Instant.parse("2026-10-12T18:20:00Z")
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studySessionRepository.save(any(StudySession.class))).thenAnswer(invocation -> {
            StudySession session = invocation.getArgument(0);
            session.setId(1L);
            return session;
        });

        StudySessionResponse response = studySessionService.create(request);

        ArgumentCaptor<StudySession> captor =
                ArgumentCaptor.forClass(StudySession.class);

        verify(studySessionRepository).save(captor.capture());

        StudySession savedSession = captor.getValue();

        // verifica a sessão enviada ao repository
        assertEquals(20, savedSession.getDurationMinutes());
        assertEquals(2, savedSession.getTicketsEarned());
        assertEquals(1L, savedSession.getUser().getId());

        // verifica as alterações no usuário
        assertEquals(5, user.getGachaTickets());
        assertEquals(6, user.getStudyMinutesBalance());

        // verifica a resposta do service
        assertEquals(20, response.durationMinutes());
        assertEquals(2, response.ticketsEarned());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsZero() {
        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        CreateStudySessionRequest request = new CreateStudySessionRequest(
                1L,
                Instant.parse("2026-10-12T18:00:00Z"),
                Instant.parse("2026-10-12T18:00:00Z")
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(InvalidStudySessionException.class, () -> studySessionService.create(request));

        verify(studySessionRepository, never()).save(any(StudySession.class));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        CreateStudySessionRequest request = new CreateStudySessionRequest(
                999L,
                Instant.parse("2026-10-12T18:00:00Z"),
                Instant.parse("2026-10-12T19:00:00Z")
        );

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> studySessionService.create(request));

        verify(studySessionRepository, never()).save(any(StudySession.class));
    }
}
