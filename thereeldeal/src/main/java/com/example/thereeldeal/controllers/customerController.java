package com.example.thereeldeal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class customerController {

    @GetMapping("")
    public String landingPage(){
        return "Customer/landingPage";
    }

    @GetMapping("/home")
    public String homePage(){
        return "Customer/homepage";
    }
}
