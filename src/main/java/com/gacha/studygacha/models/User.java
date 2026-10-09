package com.gacha.studygacha.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Builder.Default
    @Column(name = "gachaTickets", nullable = false)
    private Integer gachaTickets = 0;

    @Builder.Default
    @Column(name = "study_minutes_balance", nullable = false)
    private Integer studyMinutesBalance = 0;

    @Builder.Default
    @Column (name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
