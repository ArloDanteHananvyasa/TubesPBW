package com.example.demo.admin.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.admin.datas.billData;
import com.example.demo.admin.datas.customerData;
import com.example.demo.admin.datas.movieData;
import com.example.demo.admin.datas.reportData;
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

        // Retrieve transaction data
        transactionData td = getTransactionById(transaction_id);
        int days = td.getDays(); // Get the number of days from the transaction data

        // Calculate the due_date by adding the number of days to today's date
        LocalDate dueDate = today.plusDays(days);

        // Update both pickup_date and due_date
        jdbc.update("UPDATE transactions SET pickup_date = ?, due_date = ? WHERE transaction_id = ?", today, dueDate,
                transaction_id);
    }

    private double calculateLateFee(transactionData td) {
        double lateFee = 0;

        // Get today's date for comparison
        LocalDate today = LocalDate.now();

        // Check if due_date is not null or empty
        String dueDateStr = td.getDueReturnDate();

        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            try {
                // Convert due_date from String to LocalDate
                LocalDate dueDate = LocalDate.parse(dueDateStr); // Assuming dueDate is in YYYY-MM-DD format

                // Calculate late fee based on today's date (because the item is being returned
                // now)
                if (today.isAfter(dueDate)) {
                    long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);
                    lateFee = daysLate * 10000; // Calculate late fee
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error parsing dates: " + e.getMessage());
            }
        }
        return lateFee;
    }

    @Override
    public void completeBill(int transaction_id, int method_id, double lateFee) {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Update the return_date to today's date and set the payment method
        jdbc.update(
                "UPDATE transactions SET payment_method_id = ?, return_date = ?, late_fee = ? WHERE transaction_id = ?",
                method_id, today, lateFee, transaction_id);
    }

    @Override
    public billData getBillByTransactionId(int id) {
        // Get the transaction data
        transactionData td = getTransactionById(id);

        // Calculate the late fee (based on today's date since the item is just being
        // returned)
        double lateFee = calculateLateFee(td);

        // Return billData object, including base fee and calculated late fee
        return new billData(td.getBasePrice(), lateFee, td.getBasePrice() + lateFee);
    }

    @Override
    public List<reportData> getReport(String startDate, String endDate, String customerName) {
        // Base query for selecting data from the joined tables
        StringBuilder query = new StringBuilder(
                "SELECT * FROM transactions t JOIN users u on u.phone = t.phone JOIN payment_method p on p.method_id = t.payment_method_id WHERE 1=1");

        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(customerName);

        // List to hold parameters for the query
        List<Object> params = new ArrayList<>();

        // Parse and add condition for startDate if provided
        if (startDate != null && !startDate.isEmpty()) {
            try {
                // Parse startDate to LocalDate
                LocalDate parsedStartDate = LocalDate.parse(startDate);
                query.append(" AND t.transaction_date >= ?");
                params.add(parsedStartDate);
            } catch (DateTimeParseException e) {
                // Handle invalid date format (optional)
                System.err.println("Invalid start date format: " + startDate);
            }
        }

        // Parse and add condition for endDate if provided
        if (endDate != null && !endDate.isEmpty()) {
            try {
                // Parse endDate to LocalDate
                LocalDate parsedEndDate = LocalDate.parse(endDate);
                query.append(" AND t.transaction_date <= ?");
                params.add(parsedEndDate);
            } catch (DateTimeParseException e) {
                // Handle invalid date format (optional)
                System.err.println("Invalid end date format: " + endDate);
            }
        }

        // Add condition for customerName if provided
        if (customerName != null && !customerName.isEmpty()) {
            query.append(" AND u.name ILIKE ?");
            params.add("%" + customerName + "%"); // Use LIKE for partial matching
        }

        // Execute the query with the specified parameters and return the results
        return jdbc.query(query.toString(), this::mapRowToReportData, params.toArray());
    }

    private reportData mapRowToReportData(ResultSet rs, int rowNum) throws SQLException {
        // Extract the fields from the ResultSet
        String customerName = rs.getString("name");
        String orderDate = rs.getString("transaction_date");
        double basePrice = rs.getDouble("base_fee");
        double lateFee = rs.getDouble("late_fee");
        String methodName = rs.getString("method_name");

        // Calculate the total (basePrice + lateFee)
        double total = basePrice + lateFee;

        // Format the total using DecimalFormat
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Set the grouping separator as dot
        DecimalFormat decimalFormat = new DecimalFormat("'Rp. '#,###", symbols);
        String formattedTotal = decimalFormat.format(total);

        // Return the formatted reportData object
        return new reportData(customerName, orderDate, formattedTotal, methodName);
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
