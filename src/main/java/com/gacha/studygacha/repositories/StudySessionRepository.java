package com.gacha.studygacha.repositories;

import com.gacha.studygacha.models.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    @Query("""
        SELECT COUNT(s) > 0
        FROM StudySession s
        WHERE s.user.id  = :userId
            AND s.startedAt < :endedAt
            AND s.endedAt > :startedAt
    """)
    boolean existsOverlappingSession(
            @Param("userId") Long userId,
            @Param("startedAt") Instant startedAt,
            @Param("endedAt") Instant endedAt
    );
}
