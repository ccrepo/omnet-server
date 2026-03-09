package com.omnet.controller.session;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class OmnetAccessRestControllerTest {

  @Autowired
  private MockMvc mock;
  
  @Test
  @WithMockUser
  void testOmnetAccessRestControllerTest() throws Exception {
    
    mock.perform(get("/api/user"))
    .andExpect(status().isOk())
    .andExpect(content().string(containsString("{"))); // TODO - expand test
  }
}
