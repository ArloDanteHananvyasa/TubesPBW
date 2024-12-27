package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

    @GetMapping("/customer/homepage")
    public String homepage() {
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