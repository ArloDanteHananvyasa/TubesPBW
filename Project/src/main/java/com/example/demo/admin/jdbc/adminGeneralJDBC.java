package com.example.demo.admin.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.admin.datas.billData;
import com.example.demo.admin.datas.customerData;
import com.example.demo.admin.datas.movieData;
import com.example.demo.admin.datas.transactionData;
import com.example.demo.admin.repositories.adminGeneralRepository;

@Repository
public class adminGeneralJDBC implements adminGeneralRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<customerData> getAllCustomers() {
        return jdbc.query("SELECT * FROM users WHERE role ILIKE 'user'", this::mapRowToCustomerData);
    }

    private customerData mapRowToCustomerData(ResultSet rs, int rowNum)
            throws SQLException {
        // Create pemeriksaanData object using constructor
        return new customerData(
                rs.getString("phone"),
                rs.getString("username"),
                rs.getString("name"),
                rs.getString("email"));
    }

    @Override
    public List<transactionData> getAllTransactions(String startDate, String endDate) {
        StringBuilder query = new StringBuilder(
                "SELECT t.transaction_id, t.phone, u.name AS customerName, t.transaction_date, t.pickup_date, t.due_date, t.return_date, t.days, t.base_fee, t.late_fee, t.payment_method_id, p.method_name FROM transactions t JOIN users u ON t.phone = u.phone LEFT JOIN payment_method p ON t.payment_method_id = p.method_id WHERE (t.pickup_date IS NULL OR t.return_date IS NULL)");

        List<Object> params = new ArrayList<>();

        // Add date filters if provided
        if (startDate != null && !startDate.isEmpty()) {
            query.append(" AND t.transaction_date >= ?");
            params.add(java.sql.Date.valueOf(startDate));
        }

        if (endDate != null && !endDate.isEmpty()) {
            query.append(" AND t.transaction_date <= ?");
            params.add(java.sql.Date.valueOf(endDate));
        }

        return jdbc.query(query.toString(), this::mapRowToTransactionData, params.toArray());
    }

    @Override
    public List<movieData> getMoviesByTransactionId(int id) {
        return jdbc.query(
                "SELECT * FROM transaction_details join movies on transaction_details.movie_id = movies.movie_id WHERE transaction_id = ?",
                this::mapRowToMovieData, id);
    }

    @Override
    public void confirmPickup(int transaction_id) {
        LocalDate today = LocalDate.now();

        jdbc.update("UPDATE transactions SET pickup_date = ? WHERE transaction_id = ?", today, transaction_id);
    }

    @Override
    public void completeBill(int transaction_id, int method_id) {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Update the return_date to today's date and set the payment method
        jdbc.update("UPDATE transactions SET payment_method_id = ?, return_date = ? WHERE transaction_id = ?",
                method_id, today, transaction_id);
    }

    @Override
    public billData getBillByTransactionId(int id) {
        transactionData td = getTransactionById(id);

        double lateFee = 0;

        // Check if return_date and due_date are not null or empty
        String returnDateStr = td.getActualReturnDate();
        String dueDateStr = td.getDueReturnDate();

        if (returnDateStr != null && !returnDateStr.isEmpty() && dueDateStr != null && !dueDateStr.isEmpty()) {
            // Convert return_date and due_date from String to LocalDate
            LocalDate returnDate = LocalDate.parse(returnDateStr); // Assuming returnDate is in YYYY-MM-DD format
            LocalDate dueDate = LocalDate.parse(dueDateStr); // Assuming dueDate is in YYYY-MM-DD format

            // Check if return_date is greater than due_date to calculate late fee
            if (returnDate.isAfter(dueDate)) {
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnDate);
                lateFee = daysLate * 10000; // Calculate late fee
            }
        }

        return new billData(td.getBasePrice(), lateFee, td.getBasePrice() + lateFee);
    }

    @Override
    public transactionData getTransactionById(int id) {

        return jdbc.queryForObject(
                "SELECT t.transaction_id, t.phone, u.name AS customerName, t.transaction_date, t.pickup_date, t.due_date, t.return_date, t.days, t.base_fee, t.late_fee, t.payment_method_id, p.method_name FROM transactions t JOIN users u ON t.phone = u.phone LEFT JOIN payment_method p ON t.payment_method_id = p.method_id WHERE transaction_id = ?",
                this::mapRowToTransactionData,
                id);
    }

    private movieData mapRowToMovieData(ResultSet rs, int rowNum)
            throws SQLException {
        // Create pemeriksaanData object using constructor
        return new movieData(
                rs.getInt("movie_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("release_year"),
                rs.getInt("duration"),
                rs.getDouble("base_price"),
                rs.getString("landscapePoster"),
                rs.getString("portraitPoster"),
                rs.getInt("stock"));
    }

    private transactionData mapRowToTransactionData(ResultSet rs, int rowNum)
            throws SQLException {
        // Create transactionData object using constructor
        return new transactionData(
                rs.getInt("transaction_id"),
                rs.getString("phone"),
                rs.getString("customerName"),
                rs.getString("transaction_date"),
                rs.getString("pickup_date"),
                rs.getString("due_date"),
                rs.getString("return_date"),
                rs.getInt("days"),
                rs.getDouble("base_fee"),
                rs.getDouble("late_fee"),
                rs.getInt("payment_method_id"),
                rs.getString("method_name"));
    }
}
