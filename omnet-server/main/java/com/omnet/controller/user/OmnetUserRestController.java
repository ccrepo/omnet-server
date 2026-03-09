package com.omnet.controller.user;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.omnet.data.dto.DTO;
import com.omnet.data.dto.DTOFactory;
import com.omnet.data.dto.request.DTORequestUserPost_DTO;
import com.omnet.data.dto.request.DTORequestUserLoginPost_DTO;
import com.omnet.service.UserService;

import jakarta.validation.Valid;

/**
 * This class implements user controller for REST functionality.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@RestController
public class OmnetUserRestController {

  @Autowired
  private UserService userService;
    
  /**
   * Default constructor.
   */
  public OmnetUserRestController() {
  }

  /**
   * HTTP Get handler.
   * 
   * @return user data if it exists.
   */
  @GetMapping(path = "/api/user")
  public ResponseEntity<DTO> getUser() {

    return DTOFactory.getResponseOK("NYI");
  }
  
  /**
   * HTTP Post handler.
   * 
   * @param request request object.
   * @param bindingResult binding object for incoming values.
   * @return message response for client.
   */
  @PostMapping(path = "/api/user")
  public ResponseEntity<DTO> postUser(@Valid @RequestBody DTORequestUserPost_DTO request,
      BindingResult bindingResult) {
    
    if (bindingResult.hasErrors()) {

      String message = bindingResult.getAllErrors()
          .stream()
          .map(ObjectError::getDefaultMessage)
          .collect(Collectors.joining(" "));

      return DTOFactory.getResponseBAD_REQUEST(message);      
    }
    
    Map.Entry<Boolean, Map.Entry<String, String>> result = userService.createUser(request);
    
    String diagnostic = result.getValue().getKey();

    String email = result.getValue().getValue();

    if (result.getKey()) {
      
      return DTOFactory.getResponseUserPostOK(diagnostic, email);
    }
    
    return DTOFactory.getResponseBAD_REQUEST(diagnostic);
  }

  /**
   * HTTP Post handler for login validation and returning a session token.
   * 
   * @param request request object.
   * @param bindingResult binding object for incoming values.
   * @return message response for client.
   */
  @PostMapping(path = "/api/user/session")
  public ResponseEntity<DTO> logintUser(@Valid @RequestBody DTORequestUserLoginPost_DTO request,
      BindingResult bindingResult) {
    
    if (bindingResult.hasErrors()) {

      String message = bindingResult.getAllErrors()
          .stream()
          .map(ObjectError::getDefaultMessage)
          .collect(Collectors.joining(" "));

      return DTOFactory.getResponseBAD_REQUEST(message);      
    }
    
    Map.Entry<Boolean, String> result = userService.loginUserSession(request);

    String diagnostic = result.getValue();
    
    if (result.getKey()) {
      
      String sessionToken = result.getValue();

      return DTOFactory.getResponseUserLoginPostOK(diagnostic, sessionToken);
    }

    return DTOFactory.getResponseBAD_REQUEST(diagnostic);
  }
  
  /**
   * HTTP Post handler for login validation and returning an access token.
   * 
   * @param request request object.
   * @param bindingResult binding object for incoming values.
   * @return message response for client.
   */
  @PostMapping(path = "/api/user/access")
  public ResponseEntity<DTO> logintUserAccess(@Valid @RequestBody DTORequestUserLoginPost_DTO request,
      BindingResult bindingResult) {
    
    if (bindingResult.hasErrors()) {

      String message = bindingResult.getAllErrors()
          .stream()
          .map(ObjectError::getDefaultMessage)
          .collect(Collectors.joining(" "));

      return DTOFactory.getResponseBAD_REQUEST(message);      
    }
    
    Map.Entry<Boolean, String> result = userService.loginUserAccess(request);

    String diagnostic = result.getValue();
    
    if (result.getKey()) {
      
      String accesssToken = result.getValue();

      return DTOFactory.getResponseUserLoginPostOK(diagnostic, accesssToken); 
    }

    return DTOFactory.getResponseBAD_REQUEST(diagnostic);
  }
  
  /**
   * Method handler for DataViolationException from repository action.
   * @param ex exception.
   * @return message response for client.
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<DTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

    return DTOFactory.getResponseBAD_REQUEST("Could not load parameters: " + ex.getClass().getName());
  }
}
