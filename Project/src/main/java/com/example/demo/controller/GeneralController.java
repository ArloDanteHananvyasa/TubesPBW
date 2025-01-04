package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.user.UserData;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;

import jakarta.servlet.http.HttpSession;

import javax.validation.Valid;
import java.text.ParseException;

@Controller
public class GeneralController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String landing(HttpSession session) {
        session.setAttribute("user", null);
        session.setAttribute("role", null);

        return "General/landingPage";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "General/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session,
            Model model) {

        UserData user = userService.login(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            String role = user.getRole();
            return switch (role) {
                case "admin" -> "redirect:/admin/dashboard";
                case "user" -> "redirect:/customer/homepage";
                default -> "redirect:";
            };

        } else {
            model.addAttribute("error", "Invalid email or password");
            return "General/login";
        }
    }

    @GetMapping("/register")
    public String register() {
        return "General/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserData userData, Model model, BindingResult bindingResult)
            throws ParseException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Please correct the highlighted errors.");
            return "General/register";
        }

        // Check Phone Number
        if (userRepository.findByPhone(userData.getPhone()).isPresent()) {
            model.addAttribute("error", "Number already exists.");
            return "General/register";
        }

        // Check Email
        if (!userData.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            model.addAttribute("error", "Please enter a valid email address");
            return "General/register";
        }

        // Check Passwords
        if (!userData.getPassword().equals(userData.getConfpassword())) {
            model.addAttribute("error", "Passwords do not Match!");
            return "General/register";
        }

        boolean isRegistered = userService.register(userData);
        if (!isRegistered) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "General/register";
        }

        return "redirect:/login";
    }

}
