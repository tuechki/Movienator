package com.ontotext.example.movienator.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ontotext.example.movienator.BaseMovieTest;
import com.ontotext.example.movienator.dtos.MovieByIdDto;
import com.ontotext.example.movienator.dtos.MovieWithPlotDto;
import com.ontotext.example.movienator.exceptions.IncorrectOmdbIdException;
import com.ontotext.example.movienator.exceptions.InvalidOmdbApikeyException;
import com.ontotext.example.movienator.exceptions.MovieDoesNotExistException;
import com.ontotext.example.movienator.exceptions.ValidationException;
import com.ontotext.example.movienator.models.RequestParameters;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

public class MovieServiceTest extends BaseMovieTest {
  @Autowired
  protected TestRestTemplate restTemplate;
  @Autowired
  protected RestTemplate omdbRestTemplate;
  @Autowired
  private MovieService movieService;

  @MockBean
  private OmdbService omdbService;

  private static void areEqual(MovieByIdDto expectedMovie, MovieByIdDto actualMovie) {
    assertEquals(expectedMovie.getId(), actualMovie.getId());
    assertEquals(expectedMovie.getTitle(), actualMovie.getTitle());
    assertEquals(expectedMovie.getLanguage(), actualMovie.getLanguage());
    assertEquals(expectedMovie.getReleased(), actualMovie.getReleased());
    assertEquals(expectedMovie.getBudget(), actualMovie.getBudget());
    assertEquals(expectedMovie.getGross(), actualMovie.getGross());
    assertEquals(expectedMovie.getAspectRatio(), actualMovie.getAspectRatio());
    assertEquals(expectedMovie.getImdbRating(), actualMovie.getImdbRating());
    assertEquals(expectedMovie.getCastTotalFbLikes(), actualMovie.getCastTotalFbLikes());
    assertEquals(expectedMovie.getMovieFbLikes(), actualMovie.getMovieFbLikes());
    assertEquals(expectedMovie.getRuntime(), actualMovie.getRuntime());
    assertEquals(expectedMovie.getColor(), actualMovie.getColor());
    assertEquals(expectedMovie.getCountry(), actualMovie.getCountry());
    assertEquals(expectedMovie.getDirector(), actualMovie.getDirector());
    assertEquals(expectedMovie.getActors(), actualMovie.getActors());
    assertEquals(expectedMovie.getGenres(), actualMovie.getGenres());
    assertEquals(expectedMovie.getNumCritics(), actualMovie.getNumCritics());
    assertEquals(expectedMovie.getNumUserReviews(), actualMovie.getNumUserReviews());
    assertEquals(expectedMovie.getNumUserVotes(), actualMovie.getNumUserVotes());
    assertEquals(expectedMovie.getNumFacesInPoster(), actualMovie.getNumFacesInPoster());
    assertEquals(expectedMovie.getKeywords(), actualMovie.getKeywords());
    assertEquals(expectedMovie.getImdbLink(), actualMovie.getImdbLink());
  }

  private MovieByIdDto getMovieByIdDtoWithId22() {
    MovieByIdDto movie = new MovieByIdDto();

    movie.setId("22");
    movie.setTitle("The Amazing Spider-Man");
    movie.setLanguage("English");
    movie.setReleased(2012);
    movie.setBudget(230000000L);
    movie.setGross(262030663L);
    movie.setAspectRatio(2.35);
    movie.setImdbRating(7.0);
    movie.setCastTotalFbLikes(28489L);
    movie.setMovieFbLikes(56000L);
    movie.setRuntime(153);
    movie.setColor("Color");
    movie.setCountry("USA");
    movie.setDirector("Marc Webb");
    movie.setActors(getActors());
    movie.setGenres(getGenres());
    movie.setNumCritics(599L);
    movie.setNumUserReviews(1225L);
    movie.setNumUserVotes(451803L);
    movie.setNumFacesInPoster(0);
    movie.setKeywords(getKeywords());
    movie.setImdbLink("http://www.imdb.com/title/tt0948470");

    return movie;
  }

  private List<String> getActors() {
    List<String> actors = new ArrayList<>();

    actors.add("Emma Stone");
    actors.add("Andrew Garfield");
    actors.add("Chris Zylka");

    return actors;
  }

  private List<String> getGenres() {
    List<String> genres = new ArrayList<>();

    genres.add("Action");
    genres.add("Adventure");
    genres.add("Fantasy");

    return genres;
  }

  private List<String> getKeywords() {
    List<String> keywords = new ArrayList<>();

    keywords.add("spider man");
    keywords.add("lizard");
    keywords.add("outcast");
    keywords.add("spider");
    keywords.add("teenager");

    return keywords;
  }

  private MovieByIdDto getMovieByIdFromMovieService(String movieId, String requestId) {
    return movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setMovieId(movieId)
        .setRequestId(requestId)
        .build());
  }

  @Test
  public void getMovieByIdReturnsTheRightAndFullInfoAboutTheMovie() {
    areEqual(getMovieByIdDtoWithId22(), getMovieByIdFromMovieService("22", "abc-abc"));
  }

  @Test
  public void getMovieByIdWhenMissingIdThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> getMovieByIdFromMovieService("", "abc-abc"));
  }

  @Test
  public void getMovieByIdWithInvalidIdThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> getMovieByIdFromMovieService(": :", "abc-abc"));
  }

  @Test
  public void getMovieByIdWhenWhenMovieWithThisIdDoesNotExistThrowsMovieDoesNotExistException() {
    assertThrows(MovieDoesNotExistException.class,
        () -> getMovieByIdFromMovieService("5", "abc-abc"));
  }

  private MovieWithPlotDto getMovieWithPlotByIdFromMovieService(String movieId, String requestId) {
    return movieService.getMovieWithPlotById(new RequestParameters.RequestParametersBuilder()
        .setMovieId(movieId)
        .setRequestId(requestId)
        .build());
  }

  @Test
  public void movieDoesNotExistExceptionIsThrownWhenMissingId() {
    Mockito.when(omdbService.getPlotForMovie("tt0948470", "abc-abc"))
        .thenThrow(MovieDoesNotExistException.class);

    assertThrows(MovieDoesNotExistException.class,
        () -> getMovieWithPlotByIdFromMovieService("22", "abc-abc")
    );
  }

  @Test
  public void incorrectImdbIdExceptionIsThrownWhenMovieWithThisIdDoesNotExist() {
    Mockito.when(omdbService.getPlotForMovie("tt0948470", "abc-abc"))
        .thenThrow(IncorrectOmdbIdException.class);

    assertThrows(IncorrectOmdbIdException.class,
        () -> getMovieWithPlotByIdFromMovieService("22", "abc-abc")
    );

  }

  @Test
  public void invalidOmdbApikeyExceptionIsThrownWhenApikeyFailToAuthorize() {
    Mockito.when(omdbService.getPlotForMovie("tt0948470", "abc-abc"))
        .thenThrow(InvalidOmdbApikeyException.class);

    assertThrows(InvalidOmdbApikeyException.class,
        () -> getMovieWithPlotByIdFromMovieService("22", "abc-abc")
    );
  }

  @Test
  public void moviePlotFetchedAndSetProperly() {
    String plot = "After Peter Parker is bitten by a genetically altered spider, "
        + "he gains newfound, spider-like powers and ventures out to solve "
        + "the mystery of his parent's mysterious death.";

    Mockito.when(omdbService.getPlotForMovie("tt0948470", "abc-abc"))
        .thenReturn(plot);

    assertEquals(plot,
        getMovieWithPlotByIdFromMovieService("22", "abc-abc").getPlot()
    );
  }

  @Test
  public void moviePlotFetchedAndSetProperlyAndOnSecondGettingPlotFetchedIsFalse() {
    String plot = "After Peter Parker is bitten by a genetically altered spider, "
        + "he gains newfound, spider-like powers and ventures out to solve "
        + "the mystery of his parent's mysterious death.";

    Mockito.when(omdbService.getPlotForMovie("tt0948470", "abc-abc"))
        .thenReturn(plot);

    assertTrue(getMovieWithPlotByIdFromMovieService("22", "abc-abc").isPlotFetched());
    assertFalse(getMovieWithPlotByIdFromMovieService("22", "abc-abc").isPlotFetched());
  }

  @Test
  public void omdbServiceReturnsEmptyPlotIsEmpty() {
    Mockito.when(omdbService.getPlotForMovie("tt0948470", "abc-abc"))
        .thenReturn("");

    assertEquals("", getMovieWithPlotByIdFromMovieService("22", "abc-abc").getPlot());
  }

  @Test
  public void omdbServiceReturnsNullPlotStaysNull() {
    Mockito.when(omdbService.getPlotForMovie("tt0948470", "abc-abc"))
        .thenReturn(null);

    assertNull(getMovieWithPlotByIdFromMovieService("22", "abc-abc").getPlot());
  }

  @Test
  public void moviePlotAlreadyIn() {
    String plot = "A cryptic message from 007's past sends him pitted against "
        + "a mysterious terrorist organization called Spectre, "
        + "and learns of its involvement in previous events of his most dangerous missions.";

    MovieWithPlotDto movie = getMovieWithPlotByIdFromMovieService("3", "abc-abc");

    assertEquals(plot, movie.getPlot());
    assertFalse(movie.isPlotFetched());

    Mockito.verifyZeroInteractions(omdbService);
  }

  @Test
  public void getMovieWithPlotByIdWithInvalidIdThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> getMovieWithPlotByIdFromMovieService(": :", "abc-abc"));
  }

  @Test
  public void getMovieByIdWithPlotWhenMovieWithIdDoesNotExistThrowsMovieDoesNotExistException() {
    assertThrows(MovieDoesNotExistException.class,
        () -> getMovieByIdFromMovieService("5", "abc-abc"));
  }

}
