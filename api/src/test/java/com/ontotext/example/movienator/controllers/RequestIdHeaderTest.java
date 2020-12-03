package com.ontotext.example.movienator.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ontotext.commons.web.filters.Headers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class RequestIdHeaderTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testHelloControllerWithRequestId() throws Exception {
    mockMvc.perform(get("http://localhost:8080/hello-world")
        .header(Headers.X_REQUEST_ID, "1234"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello, SAS team!"))
        .andExpect(header().string(Headers.X_REQUEST_ID, "1234"));
  }

}
