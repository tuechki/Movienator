package com.ontotext.example.movienator.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ontotext.example.movienator.BaseMovieTest;
import com.ontotext.example.movienator.dtos.MovieByIdDto;
import com.ontotext.example.movienator.services.MovieService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MovieTest extends BaseMovieTest {

  @Autowired
  private MovieService movieService;

  private MovieByIdDto movie;

  private MovieByIdDto getMovieById() {
    if (movie == null) {
      movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
          .setRequestId(X_REQUEST_ID_HEADER)
          .setMovieId(MOVIE_ID)
          .build());
    }
    return movie;
  }

  @Test
  public void testTitle() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals("The Amazing Spider-Man", movie.getTitle());
  }

  @Test
  public void testLanguage() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals("English", movie.getLanguage());
  }

  @Test
  public void testReleased() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(2012, movie.getReleased());
  }

  @Test
  public void testBudget() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(230000000, movie.getBudget());
  }

  @Test
  public void testGross() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(262030663, movie.getGross());
  }

  @Test
  public void testAspectRation() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(2.35, movie.getAspectRatio());
  }

  @Test
  public void testImdbRating() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(7.0, movie.getImdbRating());
  }

  @Test
  public void testCastTotalFbLikes() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(28489, movie.getCastTotalFbLikes());
  }

  @Test
  public void testMovieFbLikes() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(56000, movie.getMovieFbLikes());
  }

  @Test
  public void testRuntime() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(153, movie.getRuntime());
  }

  @Test
  public void testColor() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals("Color", movie.getColor());
  }

  @Test
  public void testCountry() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals("USA", movie.getCountry());
  }

  @Test
  public void testDirector() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals("Marc Webb", movie.getDirector());
  }

  @Test
  public void testActors() {
    List<String> actors = new ArrayList<>();
    actors.add("Emma Stone");
    actors.add("Andrew Garfield");
    actors.add("Chris Zylka");

    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    for (String actor : movie.getActors()) {
      assertTrue(actors.contains(actor));
    }
  }

  @Test
  public void testGenres() {
    List<String> genres = new ArrayList<>();
    genres.add("Action");
    genres.add("Adventure");
    genres.add("Fantasy");

    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    for (String genre : movie.getGenres()) {
      assertTrue(genres.contains(genre));
    }
  }

  @Test
  public void testNumCritics() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(599, movie.getNumCritics());
  }

  @Test
  public void testNumUserReviews() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(1225, movie.getNumUserReviews());
  }

  @Test
  public void testNumUserVotes() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(451803, movie.getNumUserVotes());
  }

  @Test
  public void testNumFacesInPoster() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals(0, movie.getNumFacesInPoster());
  }

  @Test
  public void testKeywords() {
    List<String> keywords = new ArrayList<>();
    keywords.add("spider man");
    keywords.add("lizard");
    keywords.add("outcast");
    keywords.add("spider");
    keywords.add("teenager");

    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertNotNull(movie.getKeywords());
    for (String keyword : movie.getKeywords()) {
      assertTrue(keywords.contains(keyword));
    }
  }

  @Test
  public void testImdbLink() {
    MovieByIdDto movie = movieService.getMovieById(new RequestParameters.RequestParametersBuilder()
        .setRequestId(X_REQUEST_ID_HEADER)
        .setMovieId(MOVIE_ID)
        .build());

    assertEquals("http://www.imdb.com/title/tt0948470", movie.getImdbLink());
  }

}
