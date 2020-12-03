package com.ontotext.example.movienator.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ontotext.commons.web.filters.Headers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class NoRequestIdHeaderTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testHelloControllerWithoutRequestId() throws Exception {
    MvcResult result = mockMvc.perform(get("http://localhost:8080/hello-world"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello, SAS team!"))
        .andReturn();

    assertTrue(result.getResponse().getHeader(Headers.X_REQUEST_ID)
        .matches(
            "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-1"
                + "[0-9a-fA-F]{3}\\-[0-9a-fA-F]{4}\\-"
                + "[0-9a-fA-F]{12}")
    );
  }

}
