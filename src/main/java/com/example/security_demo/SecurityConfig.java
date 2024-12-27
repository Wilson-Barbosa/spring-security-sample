package com.example.security_demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

        // http.formLogin(withDefaults());
        http.httpBasic(withDefaults());

        return http.build();
    }


    @Bean
    /**
     * Since the app still doesn't have a database the authentication can be done using an
     * in-memory session, which means the application will store the information while the 
     * server is running. This is not an ideal solution, but it's done in the tutorial, so 
     * I'm copying it here. Soon this process will change to retrive a user from a database
     */
    public UserDetailsService userDetailsService(){

        // These credentials are in-memory
        UserDetails user1 = User.withUsername("wilson").password("{noop}12").roles("USER").build();
        UserDetails user2 = User.withUsername("rafael").password("{noop}123").roles("USER").build();
        UserDetails admin = User.withUsername("julia").password("{noop}1234").roles("ADMIN").build();

        // Then I can add them here and try to access a secured endpoint
        return new InMemoryUserDetailsManager(user1, user2, admin);
    }

}
