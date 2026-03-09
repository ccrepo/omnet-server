package com.omnet.data.dto.response;

import org.springframework.http.HttpStatus;

import com.omnet.data.dto.DTO;
import com.omnet.data.dto.DTOBody;
import com.omnet.data.dto.DTOHeader;

/**
 * This class implements standard OK DTO response message.
 * 
 * @author cc
 * @version %I%, %G%
 * @since 0.1
 */
public class DTOResponseOK extends DTO {

  /**
   * HTTP status code for this DTO type.
   */
  public static HttpStatus code = HttpStatus.OK;
  
  /**
   * Constructor.
   *
   * @param header DTOheader object  for use to construct superclass.
   * @param body DTOBody object  for use to construct superclass.
   */
  public DTOResponseOK(DTOHeader header, DTOBody body) {

    super(header, body);
  }  
}

