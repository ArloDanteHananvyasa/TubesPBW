package com.example.thereeldeal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.thereeldeal.User.UserData;
import com.example.thereeldeal.User.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    //ganti redirectnya kemana
    @GetMapping("/login")
    public String loginView(HttpSession session){
        if(session.getAttribute("user") != null){  //kalo udah login ke user yg loggedin
            return "redirect:/user/";
        }
        return "User/login";
    }

    //login page email/username
    @PostMapping("/login")
    public String login(@RequestParam String emailUsername, @RequestParam String password, HttpSession session, Model model){
        //logger.info("Attempting to log in with email: {}", email);

        UserData user = userService.login(emailUsername, password); //di service ceknya dua itu, sqlnya gi dibikin arlo
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole()); 
            logger.info("Login successful for user: {}", emailUsername);    //logger

            String role = user.getRole();
            return switch (role) {
                case "admin" -> "redirect:/admin/";
                case "customer" -> "redirect:/customer/";
                default -> "redirect:/customer/";
            };
        } 
        else {
            logger.warn("Login failed for email: {}", emailUsername);   //logger
            model.addAttribute("error", "Invalid emailUsername or password");
            return "User/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/"; //sesuaiin controller
    }

}

