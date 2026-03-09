package com.omnet.common;

import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.Instant;
import java.time.ZoneId;

/**
 * This class implements time related helper methods.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
public class Times {

  /**
   * Default constructor.
   */
  public Times() {  
  }

  /**
   * Method which returns the current time in UTC zone.
   * 
   * @return {@link ZonedDateTime} set to time now in TUC.
   **/
  public static ZonedDateTime getNowUTC() {

    return ZonedDateTime.now(ZoneOffset.UTC);
  }

  /**
   * Method which returns the epoch time in UTC zone.
   * 
   * @return {@link ZonedDateTime} set to epoch time in TUC.
   **/
  public static ZonedDateTime getEpochUTC() {

    return ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"));
  }

}
