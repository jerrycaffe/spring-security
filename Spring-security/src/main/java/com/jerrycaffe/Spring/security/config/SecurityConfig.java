package com.jerrycaffe.Spring.security.config;

import com.jerrycaffe.Spring.security.config.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
//        Observe most methods in here takes the object of customizer which is part of security configuration package
//The customizer itself is a functional interface i.e refer to the functional programming of java to understand functional interface

        Customizer<CsrfConfigurer<HttpSecurity>> custCsrf = new Customizer<CsrfConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CsrfConfigurer<HttpSecurity> customizer) {
                customizer.disable();
            }
        };

        //This same thing is used for other methods
        http.csrf(custCsrf) //This tells spring security to disable the login form which means anyone can access the site
                .authorizeHttpRequests(request -> request
                        .requestMatchers("register", "login").permitAll()
                        .anyRequest().authenticated())//this ensures all request coming to the application should not go through if not authenticated
               // .formLogin(Customizer.withDefaults()) //this will allow user to login via a form on the web but in REST client (postman) it returns a form response
                .httpBasic(Customizer.withDefaults()) //This has to be implemented to allow the auth to be passed in from REST endpoint
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //when this is done, everytime you login on the browser, it gives you the same form login again because it does not remember any session
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //Tells spring security to ensure jwtFilter is checked before user name and password filter
        return http.build();
    }

    //To persist data into the db during authentication we need the AuthenticationProvider
    //The AuthenticationProvider is an interface so we need an implementation which is a DaoAuthenticationProvider
    //The DaoAuthProvider is a class which extends the AbstractAuthenticationProvider class and this class implements the AuthenticationProvider interface

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailService);


        return provider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
        return config.getAuthenticationManager();
    }
}
