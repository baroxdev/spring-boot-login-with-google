package com.example.demosecurity;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements SecurityFilterChain {
  private SecurityFilterChain delegate; // create a field to store the delegate object

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
    .csrf()
    .disable()
    .authorizeHttpRequests()
    .requestMatchers("/api/auth/login").permitAll()
    .anyRequest()
    .authenticated();
    
    delegate = http.build();
    return delegate; 
}

  @Override
  public List<Filter> getFilters() {
    return delegate.getFilters(); // delegate the method to the delegate object
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    return delegate.matches(request); // delegate the method to the delegate object
  }
}
