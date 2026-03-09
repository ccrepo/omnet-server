package com.omnet.controller.session;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;

/**
 * This class implements session controller for web MVC functionality.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@Controller
public class OmnetSessionWebController {

  /**
   * Default constructor.
   */
  public OmnetSessionWebController() {
  }
  
  /**
   * HTTP Get handler.
   * 
   * @return view name for next MVC state,
   */
  @GetMapping(path = "/web/session")
  public String getSession() {
    
    return "session";   
  }
}
