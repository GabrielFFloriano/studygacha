package com.gacha.studygacha.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateStudySessionRequest(
        @NotNull(message = "user Id is required")
        Long userId,

        @NotNull(message = "session start timestamp is required")
        Instant startedAt,

        @NotNull(message = "session end timestamp is required")
        Instant endedAt
) {
}
