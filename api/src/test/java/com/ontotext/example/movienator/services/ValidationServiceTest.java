package com.ontotext.example.movienator.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ontotext.example.movienator.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

public class ValidationServiceTest {

  @Test
  public void testInvalidUriThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> ValidationService.validateUri("http ://www.omdbapi.com/"));
  }

  @Test
  public void testValidUriDoesNotThrowValidationException() {
    assertDoesNotThrow(() -> ValidationService.validateUri("http://www.omdbapi.com/"));
  }

  @Test
  public void testNullArgumentThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> ValidationService.validateNotEmpty(null));
  }

  @Test
  public void testEmptyArgumentThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> ValidationService.validateNotEmpty(""));
  }

  @Test
  public void testWhiteSpaceCharacterArgumentThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> ValidationService.validateNotEmpty(" "));
  }

  @Test
  public void testTabArgumentThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> ValidationService.validateNotEmpty("\t"));
  }

  @Test
  public void testNewLineArgumentThrowsValidationException() {
    assertThrows(ValidationException.class,
        () -> ValidationService.validateNotEmpty("\n"));
  }

  @Test
  public void testNotEmptyOrBlankArgumentDoesNotThrowValidationException() {
    assertDoesNotThrow(() -> ValidationService.validateUri("apikey"));
  }
}
