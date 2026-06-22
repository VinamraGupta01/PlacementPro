# 🎓 PlacementPro – Smart Placement & Internship Management System

> A full-stack platform that helps students discover eligible internships/jobs and enables placement cells (TPOs) to manage recruitment, applications, and analytics from a single dashboard.

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/JWT-Secured-red)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## 🚀 Overview

PlacementPro digitizes the campus recruitment process by replacing spreadsheets, forms, and manual shortlisting with a centralized recruitment platform.

### Key Features

- 🎯 Smart Eligibility Checker
- 📊 Skill Match Scoring
- 🔐 JWT Authentication & Authorization
- 👨‍🎓 Student Portal
- 🧑‍💼 Admin / TPO Dashboard
- 📈 Placement Analytics
- 📤 CSV Export Support
- 📚 Swagger API Documentation

---

## ✨ Highlights

### 👨‍🎓 Student Portal

- Secure Registration & Login
- Profile Management
- Skills Management
- Resume Upload
- Browse Eligible Jobs
- Match Score Preview
- Application Tracking

### 🧑‍💼 Admin / Placement Cell

- Manage Companies
- Create Job Openings
- Configure Eligibility Criteria
- Review Ranked Applicants
- Update Recruitment Status
- Export Applicant Data
- View Placement Analytics

---

## 🛠️ Tech Stack

| Layer | Technology |
|---------|------------|
| Backend | Java 17, Spring Boot 3.2 |
| Database | MySQL 8 |
| Data Access | JDBC / JdbcTemplate |
| Security | Spring Security + JWT |
| Frontend | Bootstrap 5 + Vanilla JavaScript |
| API Docs | Swagger (OpenAPI) |
| Build Tool | Maven |

---

## 🏗️ Architecture

```text
Client (Bootstrap + JavaScript)
            │
            ▼
     REST Controllers
            │
            ▼
      Service Layer
(Eligibility • Matching • Analytics)
            │
            ▼
    Repository Layer
      (JdbcTemplate)
            │
            ▼
       MySQL Database
```

### Engineering Highlights

- Layered Architecture
- DTO-Based API Design
- Global Exception Handling
- Role-Based Access Control
- Stateless JWT Authentication
- BCrypt Password Hashing
- Parameterized SQL Queries
- SQL Injection Protection

---

## 🧠 Smart Features

### Eligibility Checker

Before allowing an application, the system validates:

- Minimum CGPA
- Eligible Branch
- Passing Year
- Required Skills

Applications failing eligibility criteria are rejected at the backend level.

### Skill Match Score

```text
Match Score =
(Matched Skills ÷ Required Skills) × 100
```

Provides:

- Match Percentage
- Matched Skills
- Missing Skills

This helps recruiters quickly identify the strongest candidates.

---

## 🗄️ Database Design

### Core Entities

```text
Students
Companies
Jobs
Applications
Skills
Interview Rounds
```

### Database Features

- Foreign Key Constraints
- ON DELETE CASCADE
- Indexed Queries
- Duplicate Application Prevention
- Normalized Relational Design

---

## 📸 Screenshots

### Landing Page

(Add Screenshot)

### Student Dashboard

(Add Screenshot)

### Job Matching

(Add Screenshot)

### Admin Dashboard

(Add Screenshot)

### Application Management

(Add Screenshot)

---

## ⚡ Quick Start

### Clone Repository

```bash
git clone https://github.com/VinamraGupta01/PlacementPro.git
cd PlacementPro
```

### Configure Database

Update:

```properties
src/main/resources/application.properties
```

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Run Application

```bash
mvn spring-boot:run
```

### Access

```text
Application:
http://localhost:8080

Swagger:
http://localhost:8080/swagger-ui.html
```

---

## 📡 API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Includes:

- Authentication APIs
- Student APIs
- Job APIs
- Admin APIs

---

## 🚀 Deployment

### Render

Build Command:

```bash
mvn clean package -DskipTests
```

Start Command:

```bash
java -jar target/*.jar
```

Required Environment Variables:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
APP_JWT_SECRET
```

---

## 🌟 Why JDBC Instead of JPA?

Every SQL query is written manually using JdbcTemplate.

Benefits:

- Full SQL control
- Easier query optimization
- Better understanding of joins and indexing
- Stronger interview discussions

---

## 👨‍💻 Developer

### Vinamra Gupta

Backend Developer | Java | Spring Boot | MySQL

GitHub:
https://github.com/VinamraGupta01

LinkedIn:
https://linkedin.com/in/vinamra-gupta-0aa4b4375

---

## ⭐ Support

If you found this project useful, consider giving it a star.

Built with ❤️ using Java, Spring Boot, MySQL, JWT, and Bootstrap.
