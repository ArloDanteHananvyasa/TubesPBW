package com.example.demo.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.user.HomePageData;
import com.example.demo.user.MovieDetailData;
import com.example.demo.user.UserData;
import com.example.demo.user.UserRepository;

import jakarta.servlet.http.HttpSession;


@Controller
//@RequestMapping(/custoemer), gini aja terus hapus yg /customer di getmapping yg lain (tapi ntar aja takut eror) - Radif
public class CustomerController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/customer/homepage")
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

    @GetMapping("/customer/details/{title}")
    public String details(@PathVariable String title, Model model) {
        HomePageData movieGenre = userRepository.getMovieByTitle(title);
        model.addAttribute("movieGenre", movieGenre);

        MovieDetailData movieActor = userRepository.getActorsByTitle(title);
        model.addAttribute("movieActor", movieActor);
        return "Customer/MoviesDetails/Avatar/avatar";
    }

    @GetMapping("/customer/profile")
    public String profile() {
        return "Customer/profile"; // Mengarahkan ke halaman profil customer
    }

    @GetMapping("/customer/myRentals")
    public String myRentals() {
        return "Customer/myRentals"; // Mengarahkan ke halaman riwayat sewa
    }

    @GetMapping("/customer/movies")
    public String movieList() {
        return "Customer/movieList"; // Mengarahkan ke halaman daftar film
    }

    //bakal ada request param rent duration sama pickupdate
    //gapake param, pakenya ngambil dari db
    @GetMapping("/customer/rentMovie/{title}")
    public String rentMovie(@PathVariable String title, Model model, HttpSession session){
        ///tanggal harus ngambil dari cart di db
        //duration juga
        HomePageData movie = userRepository.getMovieByTitle(title);
        model.addAttribute("movie", movie);
        
        // UserData user = (UserData)session.getAttribute("user");
        // String phoneNum = user.getPhone();
        // LocalDate pickupDate = userRepository.getPickupDateFromCart(phoneNum, title);
        // if(pickupDate == null){
        //     pickupDate = LocalDate.now();
        // }
        // model.addAttribute("pickupDate", pickupDate);
       
        //jadinya bukan duration tapi returnDate
        //duration dipake buat calculate harga, somewhere pake ChronoUnit.DAYS.between(pickupDate, returnDate)
        
        
        
        return "Customer/rentMovie"; // Mengarahkan ke halaman sewa film
    }

    @PostMapping("/customer/rentMovie/{title}")
    //ceritanya udh ada cart di session (tinggal tambahin di login aj)
    //tapi lebih bagusa kalo cart ga ke reset pas login, jadinya harus ke db bgst
    public String rentMoviePost(@PathVariable String title, @RequestParam String titleInput, @RequestParam LocalDate pickUpDate, @RequestParam LocalDate returnDate, Model model, HttpSession session) {
        // Logger logger = LoggerFactory.getLogger(this.getClass());
        // logger.info("Title: " + titleInput);
        
        List<String> errors = new ArrayList<>();
        if (pickUpDate.isBefore(LocalDate.now()) || returnDate.isBefore(LocalDate.now())) {
            errors.add("Pickup date and return date must be in the future.");
        }
        if (!pickUpDate.isBefore(returnDate)) {
            errors.add("Pickup date must be before the return date.");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "Customer/rentMovie"; // return to the form page
        }


        HomePageData movie = userRepository.getMovieByTitle(titleInput);
        
        int movie_id = movie.getMovie_id();
        UserData user = (UserData)session.getAttribute("user");
        String phoneNum = user.getPhone();
        long rent_duration = ChronoUnit.DAYS.between(pickUpDate, returnDate); 
        long  total_price = movie.getBase_price() * rent_duration;

        userRepository.addMovieToCart(phoneNum, movie_id, pickUpDate, returnDate, total_price);
        return "redirect:/customer/homepage";
    }

    @GetMapping("/check-movie_title")
    @ResponseBody
    public ResponseEntity<HomePageData> checkIdPendaftaran(@RequestParam("title") String title){
        
        HomePageData movie = userRepository.getMovieByTitle(title);
        return ResponseEntity.ok(movie);
    }


    @GetMapping("/customer/cart")
    public String cart() {
        return "Customer/Cart"; // Mengarahkan ke halaman keranjang
    }
}