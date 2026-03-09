package com.omnet.data.dto.response;

import com.omnet.data.dto.DTOBody;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * This class implements a user POST DTO response message.
 * This message is used to confirm an open account.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Getter
@Setter
public class DTOResponseUserPostBody_DTO extends DTOBody {

  /**
   * Constructor.
   *
   * @param email user allocated email.
   */
  public DTOResponseUserPostBody_DTO(String email) {

    this.email = email;
  }
  
  /**
   * Message user email.
   */
  @Size(min = 1, max = 255)
  @NotNull(message = "Email cannot be null.")
  private String email;
}

