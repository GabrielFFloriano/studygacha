package com.gacha.studygacha.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateStudySessionRequest(
        @NotNull
        Long userId,

        @NotNull
        Instant startedAt,

        @NotNull
        Instant endedAt
) {
}
