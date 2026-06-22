package com.placement.repository;

import com.placement.model.Admin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AdminRepository {

    private final JdbcTemplate jdbc;

    public AdminRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Admin> mapper = (rs, n) -> Admin.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .role(rs.getString("role"))
            .createdAt(rs.getTimestamp("created_at") == null ? null : rs.getTimestamp("created_at").toLocalDateTime())
            .build();

    public void save(Admin a) {
        jdbc.update("INSERT INTO admins(name,email,password,role) VALUES (?,?,?,?)",
                a.getName(), a.getEmail(), a.getPassword(), a.getRole() == null ? "ADMIN" : a.getRole());
    }

    public Optional<Admin> findByEmail(String email) {
        return jdbc.query("SELECT * FROM admins WHERE email=?", mapper, email).stream().findFirst();
    }

    public boolean existsByEmail(String email) {
        Integer c = jdbc.queryForObject("SELECT COUNT(*) FROM admins WHERE email=?", Integer.class, email);
        return c != null && c > 0;
    }
}
