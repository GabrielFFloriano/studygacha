package com.gacha.studygacha.dtos;

import com.gacha.studygacha.models.CharacterSource;

public record ExternalCharacterData(
        Long externalId,
        String name,
        String imageUrl,
        CharacterSource source
) {
}