package com.example.demo.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void registerUser(UserData userData);
    Optional<UserData> findByEmail(String email);
    Optional<UserData> findByPhone(String phone);
    List<HomePageData> getMoviesFromLast5Years();
    List<HomePageData> getAdventureMovies();
    List<HomePageData> getScifiMovies();
    List<HomePageData> getMovieWheel();
    HomePageData getMovieByTitle(String title);
    MovieDetailData getActorsByTitle(String title);
    List<HomePageData> getMoviesByGenres(String[] genres);
    List<HomePageData> getAllMovies();

    List<HomePageData> searchTitle(String title);
}
