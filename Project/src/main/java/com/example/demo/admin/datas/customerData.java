package com.example.demo.admin.datas;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class customerData {
    private String phone;
    private String username;
    private String name;
    private String email;
}

// private customerData mapRowToPemeriksaanData(ResultSet rs, int rowNum)
// throws SQLException {
// // Create pemeriksaanData object using constructor
// return new customerData(
// rs.getString("phone"),
// rs.getString("username"),
// rs.getString("name"),
// rs.getString("email"));
// }