package com.example.demo.admin.datas;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class reportData {
    private String customerName;
    private String orderDate;
    private String total;
    private String method_name;
}
