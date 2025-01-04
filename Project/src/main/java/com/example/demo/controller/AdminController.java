package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Admin/homepageAdmin"; // Mengarahkan ke halaman dashboard admin
    }

    @GetMapping("/movies")
    public String manageMovies() {
        return "Admin/listMovies"; // Mengarahkan ke halaman manajemen film
    }

    @GetMapping("/movies/add-movies")
    public String addMovies() {
        return "Admin/addMovie"; // Mengarahkan ke halaman tambah film
    }

    @GetMapping("/actors")
    public String manageActors() {
        return "Admin/listActors"; // Mengarahkan ke halaman manajemen aktor
    }

    @GetMapping("/actors/add-actors")
    public String addActor() {
        return "Admin/addActor"; // Mengarahkan ke halaman tambah film
    }

    @GetMapping("/genres")
    public String manageGenres() {
        return "Admin/listGenres"; // Mengarahkan ke halaman manajemen genre
    }

    @GetMapping("/genres/add-genres")
    public String addGenre() {
        return "Admin/addGenres "; // Mengarahkan ke halaman tambah film
    }

    @GetMapping("/sales-report")
    public String salesReport() {
        return "Admin/salesReport"; // Mengarahkan ke halaman laporan penjualan
    }

}