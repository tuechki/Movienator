package com.ontotext.example.movienator.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ontotext.example.movienator.BaseMovieTest;
import com.ontotext.example.movienator.dtos.MovieWithPlotDto;
import com.ontotext.example.movienator.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RdfServiceTest extends BaseMovieTest {

  @Autowired
  private RdfService rdfService;

  @Autowired
  private MovieService movieService;

  @Test
  public void testMovieExistThrowsValidationExceptionWhenIdIsNull() {
    assertThrows(ValidationException.class, () -> rdfService.movieExists(null, "abc-abc"));
  }

  @Test
  public void testMovieExistReturnsFalseWhenMovieWithThisIdDoesNotExist() {
    assertFalse(rdfService.movieExists("5", "abc-abc"));
  }

  @Test
  public void testMovieExistReturnsTrueWhenMovieWithThisIdExist() {
    assertTrue(rdfService.movieExists("22", "abc-abc"));
  }

  private MovieWithPlotDto getMovieWithPlotDto(String movieId, String plot) {
    MovieWithPlotDto movie = new MovieWithPlotDto();

    movie.setId(movieId);
    movie.setTitle("Spectre");
    movie.setImdbLink("http://www.imdb.com/title/tt2379713");
    movie.setPlot(plot);

    return movie;
  }

  @Test
  public void testInsertPlotToMovieDoesNotThrowException() {
    assertDoesNotThrow(
        () -> rdfService.insertMoviePlot(getMovieWithPlotDto("3", "Some plot."), "abc-abc"));
  }

  @Test
  public void testInsertEmptyPlotToMovieDoesNotThrowException() {
    assertDoesNotThrow(() -> rdfService.insertMoviePlot(getMovieWithPlotDto("3", ""), "abc-abc"));
  }

  @Test
  public void testInsertNullPlotToMovieDoesNotThrowException() {
    assertDoesNotThrow(() -> rdfService.insertMoviePlot(getMovieWithPlotDto("3", null), "abc-abc"));
  }

  @Test
  public void testInsertPlotToMovieWithNullIdThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> rdfService.insertMoviePlot(getMovieWithPlotDto(null, "Some plot."), "abc-abc"));
  }

  @Test
  public void testInsertMovieWithEmptyIdThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> rdfService.insertMoviePlot(getMovieWithPlotDto("", "Some plot"), "abc-abc"));
  }

  @Test
  public void testInsertMovieWithInvalidIdThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> rdfService.insertMoviePlot(getMovieWithPlotDto(": :", "Some plot"), "abc-abc"));
  }

  @Test
  public void testInsertMovieWithIdDoesNotThrowException() {
    assertDoesNotThrow(
        () -> rdfService.insertMoviePlot(getMovieWithPlotDto("3", "Some plot"), "abc-abc"));
  }

  @Test
  public void testInsertMovieWithIdThatDoesNotExistThrowsMovieDoesNotExistException() {
    assertDoesNotThrow(
        () -> rdfService.insertMoviePlot(getMovieWithPlotDto("5", "Some plot"), "abc-abc"));
  }

}
