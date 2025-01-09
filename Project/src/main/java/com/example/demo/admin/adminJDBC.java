package com.example.demo.admin;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.admin.datas.actorData;
import com.example.demo.admin.datas.customerData;
import com.example.demo.admin.datas.genreData;
import com.example.demo.admin.datas.movieData;

@Repository
public class adminJDBC implements adminRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<actorData> getAllActors(String filter) {
        List<actorData> actors = new ArrayList<>();

        if (filter != null) {
            actors = jdbc.query("SELECT * FROM actors WHERE name ILIKE ? AND deleted = false ORDER BY actor_id",
                    this::mapRowToActorData,
                    "%" + filter + "%");
        } else {
            actors = jdbc.query("SELECT * FROM actors WHERE deleted = false ORDER BY actor_id",
                    this::mapRowToActorData);
        }

        return actors;
    }

    @Override
    public List<actorData> getRemovedActors(String filter) {
        List<actorData> actors = new ArrayList<>();

        if (filter != null) {
            actors = jdbc.query("SELECT * FROM actors WHERE name ILIKE ? AND deleted = true ORDER BY actor_id",
                    this::mapRowToActorData,
                    "%" + filter + "%");
        } else {
            actors = jdbc.query("SELECT * FROM actors WHERE deleted = true ORDER BY actor_id", this::mapRowToActorData);
        }

        return actors;
    }

    @Override
    public void removeActor(int id) {
        jdbc.update("UPDATE actors SET deleted = true WHERE actor_id = ?", id);
    }

    @Override
    public void restoreActor(int id) {
        jdbc.update("UPDATE actors SET deleted = false WHERE actor_id = ?", id);
    }

    @Override
    public actorData getActorById(int id) {
        return jdbc.queryForObject("SELECT * FROM actors WHERE actor_id = ? AND deleted = false",
                this::mapRowToActorData, id);
    }

    @Override
    public List<actorData> getActorByMovieId(int id) {
        return jdbc.query("SELECT * FROM movie_actors WHERE movie_id = ? AND deleted = false ORDER BY actor_id",
                this::mapRowToActorData, id);
    }

    @Override
    public boolean updateActor(int id, String actor) {
        String sqlCheck = "SELECT COUNT(*) FROM actors WHERE name ILIKE ? and actor_id != ?";
        Integer count = jdbc.queryForObject(sqlCheck, Integer.class, actor, id);

        if (count != null && count > 0) {
            return false;
        }

        // System.out.println("actor id = " + id);

        jdbc.update("UPDATE actors SET name = ? WHERE actor_id = ?", actor, id);
        return true;
    }

    @Override
    public boolean addActor(String actor) {
        String sqlCheck = "SELECT COUNT(*) FROM actors WHERE name ILIKE ?";
        Integer count = jdbc.queryForObject(sqlCheck, Integer.class, actor);

        if (count != null && count > 0) {
            actorData deleted = jdbc.queryForObject("SELECT * FROM actors WHERE name ILIKE ? AND deleted = true",
                    this::mapRowToActorData, actor);

            if (deleted != null) {
                restoreActor(deleted.getActor_id());
                return true;
            } else {
                return false;
            }
        }

        jdbc.update("INSERT INTO actors (name) VALUES (?)", actor);
        return true;
    }

    @Override
    public List<customerData> getAllCustomers() {
        return jdbc.query("SELECT * FROM users WHERE role ILIKE 'user'", this::mapRowToCustomerData);
    }

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
        return jdbc.query("SELECT * FROM movie_genres WHERE movie_id = ? AND deleted = false ORDER BY genre_id",
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

    @Override
    public List<movieData> getAllMovies(String filter) {
        List<movieData> movies = new ArrayList<>();

        if (filter != null) {
            movies = jdbc.query("SELECT * FROM movies WHERE title ILIKE ? AND deleted = false ORDER BY movie_id",
                    this::mapRowToMovieData,
                    "%" + filter + "%");
        } else {
            movies = jdbc.query("SELECT * FROM movies WHERE deleted = false ORDER BY movie_id",
                    this::mapRowToMovieData);
        }

        return movies;
    }

    @Override
    public List<movieData> getRemovedMovies(String filter) {
        List<movieData> movies = new ArrayList<>();

        if (filter != null) {
            movies = jdbc.query("SELECT * FROM movies WHERE title ILIKE ? AND deleted = true ORDER BY movie_id",
                    this::mapRowToMovieData,
                    "%" + filter + "%");
        } else {
            movies = jdbc.query("SELECT * FROM movies WHERE deleted = true ORDER BY movie_id", this::mapRowToMovieData);
        }

        return movies;
    }

    @Override
    public void removeMovie(int id) {
        jdbc.update("UPDATE movies SET deleted = true WHERE movie_id = ?", id);
    }

    @Override
    public void restoreMovie(int id) {
        jdbc.update("UPDATE movies SET deleted = false WHERE movie_id = ?", id);
    }

    @Override
    public movieData getMovieById(int id) {
        return jdbc.queryForObject("SELECT * FROM movies WHERE movie_id = ? AND deleted = false",
                this::mapRowToMovieData, id);
    }

    @Override
    public boolean addMovie(String movieTitle, String description, String releaseYear, int duration, int stock,
            List<Integer> selectedGenres, List<Integer> selectedActors, MultipartFile moviePosterLandscape,
            MultipartFile moviePosterPortrait) {

        // insert the pictures first then get the imageFilePath
        String landscapePath = saveImage(moviePosterLandscape, "Horizontal");
        String portraitPath = saveImage(moviePosterPortrait, "Vertical");

        insertIntoMovie(movieTitle, description, releaseYear, duration, stock, landscapePath, portraitPath, stock);

        int movieId = this.getMovieId(movieTitle);

        insertIntoActors(movieId, selectedActors);
        insertIntoGenres(movieId, selectedGenres);
        return true;
    }

    private String saveImage(MultipartFile image, String folder) {
        try {
            // Create the file path
            String filename = image.getOriginalFilename();
            String filePath = "F:/Campus Stuff/Codes/PBW/TheReelDeal/TheReelDeal/moviePosters/" + folder + "/"
                    + filename;

            // Create the directory if it doesn't exist
            File directory = new File("F:/Campus Stuff/Codes/PBW/TheReelDeal/TheReelDeal/moviePosters/" + folder);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it does not exist
            }

            // Save the image to the specified location
            File destFile = new File(filePath);
            image.transferTo(destFile); // Save the file to disk

            // Return the file path to be stored in the database in the desired format
            return "/" + folder + "/" + filename;

        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the error as appropriate
        }
    }

    private void insertIntoMovie(String movieTitle, String description, String releaseYear, int duration, int stock,
            String moviePosterLandscape, String moviePosterPortrait, double basePrice) {
        jdbc.update(
                "INSERT INTO movies (title, description, release_year, duration, base_price, landscapePoster, portraitPoster, stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                movieTitle, description, releaseYear, duration, basePrice, moviePosterLandscape, moviePosterPortrait,
                stock);
    }

    private int getMovieId(String movieTitle) {
        return jdbc.queryForObject("SELECT movie_id FROM movies WHERE title = ?", Integer.class, movieTitle);
    }

    private void insertIntoActors(int movieId, List<Integer> selectedActors) {
        for (int actorId : selectedActors) {
            jdbc.update("INSERT INTO movie_actors (movie_id, actor_id) VALUES (?, ?)", movieId, actorId);
        }
    }

    private void insertIntoGenres(int movieId, List<Integer> selectedGenres) {
        for (int genreId : selectedGenres) {
            jdbc.update("INSERT INTO movie_genres (movie_id, genre_id) VALUES (?, ?)", movieId, genreId);
        }
    }

    private movieData mapRowToMovieData(ResultSet rs, int rowNum)
            throws SQLException {
        // Create pemeriksaanData object using constructor
        return new movieData(
                rs.getInt("movie_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("release_year"),
                rs.getInt("duration"),
                rs.getDouble("base_price"),
                rs.getString("landscapePoster"),
                rs.getString("portraitPoster"));
    }

    private customerData mapRowToCustomerData(ResultSet rs, int rowNum)
            throws SQLException {
        // Create pemeriksaanData object using constructor
        return new customerData(
                rs.getString("phone"),
                rs.getString("username"),
                rs.getString("name"),
                rs.getString("email"));
    }

    private actorData mapRowToActorData(ResultSet rs, int rowNum)
            throws SQLException {
        // Create pemeriksaanData object using constructor
        return new actorData(
                rs.getInt("actor_id"),
                rs.getString("name"));
    }

    private genreData mapRowToGenreData(ResultSet rs, int rowNum)
            throws SQLException {
        // Create pemeriksaanData object using constructor
        return new genreData(
                rs.getInt("genre_id"),
                rs.getString("name"));
    }
}
