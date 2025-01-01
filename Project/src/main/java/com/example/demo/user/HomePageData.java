package com.example.demo.user;

import lombok.Data;

@Data
public class HomePageData {
    private int movie_id;
    private String title;
    private String description;
    private String genres_names;
    private String release_year;
    private int duration;
    private String landscapeposter;
    private String portraitposter;
}
