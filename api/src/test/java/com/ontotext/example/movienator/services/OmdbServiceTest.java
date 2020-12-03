package com.ontotext.example.movienator.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.ontotext.example.movienator.BaseMovieTest;
import com.ontotext.example.movienator.exceptions.IncorrectOmdbIdException;
import com.ontotext.example.movienator.exceptions.InvalidOmdbApikeyException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class OmdbServiceTest extends BaseMovieTest {

  public static final String BODY_WITHOUT_PLOT =
      "{\"Title\":\"The Amazing Spider-Man\",\"Year\":\"2012\","
          + "\"Rated\":\"PG-13\",\"Released\":\"03 Jul 2012\",\"Runtime\":\"136 min\","
          + "\"Genre\":\"Action, Adventure, Sci-Fi\",\"Director\":\"Marc Webb\","
          + "\"Writer\":\"James Vanderbilt (screenplay), Alvin Sargent (screenplay), "
          + "Steve Kloves (screenplay),"
          + " James Vanderbilt (story), Stan Lee (based on the Marvel comic book by), "
          + "Steve Ditko (based on the Marvel comic book by)\","
          + "\"Actors\":\"Andrew Garfield, Emma Stone, Rhys Ifans, Denis Leary\","
          + "\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"2 wins & 31 nominations.\","
          + "\"Poster\":\"https://m.media-amazon.com/images/M"
          + "/MV5BMjMyOTM4MDMxNV5BMl5BanBnXkFtZTcwNjIyNzExOA@@._V1_SX300.jpg\","
          + "\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.0/10\"},"
          + "{\"Source\":\"Rotten Tomatoes\","
          + "\"Value\":\"72%\"},{\"Source\":\"Metacritic\",\"Value\":\"66/100\"}],"
          + "\"Metascore\":\"66\",\"imdbRating\":\"7.0\","
          + "\"imdbVotes\":\"528,039\",\"imdbID\":\"tt0948470\",\"Type\":\"movie\","
          + "\"DVD\":\"09 Nov 2012\",\"BoxOffice\":\"$262,030,663\","
          + "\"Production\":\"Sony Pictures\","
          + "\"Website\":\"http://www.theamazingspiderman.com/site/\",\"Response\":\"True\"}";

  public static final String BODY_WITH_PLOT =
      "{\"Title\":\"The Amazing Spider-Man\",\"Year\":\"2012\","
          + "\"Rated\":\"PG-13\",\"Released\":\"03 Jul 2012\",\"Runtime\":\"136 min\","
          + "\"Genre\":\"Action, Adventure, Sci-Fi\",\"Director\":\"Marc Webb\","
          + "\"Writer\":\"James Vanderbilt (screenplay), Alvin Sargent (screenplay), "
          + "Steve Kloves (screenplay),"
          + " James Vanderbilt (story), Stan Lee (based on the Marvel comic book by), "
          + "Steve Ditko (based on the Marvel comic book by)\","
          + "\"Actors\":\"Andrew Garfield, Emma Stone, Rhys Ifans, Denis Leary\","
          + "\"Plot\":\"After Peter Parker is bitten by a genetically altered spider,"
          + " he gains newfound, spider-like powers and ventures out to solve the mystery "
          + "of his parent's mysterious death.\","
          + "\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"2 wins & 31 nominations.\","
          + "\"Poster\":\"https://m.media-amazon.com/images/M"
          + "/MV5BMjMyOTM4MDMxNV5BMl5BanBnXkFtZTcwNjIyNzExOA@@._V1_SX300.jpg\","
          + "\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.0/10\"},"
          + "{\"Source\":\"Rotten Tomatoes\","
          + "\"Value\":\"72%\"},{\"Source\":\"Metacritic\",\"Value\":\"66/100\"}],"
          + "\"Metascore\":\"66\",\"imdbRating\":\"7.0\","
          + "\"imdbVotes\":\"528,039\",\"imdbID\":\"tt0948470\",\"Type\":\"movie\","
          + "\"DVD\":\"09 Nov 2012\",\"BoxOffice\":\"$262,030,663\","
          + "\"Production\":\"Sony Pictures\","
          + "\"Website\":\"http://www.theamazingspiderman.com/site/\",\"Response\":\"True\"}";

  private static final String BODY_WITH_EMPTY_PLOT =
      "{\"Title\":\"The Amazing Spider-Man\",\"Year\":\"2012\","
          + "\"Rated\":\"PG-13\",\"Released\":\"03 Jul 2012\",\"Runtime\":\"136 min\","
          + "\"Genre\":\"Action, Adventure, Sci-Fi\",\"Director\":\"Marc Webb\","
          + "\"Writer\":\"James Vanderbilt (screenplay), Alvin Sargent (screenplay), "
          + "Steve Kloves (screenplay),"
          + " James Vanderbilt (story), Stan Lee (based on the Marvel comic book by), "
          + "Steve Ditko (based on the Marvel comic book by)\","
          + "\"Actors\":\"Andrew Garfield, Emma Stone, Rhys Ifans, Denis Leary\","
          + "\"Plot\":\"\","
          + "\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"2 wins & 31 nominations.\","
          + "\"Poster\":\"https://m.media-amazon.com/images/M"
          + "/MV5BMjMyOTM4MDMxNV5BMl5BanBnXkFtZTcwNjIyNzExOA@@._V1_SX300.jpg\","
          + "\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"7.0/10\"},"
          + "{\"Source\":\"Rotten Tomatoes\","
          + "\"Value\":\"72%\"},{\"Source\":\"Metacritic\",\"Value\":\"66/100\"}],"
          + "\"Metascore\":\"66\",\"imdbRating\":\"7.0\","
          + "\"imdbVotes\":\"528,039\",\"imdbID\":\"tt0948470\",\"Type\":\"movie\","
          + "\"DVD\":\"09 Nov 2012\",\"BoxOffice\":\"$262,030,663\","
          + "\"Production\":\"Sony Pictures\","
          + "\"Website\":\"http://www.theamazingspiderman.com/site/\",\"Response\":\"True\"}";

  @Autowired
  protected TestRestTemplate restTemplate;
  @Autowired
  protected RestTemplate omdbRestTemplate;

  private MockRestServiceServer mockOmdbServer;

  @Autowired
  private OmdbService omdbService;

  @PostConstruct
  private void postConstruct() {
    mockOmdbServer = MockRestServiceServer.createServer(omdbRestTemplate);
  }

  @Test
  public void testOmdbApiReturns1xxStausCode() throws URISyntaxException {
    for (HttpStatus httpStatus : HttpStatus.values()) {
      if (httpStatus.is3xxRedirection()) {
        mockOmdbServer
            .expect(requestTo(
                new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
            )).andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(httpStatus));

        assertNull(omdbService.getPlotForMovie("tt0948470", "abc-abc"));

        mockOmdbServer.verify();
        mockOmdbServer.reset();
      }
    }
  }

  @Test
  public void testOmdbApiReturns3xxStausCode() throws URISyntaxException {
    for (HttpStatus httpStatus : HttpStatus.values()) {
      if (httpStatus.is3xxRedirection()) {
        mockOmdbServer
            .expect(requestTo(
                new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
            )).andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(httpStatus));

        assertNull(omdbService.getPlotForMovie("tt0948470", "abc-abc"));

        mockOmdbServer.verify();
        mockOmdbServer.reset();
      }
    }
  }

  @Test
  public void testOmdbApiReturns4xxStausCode() throws URISyntaxException {
    for (HttpStatus httpStatus : HttpStatus.values()) {
      if (httpStatus.is3xxRedirection()) {
        mockOmdbServer
            .expect(requestTo(
                new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
            )).andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(httpStatus));

        assertNull(omdbService.getPlotForMovie("tt0948470", "abc-abc"));

        mockOmdbServer.verify();
        mockOmdbServer.reset();
      }
    }
  }

  @Test
  public void testOmdbApiReturns5xxStausCode() throws URISyntaxException {
    for (HttpStatus httpStatus : HttpStatus.values()) {
      if (httpStatus.is3xxRedirection()) {
        mockOmdbServer
            .expect(requestTo(
                new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
            )).andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(httpStatus));

        assertNull(omdbService.getPlotForMovie("tt0948470", "abc-abc"));

        mockOmdbServer.verify();
        mockOmdbServer.reset();
      }
    }
  }

  @Test
  public void testOmdbApiReturns200StatusCodeWithNullPlot() throws URISyntaxException {
    mockOmdbServer
        .expect(requestTo(
            new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
        )).andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK).body(BODY_WITHOUT_PLOT));

    assertNull(omdbService.getPlotForMovie("tt0948470", "abc-abc"));

    mockOmdbServer.verify();
  }

  @Test
  public void testOmdbApiReturns200StatusCodeWithEmptyPlot() throws URISyntaxException {
    mockOmdbServer
        .expect(requestTo(
            new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
        )).andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .body(BODY_WITH_EMPTY_PLOT));

    assertEquals("", omdbService.getPlotForMovie("tt0948470", "abc-abc"));

    mockOmdbServer.verify();
  }

  @Test
  public void testOmdbApiReturns200WithIncorrectImdbIdResponseThrowsIncorrectImdbIdException()
      throws URISyntaxException {
    mockOmdbServer.expect(once(),
        requestTo(
            new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
        )).andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .body(OmdbService.INCORRECT_ID_JSON_RESPONSE));

    assertThrows(IncorrectOmdbIdException.class,
        () -> omdbService.getPlotForMovie("tt0948470", "abc-abc"));

    mockOmdbServer.verify();
  }

  @Test
  public void testOmdbApiReturns200WithInvalidApikeyResponseThrowsInvalidOmdbApikeyException()
      throws URISyntaxException {
    mockOmdbServer.expect(once(),
        requestTo(
            new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
        )).andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .body(OmdbService.INVALID_APIKEY_JSON_RESPONSE));

    assertThrows(InvalidOmdbApikeyException.class,
        () -> omdbService.getPlotForMovie("tt0948470", "abc-abc"));

    mockOmdbServer.verify();
  }

  @Test
  public void testOmdbApiReturns200StatusCodeWithActualPlot() throws URISyntaxException {
    mockOmdbServer
        .expect(requestTo(
            new URI("http://www.omdbapi.com/?i=tt0948470&apikey=apikeyValue")
        )).andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
            .body(BODY_WITH_PLOT));

    assertEquals(
        "After Peter Parker is bitten by a genetically altered spider, "
            + "he gains newfound, spider-like powers and ventures out to solve "
            + "the mystery of his parent's mysterious death.",
        omdbService.getPlotForMovie("tt0948470", "abc-abc"));

    mockOmdbServer.verify();
  }

}
