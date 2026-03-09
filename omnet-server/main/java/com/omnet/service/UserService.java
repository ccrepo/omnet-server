package com.omnet.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.stereotype.Service;

import com.omnet.config.OktaAdminProperties;
import com.omnet.config.OktaUserProperties;
import com.omnet.data.dto.request.DTORequestUserLoginPost_DTO;
import com.omnet.data.dto.request.DTORequestUserPost_DTO;
import com.omnet.data.entity.OmnetUser;
import com.omnet.data.entity.OmnetVehicle;
import com.omnet.repository.OmnetUserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements a user service bean.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Slf4j
@Service
public class UserService {
  
  @Autowired
  private OmnetUserRepository omnetUserRepository;
   
  @Autowired
  private OktaService oktaService;
  
  @Autowired
  private OktaAdminProperties oktaAdminProperties;

  @Autowired
  private OktaUserProperties oktaUserProperties;

  /** 
   * Default constructor.
   */
  public UserService() {
    
  }
  
  /**
   * Method to create {@link OmnetUser}.

   * @param userInfo DTO containing user information.
   * @return Map.Entry containing boolean true indicating success, false otherwise with additional diagnostic information in String.
   */
  @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
  public Map.Entry<Boolean, Map.Entry<String, String>> createUser(DTORequestUserPost_DTO userInfo) {
        
    try {

      String username = userInfo.getBody().getUsername();
      
      String surname = userInfo.getBody().getSurname();

      String email = OmnetUser.generateEmail(username, surname, oktaAdminProperties.getOktaDomainName());
      
      log.info("Created email address: " + email);
      
      String name = username + " " + surname;
      
      OmnetUser user = new OmnetUser(
          username, 
          surname, 
          email,
          userInfo.getBody().getPassword(), 
          new  ArrayList<OmnetVehicle> ());

      Optional<OmnetUser> user0 = omnetUserRepository.findByUsernameAndSurname(username, surname);
      
      if (user0.isPresent()) {
        
        String diagnostic = ("User already exists: " + name);

        log.error(diagnostic);
        
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        
        return Map.entry(false, Map.entry(diagnostic, email));
      }

      log.info("Attempting to save user: {}", name);
      
      user = omnetUserRepository.save(user);
      
      if (user == null) {
        
        String diagnostic = "User save failed for: " + name;

        log.error(diagnostic);
        
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        
        return Map.entry(false, Map.entry(diagnostic, email));
      }
      
      Map.Entry<Boolean, String> result = oktaService.createUser(user);
      
      if (!result.getKey()) {
        
        String diagnostic = "Okta user create failed for user: " + name + " " + result.getValue();

        log.error(diagnostic);
        
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        
        return Map.entry(false, Map.entry(diagnostic, email));
      }

      String diagnostic = "User created: " + name + " id " + result.getValue();
      
      log.info(diagnostic);
      
      return Map.entry(true, Map.entry(diagnostic, email));

    } catch (Exception e) {

      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

      log.error(e.getMessage(), e);
      
      return Map.entry(false, Map.entry(e.getMessage(), ""));
    }    
  }
  
  /**
   * Method to login {@link OmnetUser} and obtain a session token.

   * @param userInfo DTO containing user information.
   * @return Map.Entry containing boolean true indicating success, false otherwise with additional diagnostic information in String.
   */
  @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
  public Map.Entry<Boolean, String> loginUserSession(DTORequestUserLoginPost_DTO userInfo) {
   
    String username = userInfo.getBody().getUsername();
    
    String surname = userInfo.getBody().getSurname();

    String email = OmnetUser.generateEmail(username, surname, oktaAdminProperties.getOktaDomainName());
    
    log.info("Created email address: " + email);
    
    String name = username + " " + surname;
    
    Optional<OmnetUser> user0 = omnetUserRepository.findByEmail(email);
    
    if (!user0.isPresent()) {
      
      String diagnostic = ("User does not exist: " + name);

      log.error(diagnostic);
      
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      
      return Map.entry(false, diagnostic);
    }
    
    OmnetUser user = user0.get();
    
    Map.Entry<Boolean, String> result = oktaService.loginUserSession(user);

    if (!result.getKey()) {

      String diagnostic = "Okta user session login failed for user: " + name;

      log.error(diagnostic);

      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

      return Map.entry(false, diagnostic);
    }
        
    return Map.entry(true, result.getValue());
  }
  
  /**
   * Method to login {@link OmnetUser} and obtain an access token.

   * @param userInfo DTO containing user information.
   * @return Map.Entry containing boolean true indicating success, false otherwise with additional diagnostic information in String.
   */
  @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
  public Map.Entry<Boolean, String> loginUserAccess(DTORequestUserLoginPost_DTO userInfo) {
   
    String username = userInfo.getBody().getUsername();
    
    String surname = userInfo.getBody().getSurname();

    String email = OmnetUser.generateEmail(username, surname, oktaUserProperties.getOktaDomainName());
    
    log.info("Created email address: " + email);
    
    String name = username + " " + surname;
    
    Optional<OmnetUser> user0 = omnetUserRepository.findByEmail(email);
    
    if (!user0.isPresent()) {
      
      String diagnostic = ("User does not exist: " + name);

      log.error(diagnostic);
      
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      
      return Map.entry(false, diagnostic);
    }
    
    OmnetUser user = user0.get();
    
    Map.Entry<Boolean, String> result = oktaService.loginUserAccess(user);

    if (!result.getKey()) {

      String diagnostic = "Okta user access login failed for user: " + name;

      log.error(diagnostic);

      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

      return Map.entry(false, diagnostic);
    }
        
    return Map.entry(true, result.getValue());
  }
}
