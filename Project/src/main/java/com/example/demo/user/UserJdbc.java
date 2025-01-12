package com.example.demo.user;

import java.time.LocalDate;
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
        String sql = "SELECT * FROM view_moviegenres WHERE CAST(release_year AS INTEGER) >= (EXTRACT(YEAR FROM CURRENT_DATE) - 5) AND deleted = false";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class));
    }

    @Override
    public List<HomePageData> getAdventureMovies(){
        String sql = "SELECT * FROM view_moviegenres WHERE genres_names LIKE '%Adventure%' AND deleted = false";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class));
    }

    @Override
    public List<HomePageData> getScifiMovies(){
        String sql = "SELECT * FROM view_moviegenres WHERE genres_names LIKE '%Sci-Fi%' AND deleted = false";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class));
    }

    @Override
    public List<HomePageData> getMovieWheel(){
        String sql = "SELECT * FROM view_moviegenres WHERE deleted = false ORDER BY RANDOM() LIMIT 5";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class));
    }

    @Override
    public HomePageData getMovieByTitle(String title){
        String sql = "SELECT * FROM view_moviegenres WHERE title = ? AND deleted = false";
        List<HomePageData> movies = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class), title);
        return movies.isEmpty() ? null : movies.get(0);
    }

    @Override
    public MovieDetailData getActorsByTitle(String title){
        String sql = "SELECT * FROM view_movieactors WHERE title = ?";
        List<MovieDetailData> movies = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MovieDetailData.class), title);
        return movies.isEmpty() ? null : movies.get(0);
    }

    @Override
    public List<HomePageData> getMoviesByGenres(String[] genres){
        String sql = "SELECT * FROM view_moviegenres WHERE 1=1 AND deleted = false";

        for(String genre : genres){
            sql += " AND genres_names LIKE '%" + genre + "%'";
        }

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class));
    }

    @Override
    public List<HomePageData> getAllMovies(){
        String sql = "SELECT * FROM view_moviegenres WHERE deleted = false";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class));
    }

    @Override
    public List<GenreData> getAllGenre(){
        String sql = "SELECT * FROM genres WHERE deleted = false";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(GenreData.class));
    }

    @Override
    public List<HomePageData> searchTitle(String title){
        String sql = "SELECT * FROM view_moviegenres WHERE title iLIKE ? AND deleted = false LIMIT 10";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class), "%" + title + "%");
    }


    @Override
    public void addMovieToCart(String phoneNum, int movieId){
        String sql = """
                INSERT INTO cart (user_phone, movie_id) VALUES
                (?, ?)
                """;
        jdbcTemplate.update(
            sql, 
            phoneNum,
            movieId
            );
    }

    @Override
    public List<CartData> getCartByUser(String phoneNum){
        String sql = """
            SELECT cart.movie_id, title, movies.base_price, movies.portraitposter
            FROM CART 
            INNER JOIN movies ON cart.movie_id = movies.movie_id
            WHERE user_phone = ? AND is_active = true
            """;

            return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
                CartData cart = new CartData(
                    resultSet.getInt("movie_id"),
                    resultSet.getString("title"),
                    resultSet.getInt("base_price"),
                    resultSet.getString("portraitPoster")
                    
                );
                return cart;
            }, phoneNum);
    }

    @Override
    public HomePageData getMovieByTitleFromMovies(String title){
        String sql = "SELECT * FROM movies WHERE title = ?";
        List<HomePageData> movies = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HomePageData.class), title);
        return movies.isEmpty() ? null : movies.get(0);
    }

    @Override
    public boolean isMovieInCart(String phoneNum, int movieId){
        boolean result = true;
        String sql = """
            SELECT *
            FROM CART 
            INNER JOIN movies ON cart.movie_id = movies.movie_id
            WHERE user_phone = ? AND is_active = true AND cart.movie_id = ?
            """;
            
        List<CartData> cartList = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            CartData cart = new CartData(
                resultSet.getInt("movie_id"),
                resultSet.getString("title"),
                resultSet.getInt("base_price"),
                resultSet.getString("portraitPoster")
            );
            return cart;
        }, phoneNum, movieId);

        if(cartList.isEmpty()){
            result = false;
        }
        return result;
    }

    @Override
    public boolean isCartEmpty(String phoneNum) {
        String sql = """
                SELECT COUNT(*)
                FROM CART
               WHERE user_phone = ? AND is_active = true
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phoneNum);

        return count == null || count == 0; // If count is 0 or null, the cart is empty
    }

    @Override
    public int getTotalCartPrice(String phoneNum){
        String sql = """
            SELECT SUM(base_price) AS total_price
            FROM CART 
            INNER JOIN movies ON cart.movie_id = movies.movie_id
            WHERE user_phone = ? AND is_active = true
            """;
            // Use queryForObject to retrieve a single value
            Integer res = jdbcTemplate.queryForObject(sql, new Object[]{phoneNum}, Integer.class);
            // Handle cases where the result might be null (e.g., no matching rows)
            return res != null ? res : 0;
    }

    //take dari cart, masukin ke transactions, update stock
    @Override
public void checkoutCart(String phoneNum, int totalPrice, LocalDate pickUpDate, LocalDate returnDate, LocalDate transactionDate, int rent_duration) {
    // Step 1: Insert the transaction
    String sql = """
        INSERT INTO transactions (phone, base_fee, pickup_date, due_date, transaction_date, days) VALUES
        (?, ?, ?, ?, ?, ?);
        """;
    jdbcTemplate.update(sql, phoneNum, totalPrice, pickUpDate, returnDate, transactionDate, rent_duration);

    // Step 2: Get all movie IDs from the cart
    sql = """
        SELECT movie_id
        FROM CART
        WHERE user_phone = ? AND is_active = true;
        """;
    List<Integer> movieIds = jdbcTemplate.queryForList(sql, Integer.class, phoneNum);

    // Step 3: Retrieve the latest transaction ID
    sql = """
        SELECT transaction_id
        FROM transactions
        ORDER BY transaction_id DESC
        LIMIT 1;
        """;
    Integer transId = jdbcTemplate.queryForObject(sql, Integer.class);

    // Step 4: Insert movie IDs into the transaction_details table
    sql = """
        INSERT INTO transaction_details (transaction_id, movie_id) VALUES (?, ?);
        """;
    String sqlStock = """
        UPDATE movies
        SET stock = stock - 1
        WHERE movie_id = ?;
        """;
    for (Integer movieId : movieIds) {
        jdbcTemplate.update(sql, transId, movieId);
        jdbcTemplate.update(sqlStock, movieId);
    }

    // Step 5: Mark cart items as inactive
    sql = """
        UPDATE CART
        SET is_active = false
        WHERE user_phone = ? AND is_active = true;
        """;
    jdbcTemplate.update(sql, phoneNum);
    }

    @Override
    public void removeFromCart(String phoneNum, int movieId){
        String sql ="""
                UPDATE CART
                SET is_active = false
                WHERE user_phone = ? AND movie_id = ? AND is_active = true;
                """;
        jdbcTemplate.update(sql, phoneNum, movieId);
    }

    @Override
    public List<TransactionData> getRentalsCurrByUser(String phoneNum){
        String sql = """
            SELECT movies.title, transactions.pickup_date, transactions.due_date
            FROM
            transactions INNER JOIN
            transaction_details on transactions.transaction_id = transaction_details.transaction_id INNER JOIN
            movies on transaction_details.movie_id = movies.movie_id
            WHERE
            phone = ? AND return_date IS NULL
            ORDER BY transactions.due_date
                """;
            List<TransactionData> currTrans = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
                TransactionData trans = new TransactionData(
                    resultSet.getString("title"),
                    resultSet.getObject("pickup_date", LocalDate.class),
                    resultSet.getObject("due_date", LocalDate.class)
                );
                return trans;
            }, phoneNum);
        return currTrans;
    }

    @Override
    public List<TransactionData> getRentalsHistoryByUser(String phoneNum){
        String sql = """
            SELECT movies.title, transactions.pickup_date, transactions.due_date, transactions.return_date
            FROM
            transactions INNER JOIN
            transaction_details on transactions.transaction_id = transaction_details.transaction_id INNER JOIN
            movies on transaction_details.movie_id = movies.movie_id
            where phone = ? AND return_date IS NOT NULL
                """;
            List<TransactionData> historyTrans = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
                TransactionData trans = new TransactionData(
                    resultSet.getString("title"),
                    resultSet.getObject("pickup_date", LocalDate.class),
                    resultSet.getObject("due_date", LocalDate.class),
                    resultSet.getObject("return_date", LocalDate.class)
                );
                return trans;
            }, phoneNum);
        return historyTrans;
    }
}