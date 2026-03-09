package com.omnet.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.omnet.data.dto.DTO;
import com.omnet.data.dto.DTOFactory;

/**
 * This class implements bad controller arguments error handler.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@ControllerAdvice 
public class HandlerConfiguration extends ResponseEntityExceptionHandler {
  
  /**
   * Default constructor.
   */
  public HandlerConfiguration() {
    
  }
   
  /**
   * Method to override return message to client when controller arguments are invalid.
   * 
   * @param ex the exception to handle
   * @param headers the headers to be written to the response
   * @param status the selected response status
   * @param request the current request
   * @return a {@link ResponseEntity} for the response
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

      ResponseEntity<DTO> response = DTOFactory.getResponseBAD_REQUEST(
          "Malformed JSON request: " + ex.getMostSpecificCause().getMessage());
      
      return new ResponseEntity<Object>(response.getBody(), HttpStatus.BAD_REQUEST);
  }
}
