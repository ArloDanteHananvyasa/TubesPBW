package com.example.demo.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbc implements UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void registerUser(UserData userData){
        String sql = """
                INSERT INTO users (phone, username, name, email, password, role, deleted) VALUES
                (?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
            sql, 
            userData.getPhone(),
            userData.getUsername(),
            userData.getName(),
            userData.getEmail(),
            userData.getPassword(),
            "user",
            false
            );
    }

    @Override
    public Optional<UserData> findByEmail(String email){
        String sql = "SELECT * FROM users WHERE email = ?";
        List<UserData> users = jdbcTemplate.query(
            sql, 
            ps -> ps.setString(1, email), 
            new BeanPropertyRowMapper<>(UserData.class));
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}
