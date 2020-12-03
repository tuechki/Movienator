package com.ontotext.example.movienator.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontotext.commons.web.filters.Headers;
import com.ontotext.example.movienator.BaseMovieTest;
import com.ontotext.example.movienator.dtos.MovieWithPlotDto;
import com.ontotext.example.movienator.exceptions.MovieDoesNotExistException;
import com.ontotext.example.movienator.services.OmdbService;
import com.ontotext.example.movienator.services.OmdbServiceTest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class MovieControllerTest extends BaseMovieTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  protected RestTemplate omdbRestTemplate;

  private MockRestServiceServer mockOmdbServer;

  @PostConstruct
  private void postConstruct() {
    mockOmdbServer = MockRestServiceServer.createServer(omdbRestTemplate);
  }

  @Test
  public void testMovieEndpoint() {
    ResponseEntity<MovieDoesNotExistException> response = restTemplate.getForEntity(
        "/movies/10000",
        MovieDoesNotExistException.class
    );

    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    HttpHeaders headers = response.getHeaders();
    assertTrue(headers.get(Headers.X_REQUEST_ID).size() == 1);
    assertFalse(headers.get(Headers.X_REQUEST_ID).iterator().next().isEmpty());
  }

  @Test
  public void testLimitLessThan0() {
    ResponseEntity<String> response = restTemplate.getForEntity(
        "/movies?limit=-5",
        String.class
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(
        response.getBody().contains("\"message\":\"limit must be greater than or equal to 1\""));
  }

  @Test
  public void testLimitGreaterThan100() {
    ResponseEntity<String> response = restTemplate.getForEntity(
        "/movies?limit=103",
        String.class
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(
        response.getBody().contains("\"message\":\"limit must be less than or equal to 100\""));
  }

  @Test
  public void testOffsetLessThan0() {
    ResponseEntity<String> response = restTemplate.getForEntity(
        "/movies?offset=-5",
        String.class
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(
        response.getBody().contains("\"message\":\"offset must be greater than or equal to 0\""));
  }

  @Test
  public void testOffsetLessThan0AndLimitLessThan0() {
    ResponseEntity<String> response = restTemplate.getForEntity(
        "/movies?limit=-5&offset=-5",
        String.class
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains(
        "\"message\":\"offset must be greater than or equal to 0; "
            + "limit must be greater than or equal to 1\""));
  }

  @Test
  public void testOffsetLessThan0AndLimitGreaterThan100() {
    ResponseEntity<String> response = restTemplate.getForEntity(
        "/movies?limit=103&offset=-5",
        String.class
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains(
        "\"message\":\"limit must be less than or equal to 100; "
            + "offset must be greater than or equal to 0\""));
  }

  @Test
  public void testOmdbApi5xxThrows502() throws URISyntaxException {
    for (HttpStatus httpStatus : HttpStatus.values()) {
      if (httpStatus.is5xxServerError()) {
        mockOmdbServer
            .expect(requestTo(
                new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
            )).andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(httpStatus).body("OmdbApiServerErrorException"));

        ResponseEntity<MovieWithPlotDto> response = restTemplate
            .exchange("/movies/22/plot", HttpMethod.PATCH,
                getEntityWithHeaders("abc-abc"),
                MovieWithPlotDto.class);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());

        mockOmdbServer.verify();
        mockOmdbServer.reset();
      }
    }
  }

  @Test
  public void testOmdbApi4xxThrows500() throws URISyntaxException {
    for (HttpStatus httpStatus : HttpStatus.values()) {
      if (httpStatus.is4xxClientError()) {
        mockOmdbServer
            .expect(requestTo(
                new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
            ))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(httpStatus).body("OmdbApiClientErrorException"));

        ResponseEntity<MovieWithPlotDto> response = restTemplate
            .exchange("/movies/22/plot", HttpMethod.PATCH,
                getEntityWithHeaders("abc-abc"),
                MovieWithPlotDto.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        mockOmdbServer.verify();
        mockOmdbServer.reset();
      }
    }
  }

  @Test
  public void testEmptyResponseFromOmdbApiReturnsNullPlot() throws URISyntaxException {
    mockOmdbServer
        .expect(requestTo(new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.MOVED_PERMANENTLY));

    ResponseEntity<MovieWithPlotDto> response = restTemplate
        .exchange("/movies/22/plot", HttpMethod.PATCH,
            getEntityWithHeaders("abc-abc"),
            MovieWithPlotDto.class);

    assertNotNull(response.getBody());
    assertNull(response.getBody().getPlot());

    mockOmdbServer.verify();
  }

  @Test
  public void testOmdbApi200WithErrorIncorrectImdbIdResponseThrows404() throws URISyntaxException {
    mockOmdbServer.expect(once(),
        requestTo(new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .body(OmdbService.INCORRECT_ID_JSON_RESPONSE));

    ResponseEntity<MovieWithPlotDto> response = restTemplate
        .exchange("/movies/22/plot", HttpMethod.PATCH,
            getEntityWithHeaders("abc-abc"),
            MovieWithPlotDto.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    mockOmdbServer.verify();
  }

  @Test
  public void testOmdbApi200WithErrorInvalidApikeyResponseThrows401() throws URISyntaxException {
    mockOmdbServer.expect(once(),
        requestTo(new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .body(OmdbService.INVALID_APIKEY_JSON_RESPONSE));

    ResponseEntity<MovieWithPlotDto> response = restTemplate
        .exchange("/movies/22/plot", HttpMethod.PATCH,
            getEntityWithHeaders("abc-abc"),
            MovieWithPlotDto.class);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    mockOmdbServer.verify();
  }

  @Test
  public void testMovieDoesNotExistReturns404() {
    ResponseEntity<MovieWithPlotDto> response = restTemplate
        .exchange("/movies/555/plot", HttpMethod.PATCH,
            getEntityWithHeaders("abc-abc"),
            MovieWithPlotDto.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testPlotAlreadyInReturns200() {
    ResponseEntity<MovieWithPlotDto> response = restTemplate
        .exchange("/movies/3/plot", HttpMethod.PATCH,
            getEntityWithHeaders("abc-abc"),
            MovieWithPlotDto.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testPlotAlreadyInDoesNotReturnPlotFetchedField() {
    ResponseEntity<MovieWithPlotDto> response = restTemplate
        .exchange("/movies/3/plot", HttpMethod.PATCH,
            getEntityWithHeaders("abc-abc"),
            MovieWithPlotDto.class);

    assertNotNull(response.getBody());

    Map responseAsMap = new ObjectMapper().convertValue(response.getBody(), Map.class);
    assertFalse(responseAsMap.containsKey("plotFetched"));
  }

  @Test
  public void testPlotFetchedReturns201() throws URISyntaxException {
    mockOmdbServer.expect(once(),
        requestTo(new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .body(OmdbServiceTest.BODY_WITH_PLOT));

    ResponseEntity<MovieWithPlotDto> response = restTemplate
        .exchange("/movies/22/plot", HttpMethod.PATCH,
            getEntityWithHeaders("abc-abc"),
            MovieWithPlotDto.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    mockOmdbServer.verify();
  }

  @Test
  public void testPlotFetchedDoesNotReturnPlotFetchedField() throws URISyntaxException {
    mockOmdbServer.expect(once(),
        requestTo(new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .body(OmdbServiceTest.BODY_WITH_PLOT));

    ResponseEntity<MovieWithPlotDto> response = restTemplate
        .exchange("/movies/22/plot", HttpMethod.PATCH,
            getEntityWithHeaders("abc-abc"),
            MovieWithPlotDto.class);

    assertNotNull(response.getBody());
    Map responseAsMap = new ObjectMapper().convertValue(response.getBody(), Map.class);
    assertFalse(responseAsMap.containsKey("plotFetched"));

    mockOmdbServer.verify();
  }

  @Test
  public void testPlotFetchedEmptyDoesNotReturnPlotFetchedField() throws URISyntaxException {
    mockOmdbServer.expect(once(),
        requestTo(new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .body(OmdbServiceTest.BODY_WITHOUT_PLOT));

    ResponseEntity<MovieWithPlotDto> response = restTemplate
        .exchange("/movies/22/plot", HttpMethod.PATCH,
            getEntityWithHeaders("abc-abc"),
            MovieWithPlotDto.class);

    assertNotNull(response.getBody());

    Map responseAsMap = new ObjectMapper().convertValue(response.getBody(), Map.class);
    assertFalse(responseAsMap.containsKey("plotFetched"));

    mockOmdbServer.verify();
  }

  private HttpEntity getEntityWithHeaders(String requestId) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(Headers.X_REQUEST_ID, requestId);
    return new HttpEntity<>(headers);
  }

}
