package com.gacha.studygacha.services;

import com.gacha.studygacha.dtos.requests.CreateStudySessionRequest;
import com.gacha.studygacha.dtos.responses.StudySessionResponse;
import com.gacha.studygacha.exceptions.InvalidStudySessionException;
import com.gacha.studygacha.exceptions.OverlappingStudySessionException;
import com.gacha.studygacha.exceptions.UserNotFoundException;
import com.gacha.studygacha.models.StudySession;
import com.gacha.studygacha.models.User;
import com.gacha.studygacha.repositories.StudySessionRepository;
import com.gacha.studygacha.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class StudySessionService {

    private static final int MINUTES_PER_TCIKETS = 12;

    private final StudySessionRepository studySessionRepository;
    private final UserRepository userRepository;

    @Transactional
    public StudySessionResponse create(CreateStudySessionRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId()));

        long minutes = Duration.between(request.startedAt(), request.endedAt()).toMinutes();

        if (minutes <= 0 || minutes > 600) {
            throw new InvalidStudySessionException("Study session duration must be between 1 and 600 minutes");
        }

        if (studySessionRepository.existsOverlappingSession(request.userId(), request.startedAt(), request.endedAt())) {
            throw new OverlappingStudySessionException("Study session overlaps an existing session");
        }

        int totalMinutes = user.getStudyMinutesBalance() + Math.toIntExact(minutes);
        int ticketsEarned = totalMinutes / MINUTES_PER_TCIKETS;
        int remainingMinutes = totalMinutes %  MINUTES_PER_TCIKETS;

        user.setGachaTickets(user.getGachaTickets() + ticketsEarned);
        user.setStudyMinutesBalance(remainingMinutes);

        StudySession session = StudySession.builder()
                .user(user)
                .startedAt(request.startedAt())
                .endedAt(request.endedAt())
                .durationMinutes(Math.toIntExact(minutes))
                .ticketsEarned(ticketsEarned)
                .build();

        StudySession saved = studySessionRepository.save(session);

        return new StudySessionResponse(
                saved.getId(),
                user.getId(),
                saved.getStartedAt(),
                saved.getEndedAt(),
                saved.getDurationMinutes(),
                saved.getTicketsEarned()
        );
    }
}
