package com.example.demo.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void registerUser(UserData userData);
    void registerAdmin(UserData adminData);
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
    void addMovieToCart(String phoneNum, int movieId);
    List<CartData> getCartByUser(String phoneNum);
    HomePageData getMovieByTitleFromMovies(String title);
    boolean isMovieInCart(String phoneNum, int movieId);
    boolean isCartEmpty(String phoneNum);
    void checkoutCart(String phoneNum, int totalPrice, LocalDate pickUpDate, LocalDate returnDate, LocalDate transactionDate, int rent_duration);
    int getTotalCartPrice(String phoneNum);
    void removeFromCart(String phoneNum, int movieId);
    List<TransactionData> getRentalsCurrByUser(String phoneNum);
    List<TransactionData> getRentalsHistoryByUser(String phoneNum);

}
