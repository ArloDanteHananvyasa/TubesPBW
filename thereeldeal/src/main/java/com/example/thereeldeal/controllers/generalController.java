package com.example.thereeldeal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class generalController {
    
    @GetMapping("/test")
    public String loginPage(){
        return "loginPage";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "general/register";
    }
}
