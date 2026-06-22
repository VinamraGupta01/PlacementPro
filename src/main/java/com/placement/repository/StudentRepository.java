package com.placement.repository;

import com.placement.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbc;

    public StudentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Student> mapper = (rs, n) -> Student.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .branch(rs.getString("branch"))
            .cgpa(rs.getDouble("cgpa"))
            .passingYear((Integer) rs.getObject("passing_year"))
            .phone(rs.getString("phone"))
            .resumeUrl(rs.getString("resume_url"))
            .role(rs.getString("role"))
            .createdAt(rs.getTimestamp("created_at") == null ? null : rs.getTimestamp("created_at").toLocalDateTime())
            .build();

    public Long save(Student s) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO students(name,email,password,branch,cgpa,passing_year,phone,resume_url,role) " +
                            "VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPassword());
            ps.setString(4, s.getBranch());
            ps.setObject(5, s.getCgpa());
            ps.setObject(6, s.getPassingYear());
            ps.setString(7, s.getPhone());
            ps.setString(8, s.getResumeUrl());
            ps.setString(9, s.getRole() == null ? "STUDENT" : s.getRole());
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }

    public int update(Student s) {
        return jdbc.update("UPDATE students SET name=?, branch=?, cgpa=?, passing_year=?, phone=? WHERE id=?",
                s.getName(), s.getBranch(), s.getCgpa(), s.getPassingYear(), s.getPhone(), s.getId());
    }

    public int updateResume(Long id, String url) {
        return jdbc.update("UPDATE students SET resume_url=? WHERE id=?", url, id);
    }

    public Optional<Student> findByEmail(String email) {
        return jdbc.query("SELECT * FROM students WHERE email=?", mapper, email).stream().findFirst();
    }

    public Optional<Student> findById(Long id) {
        return jdbc.query("SELECT * FROM students WHERE id=?", mapper, id).stream().findFirst();
    }

    public List<Student> findAll() {
        return jdbc.query("SELECT * FROM students ORDER BY id DESC", mapper);
    }

    public long count() {
        Long c = jdbc.queryForObject("SELECT COUNT(*) FROM students", Long.class);
        return c == null ? 0 : c;
    }

    public boolean existsByEmail(String email) {
        Integer c = jdbc.queryForObject("SELECT COUNT(*) FROM students WHERE email=?", Integer.class, email);
        return c != null && c > 0;
    }

    // -------- skills --------
    public List<String> findSkills(Long studentId) {
        return jdbc.queryForList("SELECT skill_name FROM student_skills WHERE student_id=?", String.class, studentId);
    }

    public void clearSkills(Long studentId) {
        jdbc.update("DELETE FROM student_skills WHERE student_id=?", studentId);
    }

    public void addSkill(Long studentId, String skill) {
        jdbc.update("INSERT INTO student_skills(student_id, skill_name) VALUES (?,?)", studentId, skill);
    }
}
