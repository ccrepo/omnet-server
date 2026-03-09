package com.omnet.controller.session;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omnet.data.dto.DTO;
import com.omnet.data.dto.DTOFactory;
import com.omnet.repository.OmnetUserRepository;

/**
 * This class implements session controller for REST functionality.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@RestController
public class OmnetSessionRestController {

  @Autowired
  private OmnetUserRepository userRepository;
  
  /**
   * Default constructor.
   */
  public OmnetSessionRestController() {
  }

  /**
   * HTTP Get handler.
   * 
   * @return session data as represented by JSON
   */
  @GetMapping(path = "/api/session")
  public ResponseEntity<DTO> getSession() {

    if(!userRepository.existsById(UUID.randomUUID())) {
      
      return DTOFactory.getResponseOK("NOT exists");    
    }
    
    return DTOFactory.getResponseOK("exists");    
  }
}
