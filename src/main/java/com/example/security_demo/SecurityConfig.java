package com.example.security_demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration //marks this class as a configuration class
@EnableWebSecurity // enables this class to work as a webSecurity
public class SecurityConfig {


    @Bean
    /*
     * This method returns a SecurityFilterChain. As noted before, this is a way for the
     * user to customize and implement a Filter that will intercept everysingle request
     * for the server. This will make so the app 
     */
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // this says that any request received by the server must be authenticated
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());

        // this makes the authentication STATELESS
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

}
