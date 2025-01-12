package com.example.demo.admin.repositories;

import java.util.List;

import com.example.demo.admin.datas.billData;
import com.example.demo.admin.datas.customerData;
import com.example.demo.admin.datas.movieData;
import com.example.demo.admin.datas.reportData;
import com.example.demo.admin.datas.transactionData;

public interface adminGeneralRepository {

    List<customerData> getAllCustomers();

    List<transactionData> getAllTransactions(String startDate, String endDate);

    transactionData getTransactionById(int id);

    List<movieData> getMoviesByTransactionId(int id);

    billData getBillByTransactionId(int id);

    void confirmPickup(int transaction_id);

    void completeBill(int transaction_id, int method_id, double lateFee);

    List<reportData> getReport(String startDate, String endDate, String customerName);

    int getTotalRentalsInYear(int year);

    int getTotalSalesInYear(int year);
    List<Integer>getMonthlySales(int year);
}
