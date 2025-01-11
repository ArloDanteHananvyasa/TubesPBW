package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.user.HomePageData;
import com.example.demo.user.MovieDetailData;
import com.example.demo.user.UserRepository;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/homepage")
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
    public String details(@PathVariable String title, Model model) {
        HomePageData movieGenre = userRepository.getMovieByTitle(title);
        model.addAttribute("movieGenre", movieGenre);

        MovieDetailData movieActor = userRepository.getActorsByTitle(title);
        model.addAttribute("movieActor", movieActor);
        return "Customer/MoviesDetails/Avatar/avatar";
    }

    @GetMapping("/customer/movies")
    public String movieList(Model model) {
        List<HomePageData> movies = userRepository.getAllMovies();
        model.addAttribute("movies", movies);
        return "Customer/movieList"; // Mengarahkan ke halaman daftar film
    }

    @GetMapping("/customer/movies/filter")
    @ResponseBody
    public List<HomePageData> filterMovies(@RequestParam(required = false) List<String> genres){
        if (genres == null || genres.isEmpty() || genres.contains("All")) {
            return userRepository.getAllMovies();
        }
        return userRepository.getMoviesByGenres(genres.toArray(new String[0]));
    }

    @GetMapping("/customer/profile")
    public String profile() {
        return "Customer/profile"; // Mengarahkan ke halaman profil customer
    }

    @GetMapping("/myRentals")
    public String myRentals() {
        return "Customer/myRentals"; // Mengarahkan ke halaman riwayat sewa
    }

    @GetMapping("/customer/rentMovie")
    public String rentMovie() {
        return "Customer/rentMovie"; // Mengarahkan ke halaman sewa film
    }

    @GetMapping("/cart")
    public String cart() {
        return "Customer/Cart"; // Mengarahkan ke halaman keranjang
    }
}