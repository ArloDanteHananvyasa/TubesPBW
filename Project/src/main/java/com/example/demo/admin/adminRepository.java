package com.example.demo.admin;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.admin.datas.actorData;
import com.example.demo.admin.datas.customerData;
import com.example.demo.admin.datas.genreData;
import com.example.demo.admin.datas.movieData;

public interface adminRepository {

    // MOVIE
    List<movieData> getAllMovies(String filter);

    List<movieData> getRemovedMovies(String filter);

    boolean addMovie(String movieTitle, String description, String releaseYear, int duration, int stock,
            List<Integer> selectedGenres, List<Integer> selectedActors, MultipartFile moviePosterLandscape,
            MultipartFile moviePosterPortrait);

    void removeMovie(int id);

    void restoreMovie(int id);

    movieData getMovieById(int id);

    // Genre
    List<genreData> getAllGenres(String filter);

    List<genreData> getRemovedGenres(String filter);

    void removeGenre(int id);

    void restoreGenre(int id);

    genreData getGenreById(int id);

    List<genreData> getGenreByMovieId(int id);

    boolean updateGenre(int id, String genre);

    boolean addGenre(String genre);

    // Actors
    List<actorData> getAllActors(String filter);

    List<actorData> getRemovedActors(String filter);

    void removeActor(int id);

    void restoreActor(int id);

    actorData getActorById(int id);

    List<actorData> getActorByMovieId(int id);

    boolean updateActor(int id, String actor);

    boolean addActor(String actor);

    // Customer
    List<customerData> getAllCustomers();

}
