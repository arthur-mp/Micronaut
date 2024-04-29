package example.micronaut.repository.elasticsearch;

import example.micronaut.domain.Genre;

import java.util.List;

public interface GenreRepositoryES {

    String saveGenreEs(String name);

    Genre findByGenreName(String name);

    List<Genre> listAllGenreEs();

    boolean deleteById(String id);
}
