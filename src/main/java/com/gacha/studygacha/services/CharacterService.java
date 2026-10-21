package com.gacha.studygacha.services;

import com.gacha.studygacha.dtos.ExternalCharacterData;
import com.gacha.studygacha.models.Character;
import com.gacha.studygacha.repositories.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;

    public Character getOrCreate(ExternalCharacterData data) {
        return characterRepository.findBySourceAndExternalId(data.source(), data.externalId())
                .orElseGet(() -> create(data));
    }

    private Character create(ExternalCharacterData data) {
        Character character = Character.builder()
                .externalId(data.externalId())
                .name(data.name())
                .imageUrl(data.imageUrl())
                .source(data.source())
                .build();

        return characterRepository.save(character);
    }
}
