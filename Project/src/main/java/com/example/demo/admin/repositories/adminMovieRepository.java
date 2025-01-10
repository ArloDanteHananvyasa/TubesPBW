package com.example.demo.admin.repositories;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.admin.datas.movieData;

public interface adminMovieRepository {
    List<movieData> getAllMovies(String filter);

    List<movieData> getRemovedMovies(String filter);

    boolean addMovie(String movieTitle, String description, String releaseYear, int duration, int stock,
            List<Integer> selectedGenres, List<Integer> selectedActors, MultipartFile moviePosterLandscape,
            MultipartFile moviePosterPortrait);

    boolean editMovie(int movieId, String movieTitle, String description, String releaseYear, int duration, int stock,
            List<Integer> selectedGenres, List<Integer> selectedActors, MultipartFile moviePosterLandscape,
            MultipartFile moviePosterPortrait);

    void removeMovie(int id);

    void restoreMovie(int id);

    movieData getMovieById(int id);
}
