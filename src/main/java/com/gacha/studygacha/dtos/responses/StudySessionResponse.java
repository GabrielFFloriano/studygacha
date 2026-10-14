package com.gacha.studygacha.dtos.responses;

import java.time.Instant;

public record StudySessionResponse(
       Long id,
       Long userId,
       Instant createdAt,
       Instant endedAt,
       Integer durationMinutes,
       Integer ticketsEarned
) {
}
