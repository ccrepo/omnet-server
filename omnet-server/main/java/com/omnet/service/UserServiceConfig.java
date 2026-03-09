package com.omnet.service;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.omnet.data.entity.OmnetUser;
import com.omnet.repository.OmnetUserRepository;

/**
 * This class implements a configuration object for user services.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Slf4j
@Configuration
public class UserServiceConfig {
    
  /**
   * Default constructor.
   */
  public UserServiceConfig() {    
  }
  
  /**
   * Method which returns a lambda that searches for a user using 
   * the supplied repository and user name.
   * 
   * @param omnetUserRepository repository class used to access user data.
   * 
   * @return {@link OmnetUser} object if found.
   */
  @Bean
  public UserServiceI userServiceUserLookup(OmnetUserRepository omnetUserRepository) {

    return username -> {

      try {
        Optional<OmnetUser> user = omnetUserRepository.findByUsername(username);

        if (user.isPresent()) {

          return user.get();
        }
      
      } catch (Exception e) {

        log.error(e.getMessage(), e);
      }
      
      throw new UsernameNotFoundException("Username not found '" + username + "'");
    };
  }
}


