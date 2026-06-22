-- ============================================================
--  SMART PLACEMENT MANAGEMENT SYSTEM - DATABASE SCHEMA
-- ============================================================

CREATE TABLE IF NOT EXISTS students (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(120) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    branch        VARCHAR(50),
    cgpa          DOUBLE DEFAULT 0,
    passing_year  INT,
    phone         VARCHAR(20),
    resume_url    VARCHAR(255),
    role          VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS admins (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(120) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS companies (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    website     VARCHAR(200),
    location    VARCHAR(120),
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS jobs (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    title               VARCHAR(150) NOT NULL,
    description         TEXT,
    min_cgpa            DOUBLE DEFAULT 0,
    eligible_branches   VARCHAR(255),
    passing_year        INT,
    job_type            VARCHAR(40),
    location            VARCHAR(120),
    deadline            DATE,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_jobs_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS student_skills (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id  BIGINT NOT NULL,
    skill_name  VARCHAR(80) NOT NULL,
    CONSTRAINT fk_ss_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS job_required_skills (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id      BIGINT NOT NULL,
    skill_name  VARCHAR(80) NOT NULL,
    CONSTRAINT fk_jrs_job FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS applications (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id   BIGINT NOT NULL,
    job_id       BIGINT NOT NULL,
    status       VARCHAR(40) NOT NULL DEFAULT 'APPLIED',
    match_score  INT DEFAULT 0,
    notes        TEXT,
    applied_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_app_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_app_job FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    CONSTRAINT uk_app_student_job UNIQUE (student_id, job_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS interview_rounds (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id  BIGINT NOT NULL,
    round_name      VARCHAR(80) NOT NULL,
    scheduled_at    TIMESTAMP,
    result          VARCHAR(40),
    remarks         TEXT,
    CONSTRAINT fk_ir_app FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE
) ENGINE=InnoDB;
