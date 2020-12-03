package com.ontotext.example.movienator.config;

import com.ontotext.example.movienator.healthcheck.MovieGraphDbHealthCheck;
import com.ontotext.healthcheck.model.DetailedHealthCheckResponse;
import com.ontotext.healthcheck.model.DetailedHealthCheckResponse.Status;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MovieGraphDbHealthCheckTestConfiguration {

  @Primary
  @Bean
  public MovieGraphDbHealthCheck getDetailedResponse() {
    return new TestMovieGraphDbHealthCheck();
  }

  public class TestMovieGraphDbHealthCheck extends MovieGraphDbHealthCheck {

    @Override
    public DetailedHealthCheckResponse getDetailedResponse(String requestId) {

      DetailedHealthCheckResponse detailedHealthCheckResponse = new DetailedHealthCheckResponse();
      detailedHealthCheckResponse.setStatus(Status.OK);
      return detailedHealthCheckResponse;
    }
  }
}
