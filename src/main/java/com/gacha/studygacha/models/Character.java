package com.gacha.studygacha.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "characters", uniqueConstraints = {
    @UniqueConstraint(name = "uk_character_source_external_id", columnNames = {"source", "external_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CharacterSource source;
}
