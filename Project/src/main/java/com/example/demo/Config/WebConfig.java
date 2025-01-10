package com.example.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Nonnull;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@Nonnull ResourceHandlerRegistry registry) {
        // Serve files from the uploads folder (new folder)
        registry.addResourceHandler("/Assets/**")
                .addResourceLocations("file:/F:/Campus Stuff/Codes/PBW/TheReelDeal/TheReelDeal/moviePosters/"); // change
                                                                                                                // this
                                                                                                                // too

        // Serve files from the static folder (old folder)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

}