package com.gacha.studygacha.repositories;

import com.gacha.studygacha.models.Character;
import com.gacha.studygacha.models.CharacterSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findBySourceAndExternalId(CharacterSource source, Long externalId);
}
