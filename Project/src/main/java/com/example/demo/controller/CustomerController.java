package com.example.demo.controller;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.RequiredRole;
import com.example.demo.user.CartData;
import com.example.demo.user.GenreData;
import com.example.demo.user.HomePageData;
import com.example.demo.user.MovieDetailData;
import com.example.demo.user.TransactionData;
import com.example.demo.user.UserData;
import com.example.demo.user.UserRepository;

import jakarta.servlet.http.HttpSession;
@Controller

@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    @ResponseBody
    public List<HomePageData> search(@RequestParam String title){
        return userRepository.searchTitle("%" + title + "%");
    }
    
    @GetMapping("/homepage")
    @RequiredRole({"user"})
    public String homepage(Model model) {
        List<HomePageData> wheelMovies = userRepository.getMovieWheel();
        model.addAttribute("wheelMovies", wheelMovies);

        List<HomePageData> latestMovies = userRepository.getMoviesFromLast5Years();
        model.addAttribute("latestMovies", latestMovies);

        List<HomePageData> adventureMovies = userRepository.getAdventureMovies();
        model.addAttribute("adventureMovies", adventureMovies);

        List<HomePageData> scifiMovies = userRepository.getScifiMovies();
        model.addAttribute("scifiMovies", scifiMovies);
        return "Customer/homepage"; // Mengarahkan ke halaman utama customer
    }

    @GetMapping("/details/{title}")
    @RequiredRole({"user"})
    public String details(@PathVariable String title, Model model, HttpSession session) {
        
        HomePageData movieGenre = userRepository.getMovieByTitle(title);
        model.addAttribute("movieGenre", movieGenre);

        MovieDetailData movieActor = userRepository.getActorsByTitle(title);
        model.addAttribute("movieActor", movieActor);
        

        HomePageData movie = userRepository.getMovieByTitleFromMovies(title);
    
        // Format the base_price with a thousand separator
        if (movie != null) {
            int basePrice = movie.getBase_price(); // Assuming `base_price` is an int
            String formattedBasePrice = NumberFormat.getInstance(new Locale("en", "US"))
                                                    .format(basePrice)
                                                   .replace(",", "."); // Replace commas with dots
            model.addAttribute("formattedBasePrice", formattedBasePrice);
        }

        model.addAttribute("movie", movie);
        return "Customer/MoviesDetails/Avatar/avatar";
    }

    @PostMapping("/details/{title}")
    public String detailsPost(@PathVariable String title, @RequestParam String titleInput, @RequestParam int stock, Model model, HttpSession session) {
        //Logger logger = LoggerFactory.getLogger(this.getClass());
        
        HomePageData movie = userRepository.getMovieByTitleFromMovies(titleInput);

        //cek udah ada di table cart ato belom, kalo belom notif error, kalo udah di update 
        
        int movie_id = movie.getMovie_id();
        UserData user = (UserData)session.getAttribute("user"); //true
        String phoneNum = user.getPhone();
        //logger.info("Titlenya adalah: " + phoneNum);
        
        List<String> errors = new ArrayList<>();
        //check stock
        if(stock <= 0){
            errors.add("Movie is not in stock");
        }
        //check movies is in cart already or not
        if(!userRepository.isMovieInCart(phoneNum, movie_id)==false){
            errors.add("This movie is already in your cart");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            HomePageData movieGenre = userRepository.getMovieByTitle(title);
            model.addAttribute("movieGenre", movieGenre);

            MovieDetailData movieActor = userRepository.getActorsByTitle(title);
            model.addAttribute("movieActor", movieActor);
            model.addAttribute("movie", movie);
            if (movie != null) {
                int basePrice = movie.getBase_price(); // Assuming `base_price` is an int
                String formattedBasePrice = NumberFormat.getInstance(new Locale("en", "US"))
                                                        .format(basePrice)
                                                       .replace(",", "."); // Replace commas with dots
                model.addAttribute("formattedBasePrice", formattedBasePrice);
            }
            return "Customer/MoviesDetails/Avatar/avatar"; // return to the form page
        }

        userRepository.addMovieToCart(phoneNum, movie_id);
        return "redirect:/customer/homepage";
    }

    @GetMapping("/movies")
    @RequiredRole({"user"})
    public String movieList(Model model) {
        List<HomePageData> movies = userRepository.getAllMovies();
        List<GenreData> genres = userRepository.getAllGenre();

        model.addAttribute("movies", movies);
        model.addAttribute("genres", genres);
        return "Customer/movieList"; // Mengarahkan ke halaman daftar film
    }

    @GetMapping("/movies/filter")
    @ResponseBody
    public List<HomePageData> filterMovies(@RequestParam(required = false) List<String> genres){
        if (genres == null || genres.isEmpty() || genres.contains("All")) {
            return userRepository.getAllMovies();
        }
        return userRepository.getMoviesByGenres(genres.toArray(new String[0]));
    }

    @GetMapping("/check-movie_title")
    @ResponseBody
    public ResponseEntity<HomePageData> checkIdPendaftaran(@RequestParam("title") String title){
        
        HomePageData movie = userRepository.getMovieByTitle(title);
        return ResponseEntity.ok(movie);
    }


    @GetMapping("/cart")
    @RequiredRole({"user"})
    public String cart(Model model, HttpSession session) {
        UserData user = (UserData)session.getAttribute("user");
        String phoneNum = user.getPhone();
        List<CartData>  listCart = userRepository.getCartByUser(phoneNum);
        model.addAttribute("listCart", listCart);
        
        return "Customer/Cart"; // Mengarahkan ke halaman keranjang
    }

    @PostMapping("/cart")
    @RequiredRole({"user"})
    public String handleCheckout(@RequestParam LocalDate pickUpDate, @RequestParam LocalDate returnDate, HttpSession session, Model model){
        UserData user = (UserData)session.getAttribute("user");
        String phoneNum = user.getPhone();
        LocalDate transactionDate = LocalDate.now();

        List<String> errors = new ArrayList<>();
        if (pickUpDate.isBefore(LocalDate.now()) || returnDate.isBefore(LocalDate.now())) {
            errors.add("Pickup date and return date must be in the future.");
        }
        if (!pickUpDate.isBefore(returnDate)) {
            errors.add("Pickup date must be before the return date.");
        }
        if(userRepository.isCartEmpty(phoneNum)){
            errors.add("Cart is empty");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            List<CartData>  listCart = userRepository.getCartByUser(phoneNum);
            model.addAttribute("listCart", listCart);
            return "Customer/cart"; // return to the form page
        }

        //calculate price by taking from db instead
        int cartPrice = userRepository.getTotalCartPrice(phoneNum);
        int rent_duration = (int)ChronoUnit.DAYS.between(pickUpDate, returnDate); 
        if(rent_duration < 1){
            rent_duration = 1;
        }
        int totalPrice = cartPrice * rent_duration;

        if (!userRepository.isCartEmpty(phoneNum)) { //if cart is not empty
            //popup berhasil
            userRepository.checkoutCart(phoneNum, totalPrice, pickUpDate, returnDate, transactionDate, rent_duration);
        }
        else{
            //popup gagal
        }
        
        return "Customer/cart";
    }
    
    @GetMapping("/RemoveFromCart")
    @RequiredRole({"user"})
    public String removeFromCart(@RequestParam int movieId, HttpSession session, Model model){
        UserData user = (UserData)session.getAttribute("user");
        String phoneNum = user.getPhone();
        userRepository.removeFromCart(phoneNum, movieId);
        List<CartData>  listCart = userRepository.getCartByUser(phoneNum);
        model.addAttribute("listCart", listCart);

        return "redirect:/customer/cart";
    }

    @GetMapping("/myRentals")
    @RequiredRole({"user"})
    public String myRentals(HttpSession session, Model model) {
        UserData user = (UserData)session.getAttribute("user");
        String phoneNum = user.getPhone();

        List<TransactionData>  listRentalsCurr = userRepository.getRentalsCurrByUser(phoneNum);
        List<TransactionData> listRentalsHistory = userRepository.getRentalsHistoryByUser(phoneNum);
        model.addAttribute("rentalsCurr", listRentalsCurr);
        model.addAttribute("rentalsHistory", listRentalsHistory);
        return "Customer/myRentals"; // Mengarahkan ke halaman riwayat sewa
    }
}