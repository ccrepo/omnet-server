package com.omnet.data.dto.response;

import com.omnet.data.dto.DTOBody;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * This class implements a user POST DTO response message.
 * This message is used to confirm a login of an account.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Getter
@Setter
public class DTOResponseUserLoginPostBody_DTO extends DTOBody {

  /**
   * Constructor.
   * 
   * @param token user allocated email.
   */
  public DTOResponseUserLoginPostBody_DTO(String token) {

    this.token = token;
  }
  
  /**
   * Message user token.
   */
  @Size(min = 1, max = 255)
  @NotNull(message = "Token cannot be null.")
  private String token;
}

