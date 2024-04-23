package example.micronaut.repository;

import example.micronaut.configuration.SortingAndOrderArguments;
import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Optional<Book> findById(long id);

    Book save(@NotBlank String name, @NotBlank String isbn, Genre genre);

    Book saveWithException(@NotBlank String name, @NotBlank String isbn, Genre genre);

    void deleteById(long id);

    List<Book> findAll(@NotNull SortingAndOrderArguments args);

    int update(long id, @NotBlank String name, @NotBlank String isbn, Genre genre);

}
