package com.omnet.config;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements an OKTA properties values base class service bean.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Slf4j
abstract public class OktaProperties {

  /**
   * Constructor.
   */
  public OktaProperties() {

  }
  
  /**
   * Method returns OKTA is active flag.
   * 
   * @param propertyName property name being checked.
   * @param propertyValue property value being checked.
   * @return boolean indicating whether OKTA layer should be active or not.
   */
  protected boolean getOktaIsActive(String propertyName, String propertyValue) {
    
    if (propertyValue.equals("n") || propertyValue.equals("no") || 
        propertyValue.equals("false") || propertyValue.equals("inactive") || 
        propertyValue.equals("")) {

      return false;
    }

    if (propertyValue.equals("y") || propertyValue.equals("yes") || 
        propertyValue.equals("true") || propertyValue.equals("active")) {

      return true;
    }

    log.error("Property setting invalid for: " + propertyName);
    
    throw new IllegalStateException("Property '" + propertyName + "' please set to 'y' or 'n'");
  }
  
  /**
   * Method which checks whether all String parameters have been set.
   *
   * @param settings name and value pairs to be checked.
   * @throws IllegalStateException if invalid parameter / value found.
   */
  protected void validate(List<Map.Entry<String, String>> settings) {
    
    for (Map.Entry<String, String> setting : settings) {
      
      if (setting.getValue() == null || 
          setting.getValue().isBlank()) {
      
        log.error("Property setting invalid for: " + setting.getKey());
        
        throw new IllegalStateException("Property '" + setting.getKey() + "' must be set");
      }
    } 
  }
}
