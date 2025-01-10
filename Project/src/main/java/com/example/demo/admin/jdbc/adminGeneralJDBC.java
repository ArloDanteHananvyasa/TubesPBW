package com.example.demo.admin.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.admin.datas.customerData;
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

}
