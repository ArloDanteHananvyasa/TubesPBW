package com.example.demo.admin.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.admin.datas.genreData;
import com.example.demo.admin.repositories.adminGenreRepository;

@Repository
public class adminGenreJDBC implements adminGenreRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<genreData> getAllGenres(String filter) {
        List<genreData> genres = new ArrayList<>();

        if (filter != null) {
            genres = jdbc.query("SELECT * FROM genres WHERE name ILIKE ? AND deleted = false ORDER BY genre_id",
                    this::mapRowToGenreData,
                    "%" + filter + "%");
        } else {
            genres = jdbc.query("SELECT * FROM genres WHERE deleted = false ORDER BY genre_id",
                    this::mapRowToGenreData);
        }

        return genres;
    }

    @Override
    public List<genreData> getRemovedGenres(String filter) {
        List<genreData> genres = new ArrayList<>();

        if (filter != null) {
            genres = jdbc.query("SELECT * FROM genres WHERE name ILIKE ? AND deleted = true ORDER BY genre_id",
                    this::mapRowToGenreData,
                    "%" + filter + "%");
        } else {
            genres = jdbc.query("SELECT * FROM genres WHERE deleted = true ORDER BY genre_id", this::mapRowToGenreData);
        }

        return genres;
    }

    @Override
    public void removeGenre(int id) {
        jdbc.update("UPDATE genres SET deleted = true WHERE genre_id = ?", id);
    }

    @Override
    public void restoreGenre(int id) {
        jdbc.update("UPDATE genres SET deleted = false WHERE genre_id = ?", id);
    }

    @Override
    public genreData getGenreById(int id) {
        return jdbc.queryForObject("SELECT * FROM genres WHERE genre_id = ? AND deleted = false",
                this::mapRowToGenreData, id);
    }

    @Override
    public List<genreData> getGenreByMovieId(int id) {
        return jdbc.query(
                "SELECT * FROM movie_genres JOIN genres on genres.genre_id = movie_genres.genre_id WHERE movie_id = ? AND deleted = false ORDER BY genres.genre_id",
                this::mapRowToGenreData, id);
    }

    @Override
    public boolean updateGenre(int id, String genre) {
        String sqlCheck = "SELECT COUNT(*) FROM genres WHERE name ILIKE ?";
        Integer count = jdbc.queryForObject(sqlCheck, Integer.class, genre);

        if (count != null && count > 0) {
            // Genre already exists
            return false;
        }

        jdbc.update("UPDATE genres SET name = ? WHERE genre_id = ?", genre, id);
        return true;
    }

    @Override
    public boolean addGenre(String genre) {
        // Check if the genre already exists
        String sqlCheck = "SELECT COUNT(*) FROM genres WHERE name ILIKE ?";
        Integer count = jdbc.queryForObject(sqlCheck, Integer.class, genre);

        if (count != null && count > 0) {
            genreData deleted = jdbc.queryForObject("SELECT * FROM genres WHERE name ILIKE ? AND deleted = true",
                    this::mapRowToGenreData, genre);

            if (deleted != null) {
                restoreGenre(deleted.getGenre_id());
                return true;
            } else {
                return false;
            }
        }

        // Insert the new genre
        String sqlInsert = "INSERT INTO genres (name) VALUES (?)";
        jdbc.update(sqlInsert, genre);
        return true;
    }

    private genreData mapRowToGenreData(ResultSet rs, int rowNum)
            throws SQLException {
        // Create pemeriksaanData object using constructor
        return new genreData(
                rs.getInt("genre_id"),
                rs.getString("name"));
    }
}
