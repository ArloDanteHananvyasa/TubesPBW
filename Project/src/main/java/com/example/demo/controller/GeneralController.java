package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    @GetMapping("/landing")
    public String landing() {
        return "General/landingPage";
    }

    @GetMapping("/login")
    public String login() {
        return "General/login";
    }

    @GetMapping("/register")
    public String register() {
        return "General/register";
    }

}
