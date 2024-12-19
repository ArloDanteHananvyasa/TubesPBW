package com.example.thereeldeal;

import java.util.Arrays;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AuthorizationAspect {

    @Autowired
    private HttpSession session;

    @Before("@annotation(requiredRole)")
    public void checkAuthorization(RequiredRole requiredRole) {
        String[] roles = requiredRole.value();
        String role = (String) session.getAttribute("role");

        if (Arrays.asList(roles).contains("*")) {
            return;
        }

        if (session.getAttribute("user") == null) {
            throw new RuntimeException("Pengguna belum login!");
        }
        
        if (!Arrays.asList(roles).contains(role)) {
            throw new SecurityException("Authorized only for : " + Arrays.toString(roles));
        }
    }
}
