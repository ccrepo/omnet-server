package com.omnet.common;

/**
 * This class implements number related helper methods.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
abstract public class Numbers {

  /**
   * Default constructor.
   */
  public Numbers() {  
  }
  
  /**
   * Constant default hash code value for Entity and Data objects as placeholder before 
   * internal elements are set, to enable hash code calculation. NB> hash codes cannot change
   * once objects have been placed in hash based collections.
   */
  public static final int _CONSTANT_NUMBER_DEFAULT_HASHCODE = 31;
  

  /**
   * Constant max email length for OKTA email addresses.
   */
  public static final int _MAX_OKTA_EMAIL_LENGTH = 64;
}
