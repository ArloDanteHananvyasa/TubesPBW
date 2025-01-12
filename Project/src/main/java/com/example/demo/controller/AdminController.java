package com.example.demo.controller;


import java.text.ParseException;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.admin.datas.actorData;
import com.example.demo.admin.datas.billData;
import com.example.demo.admin.datas.customerData;
import com.example.demo.admin.datas.genreData;
import com.example.demo.admin.datas.movieData;
import com.example.demo.admin.datas.reportData;
import com.example.demo.admin.datas.transactionData;
import com.example.demo.admin.repositories.adminActorRepository;
import com.example.demo.admin.repositories.adminGeneralRepository;
import com.example.demo.admin.repositories.adminGenreRepository;
import com.example.demo.admin.repositories.adminMovieRepository;
import com.example.demo.user.UserData;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private adminGeneralRepository generalRepo;
    @Autowired
    private adminActorRepository actorRepo;
    @Autowired
    private adminGenreRepository genreRepo;
    @Autowired
    private adminMovieRepository movieRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<movieData> movies = movieRepo.getAllMovies(null);
        List<customerData> customers = generalRepo.getAllCustomers();

        model.addAttribute("totalMovies", movies.size());
        model.addAttribute("totalUsers", customers.size());

        // Example labels and data fetched from the database
        List<String> labels = List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        List<Integer> salesData = List.of(1200, 1900, 3000, 5000, 4000, 6000, 7000, 8000, 9000, 10000, 11000, 12000);

        int currYear = LocalDate.now().getYear();
        int totalRentals = generalRepo.getTotalRentalsInYear(currYear);

        model.addAttribute("labels", labels);
        model.addAttribute("salesData", salesData);

        model.addAttribute("totalRentals", totalRentals);
        int totalSales = generalRepo.getTotalSalesInYear(currYear);
        model.addAttribute("totalSales", totalSales);

        return "Admin/homepageAdmin"; // Mengarahkan ke halaman dashboard admin
    }

    @RequestMapping("/dashboard")
    public String dashboard(@RequestParam int year, Model model, HttpSession session) {
        
        List<movieData> movies = movieRepo.getAllMovies(null);
        List<customerData> customers = generalRepo.getAllCustomers();

        model.addAttribute("totalMovies", movies.size());
        model.addAttribute("totalUsers", customers.size());
        
        int totalRentals = generalRepo.getTotalRentalsInYear(year);

        model.addAttribute("totalRentals", totalRentals);

        int totalSales = generalRepo.getTotalSalesInYear(year);
        model.addAttribute("totalSales", totalSales);

        List<Integer> monthlySale = generalRepo.getMonthlySales(year);
        List<String> labels = List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        model.addAttribute("labels", labels);
        model.addAttribute("monthlySale", monthlySale);
        return "Admin/homepageAdmin";
    }

    @GetMapping("/movies")
    public String manageMovies(@RequestParam(value = "filter", required = false) String filter, Model model) {

        List<movieData> movies = new ArrayList<>();
        List<movieData> removedMovies = new ArrayList<>();

        if (filter != null) {
            movies = movieRepo.getAllMovies(filter);
            removedMovies = movieRepo.getRemovedMovies(filter);
            model.addAttribute("filterTitle", filter);
        } else {
            movies = movieRepo.getAllMovies(null);
            removedMovies = movieRepo.getRemovedMovies(null);
        }
        model.addAttribute("movies", movies);
        model.addAttribute("removedMovies", removedMovies);

        return "Admin/listMovies"; // Mengarahkan ke halaman manajemen film
    }

    @GetMapping("/movies/detail")
    public String movieDetail(@RequestParam("movieId") int id,
            @RequestParam(value = "filter", required = false) String filter, Model model) {

        List<movieData> movies = new ArrayList<>();
        List<movieData> removedMovies = new ArrayList<>();

        System.out.println(id);

        if (filter != null) {
            movies = movieRepo.getAllMovies(filter);
            removedMovies = movieRepo.getRemovedMovies(filter);
            model.addAttribute("filterTitle", filter);
        } else {
            movies = movieRepo.getAllMovies(null);
            removedMovies = movieRepo.getRemovedMovies(null);
        }
        model.addAttribute("movies", movies);
        model.addAttribute("removedMovies", removedMovies);

        movieData movieDetail = movieRepo.getMovieById(id);
        model.addAttribute("movieDetail", movieDetail);

        List<genreData> genreDetail = genreRepo.getGenreByMovieId(id);
        model.addAttribute("genres", genreDetail);

        List<actorData> actorDetail = actorRepo.getActorByMovieId(id);
        model.addAttribute("actors", actorDetail);

        System.out.println(movieDetail.getLandscapePoster());
        System.out.println(movieDetail.getPortraitPoster());

        return "Admin/listMovies";
    }

    @GetMapping("/movies/add-movies")
    public String addMovies(HttpSession session, Model model) {
        List<genreData> genres = genreRepo.getAllGenres(null);
        model.addAttribute("movieGenres", genres);

        List<actorData> actors = actorRepo.getAllActors(null);
        model.addAttribute("actors", actors);

        return "Admin/addMovie"; // Mengarahkan ke halaman tambah film
    }

    @GetMapping("/movies/update")
    public String updateMovie(@RequestParam("movieId") int id, HttpSession session, Model model) {
        movieData movieDetail = movieRepo.getMovieById(id);

        session.setAttribute("currentMovie", movieDetail);

        List<genreData> genres = genreRepo.getAllGenres(null);
        model.addAttribute("movieGenres", genres);

        List<actorData> actors = actorRepo.getAllActors(null);
        model.addAttribute("actors", actors);

        List<genreData> selectedGenres = genreRepo.getGenreByMovieId(id);
        List<Integer> selectedGenresId = new ArrayList<>();
        for (genreData genre : selectedGenres) {
            selectedGenresId.add(genre.getGenre_id());
        }
        model.addAttribute("selectedGenres", selectedGenresId);

        List<actorData> selectedActors = actorRepo.getActorByMovieId(id);
        List<Integer> selectedActorsId = new ArrayList<>();
        for (actorData actor : selectedActors) {
            selectedActorsId.add(actor.getActor_id());
        }
        model.addAttribute("selectedActors", selectedActorsId);

        model.addAttribute("movieDetail", movieDetail);

        return "Admin/editMovie";
    }

    @PostMapping("/movies/update/submit")
    public String updateMovieSubmit(
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

        movieData currentMovie = (movieData) session.getAttribute("currentMovie");

        if (!movieRepo.editMovie(currentMovie.getMovie_id(), movieTitle, description,
                releaseYear, duration, stock, selectedGenres, selectedActors,
                moviePosterLandscape, moviePosterPortrait)) {
            model.addAttribute("error", "The title '" + movieTitle + "' already exists");
            return "redirect:/admin/movies/update?movieId=" + currentMovie.getMovie_id();
        }

        return "redirect:/admin/movies";
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

        movieRepo.addMovie(movieTitle, description, releaseYear, duration, stock, selectedGenres, selectedActors,
                moviePosterLandscape, moviePosterPortrait);

        return "redirect:/admin/movies";
    }

    @GetMapping("/movies/remove")
    public String removeMovies(@RequestParam("movieId") int id) {
        movieRepo.removeMovie(id);

        return "redirect:/admin/movies";
    }

    @GetMapping("/movies/restore")
    public String retoreMovies(@RequestParam("movieId") int id) {

        movieRepo.restoreMovie(id);

        return "redirect:/admin/movies";
    }

    @GetMapping("/actors")
    public String manageActors(@RequestParam(value = "filter", required = false) String filter, Model model) {

        List<actorData> actors = new ArrayList<>();
        List<actorData> deletedActors = new ArrayList<>();

        if (filter != null) {
            actors = actorRepo.getAllActors(filter);
            deletedActors = actorRepo.getRemovedActors(filter);
            model.addAttribute("filterName", filter);
        } else {
            actors = actorRepo.getAllActors(null);
            deletedActors = actorRepo.getRemovedActors(null);
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
        actorRepo.removeActor(id);

        return "redirect:/admin/actors";
    }

    @GetMapping("/actors/restore")
    public String restoreActor(@RequestParam("actorId") int id) {
        actorRepo.restoreActor(id);

        return "redirect:/admin/actors";
    }

    @PostMapping("/actors/add-actors/submit")
    public String addActorSubmit(@RequestParam("actorName") String name, Model model) {

        if (!actorRepo.addActor(name)) {
            model.addAttribute("error", "Actor with the same name already exists");
            return "Admin/addActor";
        } else {
            return "redirect:/admin/actors";
        }
    }

    @GetMapping("/actors/update")
    public String updateActor(@RequestParam("actorId") int id, HttpSession session, Model model) {
        actorData actor = actorRepo.getActorById(id);

        session.setAttribute("currentActor", actor);
        model.addAttribute("actor", actor.getActorName());

        return "Admin/editActor"; // Mengarahkan ke halaman tambah film
    }

    @PostMapping("/actors/update/submit")
    public String updateActorSubmit(@RequestParam("actorName") String newName, HttpSession session, Model model) {
        actorData current = (actorData) session.getAttribute("currentActor");

        System.out.println(current.getActor_id());
        System.out.println(current.getActorName());

        if (!actorRepo.updateActor(current.getActor_id(), newName)) {
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
            genres = genreRepo.getAllGenres(filter);
            removedGenres = genreRepo.getRemovedGenres(filter);
            model.addAttribute("filterName", filter);
        } else {
            genres = genreRepo.getAllGenres(null);
            removedGenres = genreRepo.getRemovedGenres(null);
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
        genreRepo.removeGenre(id);

        return "redirect:/admin/genres";
    }

    @GetMapping("/genres/restore")
    public String restoreGenre(@RequestParam("genreId") int id) {
        genreRepo.restoreGenre(id);

        return "redirect:/admin/genres";
    }

    @PostMapping("/genres/add-genres/submit")
    public String submitGenre(@RequestParam("movieGenre") String name, Model model) {

        if (!genreRepo.addGenre(name)) {
            model.addAttribute("error", "Genre with the same name already exists");
            return "Admin/addGenres";
        } else {
            return "redirect:/admin/genres";
        }
    }

    @GetMapping("/transactions")
    public String manageTransactions(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            Model model) {

        List<transactionData> transactions = generalRepo.getAllTransactions(startDate, endDate);

        model.addAttribute("transactions", transactions);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "Admin/listTransactions";
    }

    @GetMapping("/transactions/detail")
    public String detailTransaction(@RequestParam("transactionId") int id, Model model) {

        transactionData detail = generalRepo.getTransactionById(id);
        List<movieData> movies = generalRepo.getMoviesByTransactionId(id);

        model.addAttribute("transactionDetail", detail);
        model.addAttribute("transactionMovies", movies);

        return "Admin/listTransactions";
    }

    @GetMapping("/transactions/bill")
    public String billTransaction(@RequestParam("transactionId") int id, HttpSession session, Model model) {

        session.setAttribute("currentTransactionId", id);
        billData bill = generalRepo.getBillByTransactionId(id);

        System.out.println(bill);

        model.addAttribute("bill", bill);
        session.setAttribute("bill", bill);

        return "Admin/listTransactions";
    }

    @GetMapping("/transactions/confirm-pickup")
    public String confirmPickup(@RequestParam("transactionId") int id, HttpSession session, Model model) {

        generalRepo.confirmPickup(id);

        return "redirect:/admin/transactions";
    }

    @PostMapping("/transactions/bill/complete")
    public String completeBill(@RequestParam("paymentMethod") int methodId, HttpSession session) {
        int transaction_id = (int) session.getAttribute("currentTransactionId");

        billData bd = (billData) session.getAttribute("bill");

        System.out.println(bd);

        generalRepo.completeBill(transaction_id, methodId, bd.getLate_fee());

        return "redirect:/admin/transactions";
    }

    @GetMapping("/sales-report")
    public String salesReport(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "customerName", required = false) String customerName,
            Model model) {

        // Pass the parameters to the service layer, leaving them null if not provided

        System.out.println(startDate);

        List<reportData> reportList = generalRepo.getReport(startDate, endDate, customerName);

        // Add the report data to the model
        model.addAttribute("transactions", reportList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("customerName", customerName);

        return "Admin/salesReport"; // Directing to the sales report page
    }


    @GetMapping("/register-new-admin")
    public String register() {
        return "Admin/registerNewAdmin";
    }

    @PostMapping("/register-new-admin/submit")
    public String registerUser(@Valid @ModelAttribute UserData userData, Model model, BindingResult bindingResult)
            throws ParseException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Please correct the highlighted errors.");
            return "Admin/registerNewAdmin";
        }

        // Check Phone Number
        if (userRepo.findByPhone(userData.getPhone()).isPresent()) {
            model.addAttribute("error", "Number already exists.");
            return "Admin/registerNewAdmin";
        }

        // Check Email
        if (!userData.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            model.addAttribute("error", "Please enter a valid email address");
            return "Admin/registerNewAdmin";
        }

        // Check Passwords
        if (!userData.getPassword().equals(userData.getConfpassword())) {
            model.addAttribute("error", "Passwords do not Match!");
            return "Admin/registerNewAdmin";
        }

        boolean isRegistered = userService.registerAdmin(userData);
        if (!isRegistered) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "Admin/registerNewAdmin";
        }

        return "redirect:/login";
    }
}