package com.example.demo.admin.datas;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class movieData {
    private int movie_id;
    private String title;
    private String description;
    private String releaseYear;
    private int duration;
    private double basePrice;
    private String landscapePoster;
    private String potraitPoster;
}

// private movieData mapRowToPemeriksaanData(ResultSet rs, int rowNum)
// throws SQLException {
// // Create pemeriksaanData object using constructor
// return new movieData(
// rs.getInt("movie_id"),
// rs.getString("title"),
// rs.getString("description"),
// rs.getString("release_year"),
// rs.getInt("duration"),
// rs.getDouble("base_price"),
// rs.getString("landscapePoster"),
// rs.getString("portraitPoster"));
// }
