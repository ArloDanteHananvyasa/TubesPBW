package com.example.demo.admin.jdbc;

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

import com.example.demo.admin.datas.movieData;
import com.example.demo.admin.repositories.adminMovieRepository;

@Repository
public class adminMovieJDBC implements adminMovieRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<movieData> getAllMovies(String filter) {
        List<movieData> movies = new ArrayList<>();

        if (filter != null) {
            movies = jdbc.query("SELECT * FROM movies WHERE title ILIKE ? AND deleted = false ORDER BY title",
                    this::mapRowToMovieData,
                    "%" + filter + "%");
        } else {
            movies = jdbc.query("SELECT * FROM movies WHERE deleted = false ORDER BY title",
                    this::mapRowToMovieData);
        }

        return movies;
    }

    @Override
    public List<movieData> getRemovedMovies(String filter) {
        List<movieData> movies = new ArrayList<>();

        if (filter != null) {
            movies = jdbc.query("SELECT * FROM movies WHERE title ILIKE ? AND deleted = true ORDER BY title",
                    this::mapRowToMovieData,
                    "%" + filter + "%");
        } else {
            movies = jdbc.query("SELECT * FROM movies WHERE deleted = true ORDER BY title", this::mapRowToMovieData);
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
        return jdbc.queryForObject("SELECT * FROM movies WHERE movie_id = ?",
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

    @Override
    public boolean editMovie(int movieId, String movieTitle, String description, String releaseYear, int duration,
            int stock, List<Integer> selectedGenres, List<Integer> selectedActors, MultipartFile moviePosterLandscape,
            MultipartFile moviePosterPortrait) {

        // Check if the movie title already exists and belongs to another movie
        int check = jdbc.queryForObject("SELECT COUNT(*) FROM movies WHERE title ILIKE ? AND movie_id != ?",
                Integer.class,
                movieTitle, movieId);

        if (check > 0) {
            return false; // Title conflict with another movie
        }

        // Get current movie details to handle images
        movieData currentMovie = this.getMovieById(movieId);

        String landscapePath = currentMovie.getLandscapePoster();
        String portraitPath = currentMovie.getPortraitPoster();

        // Handle new landscape poster upload
        if (moviePosterLandscape != null && !moviePosterLandscape.isEmpty()) {
            // Delete old landscape poster
            deleteImage(
                    "C:\\Users\\Junita Hariyati\\OneDrive - Universitas Katolik Parahyangan\\Semester 5\\PBW\\Tubes\\Project\\moviePosters"
                            + landscapePath);

            // Save new landscape poster
            landscapePath = saveImage(moviePosterLandscape, "Horizontal");
        }

        // Handle new portrait poster upload
        if (moviePosterPortrait != null && !moviePosterPortrait.isEmpty()) {
            // Delete old portrait poster
            deleteImage(
                    "C:\\Users\\Junita Hariyati\\OneDrive - Universitas Katolik Parahyangan\\Semester 5\\PBW\\Tubes\\Project\\moviePosters"
                            + portraitPath);

            // Save new portrait poster
            portraitPath = saveImage(moviePosterPortrait, "Vertical");
        }

        // Update movie details in the database
        jdbc.update(
                "UPDATE movies SET title = ?, description = ?, release_year = ?, duration = ?, base_price = ?, landscapePoster = ?, portraitPoster = ?, stock = ? WHERE movie_id = ?",
                movieTitle, description, releaseYear, duration, currentMovie.getBasePrice(), landscapePath,
                portraitPath,
                stock, movieId);

        // Update genres
        jdbc.update("DELETE FROM movie_genres WHERE movie_id = ?", movieId); // Remove existing genres
        insertIntoGenres(movieId, selectedGenres); // Add updated genres

        // Update actors
        jdbc.update("DELETE FROM movie_actors WHERE movie_id = ?", movieId); // Remove existing actors
        insertIntoActors(movieId, selectedActors); // Add updated actors

        return true;
    }

    private void deleteImage(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    private String saveImage(MultipartFile image, String folder) {
        try {
            // Create the file path
            String filename = image.getOriginalFilename();
            String filePath = "C:\\Users\\Junita Hariyati\\OneDrive - Universitas Katolik Parahyangan\\Semester 5\\PBW\\Tubes\\Project\\moviePosters\\"
                    + folder + "/"
                    + filename;

            // Create the directory if it doesn't exist
            File directory = new File(
                    "C:\\Users\\Junita Hariyati\\OneDrive - Universitas Katolik Parahyangan\\Semester 5\\PBW\\Tubes\\Project\\moviePosters\\"
                            + folder);
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
                rs.getString("portraitPoster"),
                rs.getInt("stock"));
    }
}
