package com.omnet.controller.session;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(controllers = OmnetSessionWebController.class,
excludeAutoConfiguration = { 
    org.springframework.boot.security.oauth2.client.autoconfigure.servlet.OAuth2ClientWebSecurityAutoConfiguration.class,
    org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration.class,
    org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration.class
})
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class OmnetSessionWebControllerTest {

  @Autowired
  private MockMvc mock;
  
  @Test
  public void testOmnetSessionWebControllerTest() throws Exception {

    mock.perform(get("/web/session"))
    .andExpect(status().isOk())
    .andExpect(view().name("session"))
    .andExpect(content().string(containsString("Welcome")))
    ;
  }
}
