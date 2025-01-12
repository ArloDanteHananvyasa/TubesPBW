package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(UserData user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.registerUser(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerAdmin(UserData user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.registerAdmin(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserData login(String email, String password){
        if(userRepository.findByEmail(email).isPresent()){
            UserData user = userRepository.findByEmail(email).get();

            if(passwordEncoder.matches(password, user.getPassword())){
                return user;
            }
        }
        return null;
    }
}
