package com.example.security_demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class GreetingController {
    
    @GetMapping("greeting")
    public String getMethodName() {
        return new String("Hello from spring");
    }

    /**
     * The @PreAuthorize annotation indicates to Spring Security that only poeple with the
     * right roles can access this endpoint specifically.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("user")
    public String userGreeting() {
        return new String("Hello to user");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin")
    public String adminGreeting() {
        return new String("Hello to admin");
    }
    

}
