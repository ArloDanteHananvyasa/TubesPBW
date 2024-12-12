package com.example.thereeldeal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class generalController {
    
    @GetMapping("/login")
    public String loginPage(){
        return "General/loginPage";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "General/register";
    }
}
