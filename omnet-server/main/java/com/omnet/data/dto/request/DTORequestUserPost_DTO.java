package com.omnet.data.dto.request;

import com.omnet.data.dto.DTOBody;
import com.omnet.data.dto.DTOHeader;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * This class implements a user POST DTO request message.
 * This message is used to open an account.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Getter
@Setter
public class DTORequestUserPost_DTO extends DTOBody {

  /**
   * Message header for DTO type.
   */
  @Valid
  @NotNull(message = "Header cannot be null.")
  private DTOHeader header;
  
  /**
   * Message body for DTO type.
   */
  @Valid
  @NotNull(message = "Body cannot be null.")
  private DTORequestUserPostBody_DTO body;
  
  /**
   * Constructor.
   *
   * @param header header element.
   * @param body body element.
   */
  public DTORequestUserPost_DTO(DTOHeader header, DTORequestUserPostBody_DTO body) {

    this.header = header;

    this.body = body;
  }
  
}

