package com.omnet.config;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.omnet.service.UserServiceI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * This class implements security configuration class.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Configuration 
public class SecurityConfiguration {

  /**
   * Default constructor.
   */
  SecurityConfiguration() {
  }
  
  /**
   * Method returns a BCrypt password encoder.
   * 
   * @return {@link BCryptPasswordEncoder} password encoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    
    return new BCryptPasswordEncoder();
  }
  
  /**
   * Method to register UserService as the {@link UserDetailsService}.
   *
   * @param userService use service object to be used,
   * 
   * @return {@link UserDetailsService} which can be used for user lookup.
   */
  @Bean
  public UserDetailsService userDetailsService(UserServiceI userService) {
   
    return username -> userService.getUserByName(username);
  }
  
  /**
   * Method to remove security for API calls for non-dev case for stated URL and POST only.
   * 
   * @param http HTTP call object
   * @return http filter to for removing security
   * @throws Exception if error
   */
  @Bean
  @Order(1)
  public SecurityFilterChain apiUserSecurityPOST(HttpSecurity http) throws Exception {
        
    http.securityMatcher(request -> "POST".equals(request.getMethod()) && "/api/user".equals(request.getServletPath()))
    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
    .csrf(AbstractHttpConfigurer::disable);
 
    return http.build();
  }

  /**
   * Method to remove security for API calls for non-dev case for stated URL and POST and GET only.
   * 
   * @param http HTTP call object
   * @return http filter to for removing security
   * @throws Exception if error
   */
  @Bean
  @Order(2)
  public SecurityFilterChain apiUserSecurityAccessPOST(HttpSecurity http) throws Exception {
    
    http.securityMatcher("/api/user/access")
    .authorizeHttpRequests(auth -> 
        auth.requestMatchers(request -> "POST".equals(request.getMethod())).permitAll()
        .anyRequest().authenticated())
    .csrf(AbstractHttpConfigurer::disable);
 
    return http.build();
  }
  
  /**
   * Method to remove security for API calls for non-dev case for stated URL and POST only.
   * 
   * @param http HTTP call object
   * @return http filter to for removing security
   * @throws Exception if error
   */
  @Bean
  @Order(3)
  public SecurityFilterChain apiUserSecuritySessionPOST(HttpSecurity http) throws Exception {
    
    http.securityMatcher("/api/user/session")
    .authorizeHttpRequests(auth -> 
        auth.requestMatchers(request -> "POST".equals(request.getMethod())).permitAll()
            .anyRequest().authenticated())
    .csrf(AbstractHttpConfigurer::disable);
 
    return http.build();
  }
  
  /**
   * Method to alter security for API calls for non-dev case for automatic OKTA validation.
   * 
   * @param http HTTP call object
   * @return http filter to for removing security
   * @throws Exception if error
   */
  @Bean
  @Order(4)
  public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {

      http
          .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
          .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
          .csrf(AbstractHttpConfigurer::disable)
          .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

      return http.build();
  }  
}

