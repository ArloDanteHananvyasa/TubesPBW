package com.example.demo.user;
import java.text.NumberFormat;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class CartData {
    public CartData(int movieId, String title, int base_price, String portraitPoster) {
        this.title = title;
        this.base_price = base_price;
        NumberFormat rupiahFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        this.formattedBasePrice =  rupiahFormatter.format(base_price);
        this.portraitPoster = portraitPoster;
        this.movieId = movieId;
    }

    private int movieId;
    private String title;
    private int base_price;
    private String portraitPoster;
    private String formattedBasePrice;
}
