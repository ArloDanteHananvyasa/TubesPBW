package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.user.HomePageData;
import com.example.demo.user.UserRepository;

@Controller
public class CustomerController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/customer/homepage")
    public String homepage(Model model) {
        List<HomePageData> latestMovies = userRepository.getMoviesFromLast5Years();
        model.addAttribute("latestMovies", latestMovies);

        List<HomePageData> adventureMovies = userRepository.getAdventureMovies();
        model.addAttribute("adventureMovies", adventureMovies);

        List<HomePageData> scifiMovies = userRepository.getScifiMovies();
        model.addAttribute("scifiMovies", scifiMovies);
        return "Customer/homepage"; // Mengarahkan ke halaman utama customer
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

    @GetMapping("/customer/rentMovie")
    public String rentMovie() {
        return "Customer/rentMovie"; // Mengarahkan ke halaman sewa film
    }

    @GetMapping("/customer/cart")
    public String cart() {
        return "Customer/Cart"; // Mengarahkan ke halaman keranjang
    }
}