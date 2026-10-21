package com.gacha.studygacha.services;

import com.gacha.studygacha.models.Character;
import com.gacha.studygacha.models.CollectedCharacter;
import com.gacha.studygacha.models.User;
import com.gacha.studygacha.repositories.CollectedCharacterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollectedCharacterService {

    private final CollectedCharacterRepository collectedCharacterRepository;

    @Transactional
    public CollectedCharacter addCharacter(User user, Character character) {
        Optional<CollectedCharacter> existingCollectedCharacter = collectedCharacterRepository
                .findByUserIdAndCharacterId(user.getId(), character.getId());

        if (existingCollectedCharacter.isPresent()) {
            CollectedCharacter collectedCharacter = existingCollectedCharacter.get();

            collectedCharacter.setCopies(collectedCharacter.getCopies() + 1);

            return collectedCharacter;
        }

        CollectedCharacter collectedCharacter = CollectedCharacter.builder()
                .user(user)
                .character(character)
                .copies(1)
                .level(1)
                .build();

        return collectedCharacterRepository.save(collectedCharacter);
    }

}
