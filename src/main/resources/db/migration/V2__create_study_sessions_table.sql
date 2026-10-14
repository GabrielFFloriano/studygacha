CREATE TABLE study_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ended_at TIMESTAMP WITH TIME ZONE NOT NULL,
    duration_minutes INTEGER NOT NULL,
    tickets_earned INTEGER NOT NULL,

    CONSTRAINT fk_study_sessions_user
       FOREIGN KEY (user_id)
       REFERENCES users(id),

    CONSTRAINT chk_duration_minutes_positive
       CHECK (duration_minutes > 0),

   CONSTRAINT chk_tickets_earned_non_negative
       CHECK (tickets_earned >= 0)
);