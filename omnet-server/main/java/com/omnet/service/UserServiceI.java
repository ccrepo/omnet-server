package com.omnet.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.omnet.data.entity.OmnetUser;

/**
 * This interface defines a user services interface.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
public interface UserServiceI {

  /**
   * Method to find a user using a provided user name.
   * @param username key to search for user.
   * @return OmnetUser is found else throws an exception.
   * @throws UsernameNotFoundException if user not found.
   */
  OmnetUser getUserByName(String username) throws UsernameNotFoundException;
}
