package com.omnet.data.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements our base DTO header.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Slf4j
@Getter
@Setter
public class DTOHeader {

  /**
   * Default constructor.
   */
  public DTOHeader() {
  }

  /**
   * Constructor.
   *
   * @param id DTO message id.
   * @param status boolean indicating validity or otherwise action in message.
   * @param code HTTP code for action in message,
   * @param message additional information.
   */
  public DTOHeader(UUID id, Boolean status, Integer code, String message) {

    this.id = id;
        
    this.status = status;
    
    this.code = code;

    this.message = message;

    this.originator = "server";
  }

  /**
   * Message id
   */
  @NotNull(message = "Id cannot be null.")
  private UUID id;

  /**
   * Message status.
   */
  private Boolean status;

  /**
   * Message code.
   */
  private Integer code;

  /**
   * Message message
   */
  @Size(max = 255)
  @NotNull(message = "Message cannot be null.")
  private String message;

  /**
   * Message originator type.
   */
  @NotNull(message = "Originator cannot be null.")
  @NotEmpty(message = "Originator cannot be empty.")
  @Pattern(regexp = "client|server", message = "originator must be 'client' or 'server'")
  private String originator;
}
