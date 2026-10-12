package com.gacha.studygacha.dtos.responses;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponse(
        int status,
        String error,
        Map<String, String> fields,
        Instant timestamp
) {
}
