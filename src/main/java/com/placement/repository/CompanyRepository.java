package com.placement.repository;

import com.placement.model.Company;
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
public class CompanyRepository {

    private final JdbcTemplate jdbc;

    public CompanyRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Company> mapper = (rs, n) -> Company.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .website(rs.getString("website"))
            .location(rs.getString("location"))
            .description(rs.getString("description"))
            .createdAt(rs.getTimestamp("created_at") == null ? null : rs.getTimestamp("created_at").toLocalDateTime())
            .build();

    public Long save(Company c) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO companies(name,website,location,description) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getName());
            ps.setString(2, c.getWebsite());
            ps.setString(3, c.getLocation());
            ps.setString(4, c.getDescription());
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }

    public List<Company> findAll() {
        return jdbc.query("SELECT * FROM companies ORDER BY id DESC", mapper);
    }

    public Optional<Company> findById(Long id) {
        return jdbc.query("SELECT * FROM companies WHERE id=?", mapper, id).stream().findFirst();
    }

    public long count() {
        Long c = jdbc.queryForObject("SELECT COUNT(*) FROM companies", Long.class);
        return c == null ? 0 : c;
    }

    public int delete(Long id) {
        return jdbc.update("DELETE FROM companies WHERE id=?", id);
    }
}
