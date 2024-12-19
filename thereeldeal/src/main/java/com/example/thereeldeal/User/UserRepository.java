package com.example.thereeldeal.User;

import java.util.Optional;

public interface UserRepository {
    void saveUser(UserData userData);
    //Optional<UserData> findByNik(String nik);
    Optional<UserData> findByEmailOrUsername(String email);
}
