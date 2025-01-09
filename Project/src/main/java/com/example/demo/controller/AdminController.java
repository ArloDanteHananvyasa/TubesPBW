package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.admin.adminRepository;
import com.example.demo.admin.datas.actorData;
import com.example.demo.admin.datas.customerData;
import com.example.demo.admin.datas.genreData;
import com.example.demo.admin.datas.movieData;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private adminRepository repo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<movieData> movies = repo.getAllMovies(null);
        List<customerData> customers = repo.getAllCustomers();

        model.addAttribute("totalMovies", movies.size());
        model.addAttribute("totalUsers", customers.size());

        return "Admin/homepageAdmin"; // Mengarahkan ke halaman dashboard admin
    }

    @GetMapping("/movies")
    public String manageMovies(@RequestParam(value = "filter", required = false) String filter, Model model) {

        List<movieData> movies = new ArrayList<>();
        List<movieData> removedMovies = new ArrayList<>();

        if (filter != null) {
            movies = repo.getAllMovies(filter);
            removedMovies = repo.getRemovedMovies(filter);
            model.addAttribute("filterTitle", filter);
        } else {
            movies = repo.getAllMovies(null);
            removedMovies = repo.getRemovedMovies(null);
        }
        model.addAttribute("movies", movies);
        model.addAttribute("removedMovies", removedMovies);

        return "Admin/listMovies"; // Mengarahkan ke halaman manajemen film
    }

    @GetMapping("/movies/add-movies")
    public String addMovies(HttpSession session, Model model) {
        List<genreData> genres = repo.getAllGenres(null);
        model.addAttribute("movieGenres", genres);

        List<actorData> actors = repo.getAllActors(null);
        model.addAttribute("actors", actors);

        return "Admin/addMovie"; // Mengarahkan ke halaman tambah film
    }

    @PostMapping("/movies/add-movies/submit")
    public String submitMovie(
            @RequestParam("movieTitle") String movieTitle,
            @RequestParam("movieDescription") String description,
            @RequestParam("movieReleaseYear") String releaseYear,
            @RequestParam("movieDuration") int duration,
            @RequestParam("movieStock") int stock,
            @RequestParam("moviePrice") double price,
            @RequestParam("selectedGenres") List<Integer> selectedGenres,
            @RequestParam("selectedActors") List<Integer> selectedActors,
            @RequestParam("moviePosterLandscape") MultipartFile moviePosterLandscape,
            @RequestParam("moviePosterPortrait") MultipartFile moviePosterPortrait,
            HttpSession session,
            Model model) {

        // debug
        System.out.println("Movie Title: " + movieTitle);
        System.out.println("Movie Description: " + description);
        System.out.println("Movie Release Year: " + releaseYear);
        System.out.println("Movie Duration: " + duration);
        System.out.println("Movie Stock: " + stock);
        System.out.println("Movie Price: " + price);
        System.out.println("Selected Genres: " + selectedGenres);
        System.out.println("Selected Actors: " + selectedActors);
        System.out.println("Movie Poster Landscape: " + moviePosterLandscape);
        System.out.println("Movie Poster Portrait: " + moviePosterPortrait);

        repo.addMovie(movieTitle, description, releaseYear, duration, stock, selectedGenres, selectedActors,
                moviePosterLandscape, moviePosterPortrait);

        return "redirect:/admin/movies";
    }

    @GetMapping("/movies/remove")
    public String removeMovies(@RequestParam("movieId") int id) {
        repo.removeMovie(id);

        return "redirect:/admin/movies";
    }

    @GetMapping("/movies/restore")
    public String retoreMovies(@RequestParam("movieId") int id) {

        repo.restoreMovie(id);

        return "redirect:/admin/movies";
    }

    @GetMapping("/actors")
    public String manageActors(@RequestParam(value = "filter", required = false) String filter, Model model) {

        List<actorData> actors = new ArrayList<>();
        List<actorData> deletedActors = new ArrayList<>();

        if (filter != null) {
            actors = repo.getAllActors(filter);
            deletedActors = repo.getRemovedActors(filter);
            model.addAttribute("filterName", filter);
        } else {
            actors = repo.getAllActors(null);
            deletedActors = repo.getRemovedActors(null);
        }
        model.addAttribute("actors", actors);
        model.addAttribute("removedActors", deletedActors);

        return "Admin/listActors"; // Mengarahkan ke halaman manajemen aktor
    }

    @GetMapping("/actors/add-actors")
    public String addActor() {
        return "Admin/addActor"; // Mengarahkan ke halaman tambah film
    }

    @GetMapping("/actors/remove")
    public String removeActor(@RequestParam("actorId") int id) {
        repo.removeActor(id);

        return "redirect:/admin/actors";
    }

    @GetMapping("/actors/restore")
    public String restoreActor(@RequestParam("actorId") int id) {
        repo.restoreActor(id);

        return "redirect:/admin/actors";
    }

    @PostMapping("/actors/add-actors/submit")
    public String addActorSubmit(@RequestParam("actorName") String name, Model model) {

        if (!repo.addActor(name)) {
            model.addAttribute("error", "Actor with the same name already exists");
            return "Admin/addActor";
        } else {
            return "redirect:/admin/actors";
        }
    }

    @GetMapping("/actors/update")
    public String updateActor(@RequestParam("actorId") int id, HttpSession session, Model model) {
        actorData actor = repo.getActorById(id);

        session.setAttribute("currentActor", actor);
        model.addAttribute("actor", actor.getActorName());

        return "Admin/editActor"; // Mengarahkan ke halaman tambah film
    }

    @PostMapping("/actors/update/submit")
    public String updateActorSubmit(@RequestParam("actorName") String newName, HttpSession session, Model model) {
        actorData current = (actorData) session.getAttribute("currentActor");

        System.out.println(current.getActor_id());
        System.out.println(current.getActorName());

        if (!repo.updateActor(current.getActor_id(), newName)) {
            model.addAttribute("error", "Actor with the same name already exists");
            return "Admin/addActor";
        } else {
            return "redirect:/admin/actors";
        }
    }

    @GetMapping("/genres")
    public String manageGenres(@RequestParam(value = "filter", required = false) String filter, Model model) {

        List<genreData> genres = new ArrayList<>();
        List<genreData> removedGenres = new ArrayList<>();

        if (filter != null) {
            genres = repo.getAllGenres(filter);
            removedGenres = repo.getRemovedGenres(filter);
            model.addAttribute("filterName", filter);
        } else {
            genres = repo.getAllGenres(null);
            removedGenres = repo.getRemovedGenres(null);
        }
        model.addAttribute("genres", genres);
        model.addAttribute("removedGenres", removedGenres);

        return "Admin/listGenres"; // Mengarahkan ke halaman manajemen genre
    }

    @GetMapping("/genres/add-genres")
    public String addGenre() {
        return "Admin/addGenres"; // Mengarahkan ke halaman tambah film
    }

    @GetMapping("/genres/remove")
    public String removeGenre(@RequestParam("genreId") int id) {
        repo.removeGenre(id);

        return "redirect:/admin/genres";
    }

    @GetMapping("/genres/restore")
    public String restoreGenre(@RequestParam("genreId") int id) {
        repo.restoreGenre(id);

        return "redirect:/admin/genres";
    }

    @PostMapping("/genres/add-genres/submit")
    public String submitGenre(@RequestParam("movieGenre") String name, Model model) {

        if (!repo.addGenre(name)) {
            model.addAttribute("error", "Genre with the same name already exists");
            return "Admin/addGenres";
        } else {
            return "redirect:/admin/genres";
        }
    }

    @GetMapping("/sales-report")
    public String salesReport() {
        return "Admin/salesReport"; // Mengarahkan ke halaman laporan penjualan
    }

}