package com.example.demo.admin.datas;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class transactionData {
    private int transaction_id;
    private String customerPhone;
    private String customerName;
    private String orderDate;
    private String pickupDate;
    private String dueReturnDate;
    private String actualReturnDate;
    private int days;
    private double basePrice;
    private double lateFee;
    private int method_id;
    private String method_name;
}
