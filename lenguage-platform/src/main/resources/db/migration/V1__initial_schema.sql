-- Esquema inicial (PostgreSQL). Alineado con entidades JPA / SpringPhysicalNamingStrategy.

CREATE TABLE user_account_entity (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP(6),
    CONSTRAINT uk_user_account_email UNIQUE (email)
);

-- @MapsId + userAccount: columna FK/PK user_account_id
CREATE TABLE student_profiles (
    user_account_id BIGINT NOT NULL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255) NOT NULL,
    target_language VARCHAR(255) NOT NULL,
    native_language VARCHAR(255) NOT NULL,
    total_minutes_balance INTEGER,
    CONSTRAINT fk_student_user_account FOREIGN KEY (user_account_id) REFERENCES user_account_entity (id)
);

-- @MapsId + JoinColumn user_id
CREATE TABLE tutor_profiles (
    user_id BIGINT NOT NULL PRIMARY KEY,
    bio TEXT,
    video_intro_url VARCHAR(255),
    hourly_rate DOUBLE PRECISION,
    rating DOUBLE PRECISION,
    is_certified BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_tutor_user_account FOREIGN KEY (user_id) REFERENCES user_account_entity (id)
);

CREATE TABLE subscription_plans (
    id BIGSERIAL PRIMARY KEY,
    basic VARCHAR(255) NOT NULL,
    weekly_minutes INTEGER NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_subscription_plans_basic UNIQUE (basic)
);

CREATE TABLE availabilities (
    id BIGSERIAL PRIMARY KEY,
    start_time TIMESTAMP(6) NOT NULL,
    end_time TIMESTAMP(6) NOT NULL,
    is_booked BOOLEAN NOT NULL DEFAULT FALSE,
    tutor_id BIGINT NOT NULL,
    CONSTRAINT fk_availability_tutor FOREIGN KEY (tutor_id) REFERENCES tutor_profiles (user_id)
);

CREATE INDEX idx_tutor_start ON availabilities (tutor_id, start_time);

CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    tutor_id BIGINT NOT NULL,
    start_time TIMESTAMP(6) NOT NULL,
    end_time TIMESTAMP(6) NOT NULL,
    status VARCHAR(50) NOT NULL,
    meeting_link VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_lesson_student FOREIGN KEY (student_id) REFERENCES student_profiles (user_account_id),
    CONSTRAINT fk_lesson_tutor FOREIGN KEY (tutor_id) REFERENCES tutor_profiles (user_id)
);

CREATE TABLE student_subscriptions (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    end_date TIMESTAMP(6) NOT NULL,
    status VARCHAR(50) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_sub_student FOREIGN KEY (student_id) REFERENCES student_profiles (user_account_id),
    CONSTRAINT fk_sub_plan FOREIGN KEY (plan_id) REFERENCES subscription_plans (id)
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    plan_id BIGINT,
    amount NUMERIC(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(50) NOT NULL,
    external_reference VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6),
    CONSTRAINT uk_transactions_external_reference UNIQUE (external_reference),
    CONSTRAINT fk_transaction_plan FOREIGN KEY (plan_id) REFERENCES subscription_plans (id)
);

CREATE INDEX idx_transaction_status ON transactions (status);
