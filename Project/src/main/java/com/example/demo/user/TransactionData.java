package com.example.demo.user;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class TransactionData{
    private String title;
    private LocalDate pickup_date;
    private LocalDate due_date;
    private LocalDate return_date; //can be null if not returned yet
    private String status; //if return_date is null then status is "Not Returned", else returned
    private int daysTillDue; //localdate.now() till due_date, kalo negatif, 

    //kalo belom returned
    public TransactionData(String title, LocalDate pickup_date, LocalDate due_date) {
        this.title = title;
        this.pickup_date = pickup_date;
        this.due_date = due_date;
        this.return_date = null;
        LocalDate now = LocalDate.now();
        this.daysTillDue = (int)ChronoUnit.DAYS.between(now, due_date); 
        //
        if(daysTillDue < 0){
            this.status = "Overdue";
        }
        else{
            this.status = "Not Returned";
        }
    }

    //kalo udah returned
    public TransactionData(String title, LocalDate pickup_date, LocalDate due_date, LocalDate return_date){
        this.title = title;
        this.pickup_date = pickup_date;
        this.due_date = due_date;
        this.return_date = return_date;
        this.daysTillDue = (int)ChronoUnit.DAYS.between(pickup_date, due_date); 
        this.status = "Returned";
    }


}
