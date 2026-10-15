ALTER TABLE study_sessions
ADD CONSTRAINT uk_study_session_user_period
UNIQUE (user_id, started_at, ended_at)