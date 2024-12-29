package com.example.demo.user;

import java.util.Optional;

public interface UserRepository {
    void registerUser(UserData userData);
    Optional<UserData> findByEmail(String email);
}
