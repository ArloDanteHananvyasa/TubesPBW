package com.example.demo.user;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserData {

    @NotEmpty(message = "Phone is required")
    @Size(min = 1, max = 20, message = "Maximum 20 Characters long")
    private String phone;

    @NotEmpty(message = "Username is required")
    @Size(min = 1, max = 50, message = "Maximum 50 Characters long")
    private String username;

    @NotEmpty(message = "Name is required")
    @Size(min = 1, max = 50, message = "Maximum 50 Characters long")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(min = 1, max = 50, message = "Maximum 50 Characters long")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String role;
    private String deleted;

    @Transient
    @NotEmpty(message = "Confirm Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String confpassword;
}
