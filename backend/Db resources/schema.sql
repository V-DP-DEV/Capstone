-- =========================================================
-- USERS
-- =========================================================

CREATE TABLE users (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    firstname varchar(255) NOT NULL,
    surname varchar(255) NOT NULL,

    CONSTRAINT uk_users_email
        UNIQUE (email)
);


-- =========================================================
-- INTERVIEW CATEGORIES
-- =========================================================

CREATE TABLE interview_categories (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,

    CONSTRAINT uk_interview_categories_name
        UNIQUE (name)
);


-- =========================================================
-- INTERVIEWS
-- =========================================================

CREATE TABLE interviews (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    interview_name VARCHAR(255) NOT NULL,
    category_id BIGINT UNSIGNED NOT NULL,
    admin_id BIGINT UNSIGNED NOT NULL,
    description TEXT NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    estimated_time_in_minutes INT UNSIGNED NOT NULL,

    CONSTRAINT fk_interviews_category
        FOREIGN KEY (category_id)
        REFERENCES interview_categories(id),

    CONSTRAINT fk_interviews_admin
        FOREIGN KEY (admin_id)
        REFERENCES users(id)
);

-- =========================================================
-- QUESTION TYPES
-- =========================================================

CREATE TABLE question_types (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,

    CONSTRAINT uk_question_types_name
        UNIQUE (name)
);

-- =========================================================
-- INTERVIEW QUESTIONS
-- =========================================================

CREATE TABLE interview_questions (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    interview_id BIGINT UNSIGNED NOT NULL,
    admin_id BIGINT UNSIGNED NOT NULL,
    type_id BIGINT UNSIGNED NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    text TEXT NOT NULL,
    answer TEXT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_interview_questions_interview
        FOREIGN KEY (interview_id)
        REFERENCES interviews(id),

    CONSTRAINT fk_interview_questions_admin
        FOREIGN KEY (admin_id)
        REFERENCES users(id),
    CONSTRAINT fk_interview_questions_type
        FOREIGN KEY (type_id)
        REFERENCES question_types(id)
);


-- =========================================================
-- CONCEPTS
-- =========================================================

CREATE TABLE concepts (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,

    CONSTRAINT uk_concepts_name
        UNIQUE (name)
);


-- =========================================================
-- INTERVIEW QUESTION -> CONCEPT
-- =========================================================

CREATE TABLE interview_question_concepts (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT UNSIGNED NOT NULL,
    concept_id BIGINT UNSIGNED NOT NULL,

    CONSTRAINT fk_interview_question_concepts_question
        FOREIGN KEY (question_id)
        REFERENCES interview_questions(id),

    CONSTRAINT fk_interview_question_concepts_concept
        FOREIGN KEY (concept_id)
        REFERENCES concepts(id),

    CONSTRAINT uk_interview_question_concepts_question_concept
        UNIQUE (question_id, concept_id)
);


-- =========================================================
-- COMPETENCIES
-- =========================================================

CREATE TABLE competencies (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,

    CONSTRAINT uk_competencies_name
        UNIQUE (name)
);


-- =========================================================
-- COMPETENCY BREAKDOWNS
-- =========================================================

CREATE TABLE competency_breakdowns (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    competency_id BIGINT UNSIGNED NOT NULL,
    name VARCHAR(255) NOT NULL,

    CONSTRAINT fk_competency_breakdowns_competency
        FOREIGN KEY (competency_id)
        REFERENCES competencies(id),

    CONSTRAINT uk_competency_breakdowns_competency_name
        UNIQUE (competency_id, name)
);

-- =========================================================
-- INTERVIEW QUESTION -> COMPETENCY BREAKDOWN
-- =========================================================

CREATE TABLE question_type_competency_breakdowns (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    question_type_id BIGINT UNSIGNED NOT NULL,
    competency_breakdown_id BIGINT UNSIGNED NOT NULL,

    CONSTRAINT fk_qtcb_question_type
        FOREIGN KEY (question_type_id)
        REFERENCES question_types(id),

    CONSTRAINT fk_qtcb_competency_breakdown
        FOREIGN KEY (competency_breakdown_id)
        REFERENCES competency_breakdowns(id),

    CONSTRAINT uk_qtcb_question_type_breakdown
        UNIQUE (
            question_type_id,
            competency_breakdown_id
        )
);


-- =========================================================
-- INTERVIEW USER ATTEMPTS
-- =========================================================

CREATE TABLE interview_user_attempts (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    interview_id BIGINT UNSIGNED NOT NULL,
    completed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    score DECIMAL(5,2),
    status ENUM('NOT_REQUESTED', 'PROCESSING', 'COMPLETED', 'FAILED') NOT NULL DEFAULT 'NOT_REQUESTED',

    CONSTRAINT fk_interview_user_attempts_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_interview_user_attempts_interview
        FOREIGN KEY (interview_id)
        REFERENCES interviews(id)
);


-- =========================================================
-- INTERVIEW QUESTION ATTEMPTS
-- =========================================================

CREATE TABLE interview_question_attempts (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_attempt_id BIGINT UNSIGNED NOT NULL,
    question_id BIGINT UNSIGNED NOT NULL,
    answer TEXT NOT NULL,
    time_taken INT UNSIGNED NOT NULL,

    CONSTRAINT fk_interview_question_attempts_user_attempt
        FOREIGN KEY (user_attempt_id)
        REFERENCES interview_user_attempts(id),

    CONSTRAINT fk_interview_question_attempts_question
        FOREIGN KEY (question_id)
        REFERENCES interview_questions(id),

    CONSTRAINT uk_interview_question_attempts_attempt_question
        UNIQUE (user_attempt_id, question_id)
);


-- =========================================================
-- QUESTION FEEDBACKS
-- =========================================================

CREATE TABLE question_feedbacks (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    question_attempt_id BIGINT UNSIGNED NOT NULL,
    concept_id BIGINT UNSIGNED NOT NULL,
    score DECIMAL(5,2) NOT NULL,

    CONSTRAINT fk_question_feedbacks_question_attempt
        FOREIGN KEY (question_attempt_id)
        REFERENCES interview_question_attempts(id),

    CONSTRAINT fk_question_feedbacks_concept
        FOREIGN KEY (concept_id)
        REFERENCES concepts(id),

    CONSTRAINT uk_question_feedbacks_attempt_concept
        UNIQUE (question_attempt_id, concept_id)
);


-- =========================================================
-- COMPETENCY BREAKDOWN SCORES
-- =========================================================

CREATE TABLE competency_breakdown_scores (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    attempt_id BIGINT UNSIGNED NOT NULL,
    competency_breakdown_id BIGINT UNSIGNED NOT NULL,
    strength TEXT NOT NULL,
    improvements TEXT NOT NULL,

    CONSTRAINT fk_competency_breakdown_scores_attempt
        FOREIGN KEY (attempt_id)
        REFERENCES interview_user_attempts(id),

    CONSTRAINT fk_competency_breakdown_scores_competency_breakdown
        FOREIGN KEY (competency_breakdown_id)
        REFERENCES competency_breakdowns(id),

    CONSTRAINT uk_competency_breakdown_scores_attempt_breakdown
        UNIQUE (attempt_id, competency_breakdown_id)
);


-- =========================================================
-- QUESTION COMPETENCY BREAKDOWN SCORES
-- =========================================================

CREATE TABLE question_competency_breakdown_scores (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    question_attempt_id BIGINT UNSIGNED NOT NULL,
    competency_breakdown_id BIGINT UNSIGNED NOT NULL,
    score DECIMAL(5,2) NOT NULL,

    CONSTRAINT fk_question_competency_breakdown_scores_question_attempt
        FOREIGN KEY (question_attempt_id)
        REFERENCES interview_question_attempts(id),

    CONSTRAINT fk_question_competency_breakdown_scores_competency_breakdown
        FOREIGN KEY (competency_breakdown_id)
        REFERENCES competency_breakdowns(id),

    CONSTRAINT uk_question_competency_breakdown_scores_attempt_breakdown
        UNIQUE (
            question_attempt_id,
            competency_breakdown_id
        )
);

-- =========================================================
-- AUTH TOKENS
-- =========================================================

CREATE TABLE auth_tokens (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,

    token VARCHAR(512) NOT NULL,
    device_id VARCHAR(255) NULL,
    device_name VARCHAR(255) NULL,

    given_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NOT NULL,
    revoked_at DATETIME NULL,

    CONSTRAINT uk_auth_tokens_token
        UNIQUE (token),

    CONSTRAINT fk_auth_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);


-- =========================================================
-- REFRESH TOKENS
-- =========================================================

CREATE TABLE refresh_tokens (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,

    token_hash CHAR(64) NOT NULL,

    device_id VARCHAR(255) NULL,
    device_name VARCHAR(255) NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NOT NULL,
    revoked_at DATETIME NULL,

    CONSTRAINT uk_refresh_tokens_token_hash
        UNIQUE (token_hash),

    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- =========================================================
-- AUTH LOGS
-- =========================================================

CREATE TABLE auth_logs (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NULL,
    token_id BIGINT UNSIGNED NULL,
    ip_address VARCHAR(45) NOT NULL,
    action VARCHAR(100) NOT NULL,
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_auth_log_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_auth_log_token
        FOREIGN KEY (token_id)
        REFERENCES auth_tokens(id)
        ON DELETE SET NULL
);


-- =========================================================
-- AUDIT LOGS
-- =========================================================

CREATE TABLE audit_logs (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NULL,
    action VARCHAR(100) NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT UNSIGNED NOT NULL,
    at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_logs_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);
