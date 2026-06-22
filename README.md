<div align="center">

# 🎓 PlacementPro

### Smart Placement & Internship Management System

A full-stack web application that helps students discover eligible internships through intelligent matching and enables placement cells to manage the complete recruitment pipeline.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![JWT](https://img.shields.io/badge/JWT-Secured-red)
![Swagger](https://img.shields.io/badge/API-Swagger-success)

</div>

---

## 🚀 Overview

PlacementPro digitizes the entire campus recruitment process by replacing spreadsheets, forms, and manual shortlisting with a centralized platform.

### Key Capabilities

- 🎯 Smart eligibility validation (CGPA, branch, passing year, and skills)
- 📊 Skill match scoring for every application
- 🔐 Secure JWT-based authentication & authorization
- 👨‍🎓 Student portal for applications and tracking
- 🧑‍💼 Admin/TPO dashboard for recruitment management
- 📈 Recruitment analytics and insights
- 📤 CSV export for applicant shortlisting

---

## ✨ Features

### 👨‍🎓 Student Portal

- Secure registration and login
- Academic profile management
- Skills and resume management
- Browse eligible opportunities
- View skill match percentage
- Apply for internships and jobs
- Track application progress

### 🧑‍💼 Admin / Placement Cell

- Manage companies and job postings
- Configure eligibility criteria
- Review ranked applicants
- Update recruitment status
- Access placement analytics
- Export applicant data to CSV

---

## 🛠️ Tech Stack

| Layer | Technology |
|---------|------------|
| Backend | Java 17, Spring Boot 3 |
| Security | Spring Security, JWT |
| Database | MySQL 8 |
| Data Access | JDBC / JdbcTemplate |
| API Documentation | Swagger (OpenAPI) |
| Frontend | Bootstrap 5, JavaScript |
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

### Design Highlights

- Layered Architecture
- DTO-Based API Design
- Global Exception Handling
- Stateless JWT Authentication
- Role-Based Access Control
- SQL Injection Protection
- BCrypt Password Hashing

---

## 🧠 Smart Features

### 🎯 Eligibility Engine

Before allowing an application, the system validates:

- Minimum CGPA
- Eligible branch
- Passing year
- Required skills

Applications that fail eligibility checks are rejected at the backend level.

### 📊 Skill Match Scoring

```text
Match Score =
(Matched Skills ÷ Required Skills) × 100
```

The system calculates:

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

<img width="1918" height="1097" alt="Screenshot 2026-06-21 213821" src="https://github.com/user-attachments/assets/d53ceb67-c45f-4855-aaf2-a69702397df8" />

### Student Dashboard

<img width="1918" height="1102" alt="Screenshot 2026-06-21 213904" src="https://github.com/user-attachments/assets/19660cb6-2d6a-48a4-b826-eaa130ffea27" />

### Job Matching Page

<img width="1918" height="1102" alt="Screenshot 2026-06-21 213949" src="https://github.com/user-attachments/assets/facaa717-5e50-419f-afa7-40b29975809e" />

### Admin Dashboard

<img width="1918" height="1097" alt="Screenshot 2026-06-21 214113" src="https://github.com/user-attachments/assets/9681d983-6548-47fa-9690-297202471a24" />

### Application Management

<img width="1917" height="1097" alt="Screenshot 2026-06-21 214241" src="https://github.com/user-attachments/assets/35c882c0-6d12-4395-a42c-253c9738bb1a" />

---

## ⚡ Quick Start

### Clone Repository

```bash
git clone https://github.com/VinamraGupta01/PlacementPro.git
cd PlacementPro
```

### Configure Database

Update your database credentials in:

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

### Access Application

```text
Application:
http://localhost:8080

Swagger API Documentation:
http://localhost:8080/swagger-ui.html
```

---

## 📡 API Documentation

Interactive Swagger documentation is available at:

```text
http://localhost:8080/swagger-ui.html
```

Includes:

- Authentication APIs
- Student APIs
- Job APIs
- Admin APIs

---

## 🌟 Engineering Highlights

- JWT Authentication & Authorization
- RESTful API Design
- Layered Architecture
- DTO Mapping
- JdbcTemplate Data Access
- OpenAPI Documentation
- Global Error Handling
- Production-Ready Security Practices

---

## 🚀 Future Enhancements

- Email Notifications
- Resume Parsing
- Chart-Based Analytics
- Docker Support
- Multi-College Architecture
- Cloud Storage Integration

---

## 👨‍💻 Developer

### Vinamra Gupta

**Backend Developer | Java | Spring Boot | MySQL**

GitHub: https://github.com/VinamraGupta01

LinkedIn: https://linkedin.com/in/vinamra-gupta-0aa4b4375

---

<div align="center">

### ⭐ If you found this project interesting, consider giving it a star.

Built with ❤️ using Java, Spring Boot, MySQL, JWT Security, and Bootstrap.

</div>
