CREATE TABLE characters (
    id BIGSERIAL PRIMARY KEY,
    external_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    image_url VARCHAR(500),
    source VARCHAR(30) NOT NULL,

    CONSTRAINT uk_character_source_external_id
        UNIQUE (external_id, source)
);