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

    @Override
    public Optional<UserData> findByPhone(String phone){
        String sql = "SELECT * FROM users WHERE phone = ?";
        List<UserData> users = jdbcTemplate.query(
            sql, 
            ps -> ps.setString(1, phone),
            new BeanPropertyRowMapper<>(UserData.class)
            );
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public List<HomePageData> getMoviesFromLast5Years(){
        String sql = "SELECT * FROM view_homepage WHERE CAST(release_year AS INTEGER) >= (EXTRACT(YEAR FROM CURRENT_DATE) - 5)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class));
    }

    @Override
    public List<HomePageData> getAdventureMovies(){
        String sql = "SELECT * FROM view_homepage WHERE genres_names LIKE '%Adventure%'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class));
    }

    @Override
    public List<HomePageData> getScifiMovies(){
        String sql = "SELECT * FROM view_homepage WHERE genres_names LIKE '%Sci-Fi%'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class));
    }
}
