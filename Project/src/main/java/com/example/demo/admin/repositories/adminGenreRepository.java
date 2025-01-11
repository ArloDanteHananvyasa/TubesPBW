package com.example.demo.admin.repositories;

import java.util.List;

import com.example.demo.admin.datas.genreData;

public interface adminGenreRepository {
    List<genreData> getAllGenres(String filter);

    List<genreData> getRemovedGenres(String filter);

    void removeGenre(int id);

    void restoreGenre(int id);

    genreData getGenreById(int id);

    List<genreData> getGenreByMovieId(int id);

    boolean updateGenre(int id, String genre);

    boolean addGenre(String genre);
}
