package com.example.security_demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class GreetingController {
    
    @GetMapping("greeting")
    public String getMethodName() {
        return new String("Hello from spring");
    }
    

}
