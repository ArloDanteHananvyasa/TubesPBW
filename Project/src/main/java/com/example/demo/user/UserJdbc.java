package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbc implements UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void registerUser(UserData userData){
        String sql = """
                INSERT INTO users (phone, username, name, email, password, deleted) VALUES
                (?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
            sql, 
            userData.getPhone(),
            userData.getUsername(),
            userData.getName(),
            userData.getEmail(),
            userData.getPassword(),
            false
            );
    }
}
