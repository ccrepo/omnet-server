package com.omnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO 
// 1. application.properties move secrets into docker build
// 2. Limit total number of users locally and on OKTA to 100.
// 3. Add to doc OKTA setup: ADMIN, USER, API, native, password only. 
// 4. Remove data from persistence that is not required or redundant. 
// 

/**
 * This class implements server boot startup.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
@SpringBootApplication
public class OmnetApplicationServer {

  /**
   * Default constructor.
   */
  public OmnetApplicationServer() {
  }

  /**
   * Main method.
   * 
   * @param args program arguments.
   */
	public static void main(String[] args) {
	
	  SpringApplication.run(OmnetApplicationServer.class, args);
	}
}
