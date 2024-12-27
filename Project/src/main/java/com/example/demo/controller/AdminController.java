package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "Admin/homepageAdmin"; // Mengarahkan ke halaman dashboard admin
    }

    @GetMapping("/admin/movies")
    public String manageMovies() {
        return "Admin/listMovies"; // Mengarahkan ke halaman manajemen film
    }

    @GetMapping("/admin/movies/add-movies")
    public String addMovies() {
        return "Admin/addMovie"; // Mengarahkan ke halaman tambah film
    }

    @GetMapping("/admin/actors")
    public String manageActors() {
        return "Admin/listActors"; // Mengarahkan ke halaman manajemen aktor
    }

    @GetMapping("/admin/actors/add-actors")
    public String addActor() {
        return "Admin/addActor"; // Mengarahkan ke halaman tambah film
    }

    @GetMapping("/admin/genres")
    public String manageGenres() {
        return "Admin/listGenres"; // Mengarahkan ke halaman manajemen genre
    }

    @GetMapping("/admin/genres/add-genres")
    public String addGenre() {
        return "Admin/addGenres "; // Mengarahkan ke halaman tambah film
    }

    @GetMapping("/admin/sales-report")
    public String salesReport() {
        return "Admin/salesReport"; // Mengarahkan ke halaman laporan penjualan
    }

}