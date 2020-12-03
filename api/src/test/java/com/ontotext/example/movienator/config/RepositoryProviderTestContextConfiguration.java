package com.ontotext.example.movienator.config;

import com.ontotext.example.movienator.services.RepositoryProvider;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.inferencer.fc.SchemaCachingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class RepositoryProviderTestContextConfiguration {

  @Primary
  @Bean
  public RepositoryProvider repositoryProvider() {
    return new InMemoryRepositoryProvider();
  }

  private class InMemoryRepositoryProvider extends RepositoryProvider {

    private Repository repository;

    public InMemoryRepositoryProvider() {
      repository = new SailRepository(new SchemaCachingRDFSInferencer(new MemoryStore()));
      repository.init();
    }

    @Override
    public Repository getRepository(String requestId) {
      return repository;
    }

    @Override
    public void shutdown() {
      repository.shutDown();
    }

  }

}
