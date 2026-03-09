package com.omnet.data.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.omnet.data.dto.response.DTOResponseBAD_REQUEST;
import com.omnet.data.dto.response.DTOResponseOK;
import com.omnet.data.dto.response.DTOResponseUserLoginPostBody_DTO;
import com.omnet.data.dto.response.DTOResponseUserPostBody_DTO;

import java.util.UUID;

/**
 * This class implements factory class to obtain various types of responses.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
public class DTOFactory {
  
  /**
   * Default constructor.
   */
  private DTOFactory() {
    
  }
  
  /**
   * Method returns a response DTO with status OK.
   * 
   * @param message to be included in response.
   * @return response entity DTO.
   */
  static public ResponseEntity<DTO> getResponseOK(String message) {
    
    HttpStatus code = DTOResponseOK.code;
    
    DTOHeader header = new DTOHeader(UUID.randomUUID(), true, 
        code.value(), message);
    
    DTO dto = new DTOResponseOK(
        header, 
        new DTOBody());
    
    return new ResponseEntity<>(dto, code);
  }

  /**
   * Method returns a response DTO with status bas request.
   * 
   * @param message to be included in response.
   * @return response entity DTO.
   */
  static public ResponseEntity<DTO> getResponseBAD_REQUEST(String message) {
    
    HttpStatus code = DTOResponseBAD_REQUEST.code;

    DTOHeader header = new DTOHeader(UUID.randomUUID(), false,
        code.value(), message);
    
    DTO dto = new DTOResponseBAD_REQUEST(
        header, 
        new DTOBody());
    
    return new ResponseEntity<>(dto, code);
  }
  
  /**
   * Method returns a response DTO with status bad request.
   * 
   * @param message to be included in response.
   * @param email to be included in response.
   * @return response entity DTO.
   */
  static public ResponseEntity<DTO> getResponseUserPostOK(String message, String email) {
    
    HttpStatus code = DTOResponseOK.code;

    DTOHeader header = new DTOHeader(UUID.randomUUID(), false,
        code.value(), message);
    
    DTOBody body = new DTOResponseUserPostBody_DTO(email);
    
    DTO dto = new DTOResponseBAD_REQUEST(
        header, 
        body);
    
    return new ResponseEntity<>(dto, code);
  }

  /**
   * Method returns a response DTO with status bad request.
   * 
   * @param message to be included in response.
   * @param token to be included in response.
   * @return response entity DTO.
   */
  static public ResponseEntity<DTO> getResponseUserLoginPostOK(String message, String token) { 
    
    HttpStatus code = DTOResponseOK.code;

    DTOHeader header = new DTOHeader(UUID.randomUUID(), false,
        code.value(), message);
    
    DTOBody body = new DTOResponseUserLoginPostBody_DTO(token);
    
    DTO dto = new DTOResponseBAD_REQUEST(
        header, 
        body);
    
    return new ResponseEntity<>(dto, code);
  }

  /**
   * Method returns a response DTO with status OK and default message.
   * 
   * @return response entity DTO.
   */
  static public ResponseEntity<DTO> getResponseOK() {
    
    return getResponseOK("ok.");
  }

}
