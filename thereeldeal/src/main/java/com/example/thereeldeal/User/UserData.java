package com.example.thereeldeal.User;


import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserData {

    @NotEmpty(message = "Nama tidak boleh kosong")
    private String nama;

    @NotEmpty(message = "NIK tidak boleh kosong")
    @Size(min = 16, max = 16, message = "NIK harus 16 karakter")
    private String nik;

    @NotEmpty(message = "Email tidak boleh kosong")
    private String email;

    

    @NotEmpty(message = "Kata sandi tidak boleh kosong")
    @Size(min = 6, max = 20, message = "Kata sandi harus 6-20 karakter")
    private String kata_sandi;
    
    private String jenis_kelamin;
    private String peran;

    @Transient
    private String confpassword;
}
