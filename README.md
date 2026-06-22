# 🎓 PlacementPro – Smart Placement & Internship Management System

> A premium, full-stack platform that helps **students** discover eligible internships/jobs and helps **placement cells (TPOs)** manage companies, applications and analytics — built with **Spring Boot + JDBC + MySQL + Bootstrap**.

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## ✨ Highlights

- 🎯 **Smart Eligibility Checker** – auto-validates CGPA, branch, passing year and required skills before a student can apply.
- 📊 **Resume / Skill Match Score** – every application gets a 0–100% match score with matched/missing skill breakdown.
- 🧑‍💼 **Two-role system** – clean, separate experience for **students** and **admin/TPO**.
- 📈 **Analytics dashboard** – application pipeline, applications-per-company, branch-wise selections.
- 📤 **CSV export** of applicants for offline review.
- 🔐 **JWT-based authentication** + Spring Security.
- 📚 **Swagger / OpenAPI** docs out of the box.
- 💅 **Premium UI** built with Bootstrap 5 + custom design tokens (no heavy frameworks needed).

---

## 🖼️ Screens

| Landing | Student Dashboard | Admin Analytics |
|---|---|---|
| Hero, live jobs, partner companies | Eligible jobs, applications status, profile snapshot | Pipeline + per-company stats + CSV export |

> Open the app to see the polished UI – it's intentionally light and fast.

---

## 🧱 Tech Stack

| Layer        | Tech                                                  |
|--------------|--------------------------------------------------------|
| Backend      | Java 17, Spring Boot 3.2                              |
| Persistence  | **JDBC / JdbcTemplate** + MySQL 8                     |
| Security     | Spring Security + **JWT** (jjwt)                      |
| Frontend     | HTML + Bootstrap 5 + Vanilla JS (no build step)       |
| Docs         | springdoc-openapi (Swagger UI)                        |
| Build        | Maven                                                 |

> No JPA/Hibernate — pure SQL via `JdbcTemplate` so you can **explain every query** in interviews.

---

## 🚀 Quick Start (Local)

### 1. Prerequisites

- **Java 17+**  &nbsp;→ `java -version`
- **Maven 3.8+** &nbsp;→ `mvn -version`
- **MySQL 8.x** running on `localhost:3306` (or a Docker container)
- Git

### 2. Clone & configure

```bash
git clone https://github.com/<your-username>/placement-management-system.git
cd placement-management-system
```

Open `src/main/resources/application.properties` and update the MySQL username/password if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/placement_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

> The database `placement_db` is **created automatically** on first run; tables are created via `schema.sql`; demo data is seeded by `DataInitializer.java`.

### 3. Run it

```bash
mvn spring-boot:run
```

Then open:

| URL                                    | What                          |
|----------------------------------------|-------------------------------|
| http://localhost:8080                  | 🌐 Landing page                |
| http://localhost:8080/pages/login.html | 🔐 Login                       |
| http://localhost:8080/pages/admin.html | 🧑‍💼 Admin dashboard (after login) |
| http://localhost:8080/swagger-ui.html  | 📚 Swagger API docs            |

### 4. Demo accounts

| Role    | Email                | Password    |
|---------|----------------------|-------------|
| Admin   | `admin@college.edu`  | `admin123`  |
| Student | `aarav@student.edu`  | `student123`|
| Student | `priya@student.edu`  | `student123`|
| Student | `rohan@student.edu`  | `student123`|

---

## 🐳 Run MySQL via Docker (optional)

```bash
docker run --name placement-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=placement_db \
  -p 3306:3306 -d mysql:8
```

---

## 🗃️ Database Schema

```
students            (id, name, email, password, branch, cgpa, passing_year, phone, resume_url, role, created_at)
admins              (id, name, email, password, role, created_at)
companies           (id, name, website, location, description, created_at)
jobs                (id, company_id, title, description, min_cgpa, eligible_branches, passing_year, job_type, location, deadline, created_at)
student_skills      (id, student_id, skill_name)
job_required_skills (id, job_id, skill_name)
applications        (id, student_id, job_id, status, match_score, notes, applied_at, updated_at)
interview_rounds    (id, application_id, round_name, scheduled_at, result, remarks)
```

All foreign keys use `ON DELETE CASCADE` for clean data lifecycle.

---

## 📡 Main REST APIs

### Auth
| Method | URL                       | Body / Notes                        |
|--------|---------------------------|-------------------------------------|
| POST   | `/api/auth/register`      | Student registration                |
| POST   | `/api/auth/login`         | Student or admin login → JWT        |

### Students
| Method | URL                                       |
|--------|--------------------------------------------|
| GET    | `/api/students`                            |
| GET    | `/api/students/{id}`                       |
| PUT    | `/api/students/{id}`                       |
| POST   | `/api/students/{id}/skills`                |
| POST   | `/api/students/{id}/resume`  *(multipart)* |
| GET    | `/api/students/{id}/applications`          |

### Jobs
| Method | URL                                  |
|--------|---------------------------------------|
| GET    | `/api/jobs`                          |
| GET    | `/api/jobs/{id}`                     |
| POST   | `/api/jobs`                          |
| DELETE | `/api/jobs/{id}`                     |
| GET    | `/api/jobs/eligible/{studentId}`     |
| GET    | `/api/jobs/{jobId}/match/{studentId}`|
| POST   | `/api/jobs/{jobId}/apply/{studentId}`|

### Admin
| Method | URL                                          |
|--------|-----------------------------------------------|
| POST   | `/api/admin/companies`                       |
| GET    | `/api/admin/companies`                       |
| DELETE | `/api/admin/companies/{id}`                  |
| GET    | `/api/admin/applications`                    |
| PUT    | `/api/admin/applications/{id}/status`        |
| PUT    | `/api/admin/applications/{id}/notes`         |
| GET    | `/api/admin/dashboard`                       |
| GET    | `/api/admin/export/applications`             |

Full schema is available in Swagger.

---

## 🧠 How the “Smart” parts work

### Eligibility Checker (`JobService.isEligible`)

```text
1. student.cgpa  >=  job.minCgpa
2. student.branch ∈ job.eligibleBranches  (CSV match)
3. student.passingYear == job.passingYear (when set)
4. compute skill match score (matched / required * 100)
```

Result is exposed both via `GET /api/jobs/eligible/{studentId}` (list)
and `GET /api/jobs/{jobId}/match/{studentId}` (detailed score).

### Skill Match (`JobService.computeMatch`)

Simple, case-insensitive intersection between **student skills** and **required skills**.
Returns `matchScore` + `matchedSkills` + `missingSkills` — perfect to explain in interviews
without invoking AI/ML buzzwords.

---

## 🌐 Push to GitHub

```bash
cd placement-management-system
git init
git add .
git commit -m "feat: initial commit – Smart Placement Management System"
git branch -M main
git remote add origin https://github.com/<your-username>/placement-management-system.git
git push -u origin main
```

> Don't forget to update the badge / clone URL in this README after pushing.

---

## ☁️ Deploy (optional)

- **Render / Railway**: build with `mvn package`, run `java -jar target/placement-management-system-1.0.0.jar`.
- Add MySQL as a managed addon, then set these env vars:
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`
  - `APP_JWT_SECRET` (any base64 string ≥ 32 bytes)

---

## 🗣️ Talking Points for Interviews

- “I used **JdbcTemplate** over JPA so I had **full control over every SQL query** — including joins for the analytics dashboard and the eligibility checker.”
- “I implemented a **smart eligibility checker** that runs **before** an application is created, enforcing it at the **service layer** instead of relying on the UI.”
- “Authentication is **JWT-based** with role-based authorization (`ROLE_STUDENT` / `ROLE_ADMIN`).”
- “The whole frontend is **static HTML/JS** served by Spring Boot, so deployment is a **single jar**.”
- “The schema uses **foreign keys with cascade**, a `UNIQUE(student_id, job_id)` constraint on applications to prevent duplicates, and parameterised queries everywhere to prevent SQL injection.”

---

## 📄 License

MIT — feel free to fork, customise, and present as your own placement project.
