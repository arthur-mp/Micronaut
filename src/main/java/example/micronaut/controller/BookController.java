package example.micronaut.controller;

import example.micronaut.configuration.SortingAndOrderArguments;
import example.micronaut.domain.Book;
import example.micronaut.domain.Genre;
import example.micronaut.repository.BookRepository;
import example.micronaut.repository.GenreRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static io.micronaut.http.HttpHeaders.LOCATION;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/books")
class BookController {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    BookController(GenreRepository genreRepository, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    @Get("/{id}")
    Book show(Long id) {
        return bookRepository
                .findById(id)
                .orElse(null);
    }

    @Put
    HttpResponse<?> update(@Body @Valid BookUpdateCommand command) {
        Genre genre = genreRepository.findById(command.getGenre().getId()).orElse(null);
        int numberOfEntitiesUpdated = bookRepository.update(command.getId(), command.getName(), command.getIsbn(), genre);

        return HttpResponse
                .noContent()
                .header(LOCATION, location(command.getId()).getPath());
    }

    @Get(value = "/list{?args*}")
    HttpResponse<List<Book>> list(@Valid SortingAndOrderArguments args) {
        return HttpResponse.ok(bookRepository.findAll(args));
    }

    @Post
    HttpResponse<Book> save(@Body @Valid BookSaveCommand cmd) {
        Genre genre = genreRepository.findById(cmd.getGenre().getId()).orElse(null);
        Book book = bookRepository.save(cmd.getName(), cmd.getIsbn(), genre);

        return HttpResponse
                .created(book)
                .headers(headers -> headers.location(location(book.getId())));
    }

    @Post("/saveList")
    HttpResponse<List<Book>> saveList(@Body @Valid List<BookSaveCommand> cmd) {
        List<Book> result = new ArrayList<>();
        for (BookSaveCommand gsc : cmd) {
            Genre genre = genreRepository.findById(gsc.getGenre().getId()).orElse(null);
            result.add(bookRepository.save(gsc.getName(), gsc.getIsbn(), genre));
        }

        return HttpResponse
                .created(result)
                .headers(headers -> headers.location(location(result.get(0).getId())));
    }

    @Post("/ex")
    HttpResponse<Book> saveExceptions(@Body @Valid BookSaveCommand cmd) {
        try {
            Genre genre = genreRepository.findById(cmd.getGenre().getId()).orElse(null);
            Book book = bookRepository.saveWithException(cmd.getName(), cmd.getIsbn(), genre);
            return HttpResponse
                    .created(book)
                    .headers(headers -> headers.location(location(book.getId())));
        } catch (PersistenceException e) {
            return HttpResponse.noContent();
        }
    }

    @Delete("/{id}")
    HttpResponse<?> delete(Long id) {
        bookRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    private URI location(Long id) {
        return URI.create("/genres/" + id);
    }
}