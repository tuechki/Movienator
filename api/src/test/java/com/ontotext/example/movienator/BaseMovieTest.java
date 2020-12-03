package com.ontotext.example.movienator;

import com.ontotext.example.movienator.config.MovieGraphDbHealthCheckTestConfiguration;
import com.ontotext.example.movienator.config.RepositoryProviderTestContextConfiguration;
import com.ontotext.example.movienator.healthcheck.MovieGraphDbHealthCheck;
import com.ontotext.example.movienator.models.Movie;
import com.ontotext.example.movienator.services.RepositoryProvider;
import com.ontotext.healthcheck.model.DetailedHealthCheckResponse;
import com.ontotext.healthcheck.model.DetailedHealthCheckResponse.Status;
import java.io.File;
import java.io.IOException;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@Import({RepositoryProviderTestContextConfiguration.class, MovieGraphDbHealthCheckTestConfiguration.class})
public class BaseMovieTest {

  protected static final String X_REQUEST_ID_HEADER = "abc-abc";
  protected static final String MOVIE_ID = "22";
  private static final String MOVIE_DB_RDF_DATA = "java_demo_movieDB.trig";

  @Autowired
  protected RepositoryProvider repositoryProvider;

  @BeforeEach
  public void before() throws IOException {
    File rdfFile = new ClassPathResource(MOVIE_DB_RDF_DATA).getFile();

    try (RepositoryConnection con = repositoryProvider.getRepository(X_REQUEST_ID_HEADER)
        .getConnection()) {
      con.add(rdfFile, "", RDFFormat.TRIG);
    }
  }

  @AfterEach
  public void after() {
    try (RepositoryConnection connection = repositoryProvider.getRepository(X_REQUEST_ID_HEADER)
        .getConnection()) {
      connection.clear();
    }
  }

}
