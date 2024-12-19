package com.example.thereeldeal.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(UserData user) {
        try {
            //password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.saveUser(user);
            return true;
        } catch (Exception e) {
            System.err.println("Error when saving user: " + e.getMessage());
            return false;
        }
    }

    public UserData login(String emailUsername, String password) {
        
        if(userRepository.findByEmailOrUsername(emailUsername).isPresent()){
            UserData user = userRepository.findByEmailOrUsername(emailUsername).get();

            if(passwordEncoder.matches(password, user.getPassword())){
                return user;
            }
        }
        return null;
    }
}

