package com.placement.repository;

import com.placement.model.Application;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ApplicationRepository {

    private final JdbcTemplate jdbc;

    public ApplicationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Application> mapper = (rs, n) -> Application.builder()
            .id(rs.getLong("id"))
            .studentId(rs.getLong("student_id"))
            .jobId(rs.getLong("job_id"))
            .status(rs.getString("status"))
            .matchScore(rs.getInt("match_score"))
            .notes(rs.getString("notes"))
            .appliedAt(rs.getTimestamp("applied_at") == null ? null : rs.getTimestamp("applied_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at") == null ? null : rs.getTimestamp("updated_at").toLocalDateTime())
            .studentName(hasColumn(rs, "student_name") ? rs.getString("student_name") : null)
            .studentEmail(hasColumn(rs, "student_email") ? rs.getString("student_email") : null)
            .jobTitle(hasColumn(rs, "job_title") ? rs.getString("job_title") : null)
            .companyName(hasColumn(rs, "company_name") ? rs.getString("company_name") : null)
            .build();

    private static boolean hasColumn(java.sql.ResultSet rs, String col) {
        try {
            rs.findColumn(col);
            return true;
        } catch (java.sql.SQLException e) {
            return false;
        }
    }

    private static final String JOIN_SQL =
            "SELECT a.*, s.name AS student_name, s.email AS student_email, " +
                    "       j.title AS job_title, c.name AS company_name " +
                    "FROM applications a " +
                    "JOIN students s ON s.id = a.student_id " +
                    "JOIN jobs j ON j.id = a.job_id " +
                    "JOIN companies c ON c.id = j.company_id ";

    public Long save(Application a) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO applications(student_id, job_id, status, match_score, notes) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, a.getStudentId());
            ps.setLong(2, a.getJobId());
            ps.setString(3, a.getStatus() == null ? "APPLIED" : a.getStatus());
            ps.setInt(4, a.getMatchScore() == null ? 0 : a.getMatchScore());
            ps.setString(5, a.getNotes());
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }

    public int updateStatus(Long id, String status) {
        return jdbc.update("UPDATE applications SET status=? WHERE id=?", status, id);
    }

    public int updateNotes(Long id, String notes) {
        return jdbc.update("UPDATE applications SET notes=? WHERE id=?", notes, id);
    }

    public boolean existsByStudentAndJob(Long studentId, Long jobId) {
        Integer c = jdbc.queryForObject(
                "SELECT COUNT(*) FROM applications WHERE student_id=? AND job_id=?", Integer.class, studentId, jobId);
        return c != null && c > 0;
    }

    public Optional<Application> findById(Long id) {
        return jdbc.query(JOIN_SQL + " WHERE a.id=?", mapper, id).stream().findFirst();
    }

    public List<Application> findAll() {
        return jdbc.query(JOIN_SQL + " ORDER BY a.id DESC", mapper);
    }

    public List<Application> findByStudent(Long studentId) {
        return jdbc.query(JOIN_SQL + " WHERE a.student_id=? ORDER BY a.id DESC", mapper, studentId);
    }

    public List<Application> findByJob(Long jobId) {
        return jdbc.query(JOIN_SQL + " WHERE a.job_id=? ORDER BY a.match_score DESC, a.id DESC", mapper, jobId);
    }

    public long count() {
        Long c = jdbc.queryForObject("SELECT COUNT(*) FROM applications", Long.class);
        return c == null ? 0 : c;
    }

    public Map<String, Object> countsByStatus() {
        List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT status, COUNT(*) AS cnt FROM applications GROUP BY status");
        java.util.Map<String, Object> result = new java.util.LinkedHashMap<>();
        for (Map<String, Object> r : rows) {
            result.put((String) r.get("status"), r.get("cnt"));
        }
        return result;
    }

    public List<Map<String, Object>> applicationsPerCompany() {
        return jdbc.queryForList(
                "SELECT c.name AS company, COUNT(a.id) AS applications " +
                        "FROM companies c LEFT JOIN jobs j ON j.company_id = c.id " +
                        "LEFT JOIN applications a ON a.job_id = j.id " +
                        "GROUP BY c.id, c.name ORDER BY applications DESC");
    }

    public List<Map<String, Object>> selectionsByBranch() {
        return jdbc.queryForList(
                "SELECT s.branch AS branch, COUNT(a.id) AS selected " +
                        "FROM applications a JOIN students s ON s.id = a.student_id " +
                        "WHERE a.status='SELECTED' GROUP BY s.branch ORDER BY selected DESC");
    }
}
