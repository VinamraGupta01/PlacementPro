package com.placement.repository;

import com.placement.model.Job;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JobRepository {

    private final JdbcTemplate jdbc;

    public JobRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Job> mapper = (rs, n) -> Job.builder()
            .id(rs.getLong("id"))
            .companyId(rs.getLong("company_id"))
            .companyName(rs.getString("company_name"))
            .title(rs.getString("title"))
            .description(rs.getString("description"))
            .minCgpa(rs.getDouble("min_cgpa"))
            .eligibleBranches(rs.getString("eligible_branches"))
            .passingYear((Integer) rs.getObject("passing_year"))
            .jobType(rs.getString("job_type"))
            .location(rs.getString("location"))
            .deadline(rs.getDate("deadline") == null ? null : rs.getDate("deadline").toLocalDate())
            .createdAt(rs.getTimestamp("created_at") == null ? null : rs.getTimestamp("created_at").toLocalDateTime())
            .build();

    private static final String SELECT_JOB =
            "SELECT j.*, c.name AS company_name FROM jobs j JOIN companies c ON c.id = j.company_id ";

    public Long save(Job j) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO jobs(company_id,title,description,min_cgpa,eligible_branches,passing_year,job_type,location,deadline) " +
                            "VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, j.getCompanyId());
            ps.setString(2, j.getTitle());
            ps.setString(3, j.getDescription());
            ps.setObject(4, j.getMinCgpa());
            ps.setString(5, j.getEligibleBranches());
            ps.setObject(6, j.getPassingYear());
            ps.setString(7, j.getJobType());
            ps.setString(8, j.getLocation());
            ps.setDate(9, j.getDeadline() == null ? null : Date.valueOf(j.getDeadline()));
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }

    public List<Job> findAll() {
        return jdbc.query(SELECT_JOB + " ORDER BY j.id DESC", mapper);
    }

    public Optional<Job> findById(Long id) {
        return jdbc.query(SELECT_JOB + " WHERE j.id=?", mapper, id).stream().findFirst();
    }

    public long count() {
        Long c = jdbc.queryForObject("SELECT COUNT(*) FROM jobs", Long.class);
        return c == null ? 0 : c;
    }

    public int delete(Long id) {
        return jdbc.update("DELETE FROM jobs WHERE id=?", id);
    }

    // -------- required skills --------
    public List<String> findRequiredSkills(Long jobId) {
        return jdbc.queryForList("SELECT skill_name FROM job_required_skills WHERE job_id=?", String.class, jobId);
    }

    public void clearRequiredSkills(Long jobId) {
        jdbc.update("DELETE FROM job_required_skills WHERE job_id=?", jobId);
    }

    public void addRequiredSkill(Long jobId, String skill) {
        jdbc.update("INSERT INTO job_required_skills(job_id, skill_name) VALUES (?,?)", jobId, skill);
    }
}
