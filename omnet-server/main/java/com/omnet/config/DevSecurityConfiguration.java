package com.omnet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class implements development configuration class.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Configuration 
@Profile("dev")
public class DevSecurityConfiguration {

  /**
   * Default constructor.
   */
  DevSecurityConfiguration() {  
  }
  
  /**
   * Method to remove security for API calls.
   * 
   * @param http HTTP call object
   * @return http filter to for removing security
   * @throws Exception if error
   */
  @Bean
  @Order(1)
  public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
    
    http.securityMatcher("/api/**")
    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
    .csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

  /**
   * Method to remove security for Web calls.
   * 
   * @param http HTTP call object
   * @return http filter to for removing security
   * @throws Exception if error
   */
  @Bean
  @Order(2)
  public SecurityFilterChain filterChainForWebMvc(HttpSecurity http) throws Exception {
    
    http.securityMatcher("/web/**")
    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
      
    return http.build();
  }
}

