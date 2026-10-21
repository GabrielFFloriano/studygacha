package com.gacha.studygacha.repositories;

import com.gacha.studygacha.models.CollectedCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectedCharacterRepository extends JpaRepository<CollectedCharacter, Long> {
    Optional<CollectedCharacter> findByUserIdAndCharacterId(Long userId, Long characterId);

    List<CollectedCharacter> findAllByUserId(Long userId);
}
