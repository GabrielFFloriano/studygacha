CREATE TABLE collected_characters (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    character_id BIGINT NOT NULL,
    copies INTEGER NOT NULL DEFAULT 1,
    level INTEGER NOT NULL DEFAULT 1,
    obtained_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_collected_characters_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_collected_characters_character
        FOREIGN KEY (character_id)
        REFERENCES characters(id),

    CONSTRAINT uk_collected_character_user_character
        UNIQUE (user_id, character_id),

    CONSTRAINT chk_collected_characters_copies_positive
        CHECK (copies > 0),

    CONSTRAINT chk_collected_characters_level_positive
        CHECK (level > 0)
);