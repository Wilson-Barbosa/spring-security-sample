package com.example.security_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;


@Configuration //marks this class as a configuration class
@EnableWebSecurity // enables this class to work as a webSecurity
@EnableMethodSecurity // used for the @PreAuthorize annotation inside the controllers
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    /*
     * This method returns a SecurityFilterChain. As noted before, this is a way for the
     * user to customize and implement a Filter that will intercept everysingle request
     * for the server. This will make so the app 
     */
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    
        /**
         * Using the builder pattern I can build a SecurityFilter and apply multiple rulings
         */ 
        http.authorizeHttpRequests((requests) -> requests
                                                .requestMatchers("/h2-console/**").permitAll() // from the url I can allow certain paths
                                                .anyRequest().authenticated()
        );

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // allows the h2-console frames to be displayed
        http.csrf(csrf -> csrf.disable()); // removes the authentication form for h2 access

        // this makes the authentication STATELESS
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // http.formLogin(withDefaults());
        http.httpBasic(withDefaults());

        return http.build();
    }


    /**
     * H2 database was added and therefore it can be used to store and retrive user credentials.
     * A schema.sql will be executed and these users will be added to the tables... I'm using this
     * application runner because the schema was being generated AFTER the attemp to persist the data
     */
    @Bean
    ApplicationRunner initUsers(){
        return args -> {



            // These objects are created using the builder pattern.
            UserDetails user1 = User.withUsername("wilson")
                                    .password(passwordEncoder().encode("pass123"))
                                    .roles("USER").build();
            UserDetails user2 = User.withUsername("rafael")
                                    .password(passwordEncoder().encode("pass123"))
                                    .roles("USER").build();
            UserDetails admin = User.withUsername("julia")
                                    .password(passwordEncoder().encode("adminpassword"))
                                    .roles("ADMIN").build();

            /**
             * It's important to note that JdbcUserDetailsManager's createUser() method actually 
             * persists the credentials into a database, in my case the h2, meaning it will try
             * to execute sql statements upon its instantiantion. See the implementation
             * on JdbcUserDetailsManager decompiled class for more info.
             */
            JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
            jdbcUserDetailsManager.createUser(user1);
            jdbcUserDetailsManager.createUser(user2);
            jdbcUserDetailsManager.createUser(admin);
        };

    }


    /**
     * Returns a BCrypt implementation of a passwordEnconder. Use enconde() to hash the password 
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(BCryptVersion.$2Y);
    }
    

}
