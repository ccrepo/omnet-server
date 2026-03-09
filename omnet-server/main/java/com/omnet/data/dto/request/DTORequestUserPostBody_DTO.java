package com.omnet.data.dto.request;

import com.omnet.data.dto.DTOBody;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class DTORequestUserPostBody_DTO extends DTOBody {

  /**
   * Constructor.
   *
   * @param username user name.
   * @param surname user surname.
   * @param password user password.
   */
  public DTORequestUserPostBody_DTO(String username, String surname, String password) {

    this.username = username;

    this.surname = surname;

    this.password = password;
  }
  
  /**
   * Message user name.
   */
  @Size(max = 25)
  @NotNull(message = "Username cannot be null.")
  private String username;

  /**
   * Message user surname.
   */
  @Size(max = 25)
  @NotNull(message = "Surname cannot be null.")
  private String surname;

  /**
   * Message password.
   */
  @Size(max = 40)
  @NotNull(message = "Password cannot be null.")
  private String password;
}

