package com.gacha.studygacha.dtos.responses;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        Integer gachaTickets,
        Integer studyMinutesBalance,
        Instant createdAt
) {
}
