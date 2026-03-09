package com.omnet.data.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * This class implements our base DTO object which should act as a 
 * basis for all client to server communication.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Getter
@Setter
abstract public class DTO {
 
  /**
   * DTO head data
   */
  @NotNull
  private DTOHeader header;
  
  /**
   * DTO body data
   */
  @NotNull
  private DTOBody body;

  /**
   * Default constructor.
   */
  public DTO() {
    
  }
  
  /**1
   * Constructor.
   *
   * @param header message header {@link DTOHeader} object.
   * @param body message body {@link DTOBody} object.
   */
  protected DTO(DTOHeader header, DTOBody body) {

    this.header = header;
    
    this.body = body;
  }  
}
