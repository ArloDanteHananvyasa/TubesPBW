package com.example.demo.admin.datas;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class billData {
    private double base_fee;
    private double late_fee;
    private double final_fee;
}
