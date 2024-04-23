package example.micronaut.controller;

import example.micronaut.configuration.SortingAndOrderArguments;
import example.micronaut.domain.Genre;
import example.micronaut.repository.GenreRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static io.micronaut.http.HttpHeaders.LOCATION;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/genres")
class GenreController {

    private final GenreRepository genreRepository;

    GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Get("/{id}")
    Genre show(Long id) {
        return genreRepository
                .findById(id)
                .orElse(null);
    }

    @Put
    HttpResponse<?> update(@Body @Valid GenreUpdateCommand command) {
        int numberOfEntitiesUpdated = genreRepository.update(command.getId(), command.getName());

        return HttpResponse
                .noContent()
                .header(LOCATION, location(command.getId()).getPath());
    }

    @Get(value = "/list{?args*}")
    List<Genre> list(@Valid SortingAndOrderArguments args) {
        return genreRepository.findAll(args);
    }

    @Post
    HttpResponse<Genre> save(@Body @Valid GenreSaveCommand cmd) {
        Genre genre = genreRepository.save(cmd.getName());

        return HttpResponse
                .created(genre)
                .headers(headers -> headers.location(location(genre.getId())));
    }

    @Post("/saveList")
    HttpResponse<List<Genre>> saveList(@Body @Valid List<GenreSaveCommand> cmd) {
        List<Genre> result = new ArrayList<>();
        for (GenreSaveCommand gsc : cmd) {
            result.add(genreRepository.save(gsc.getName()));
        }

        return HttpResponse
                .created(result)
                .headers(headers -> headers.location(location(result.get(0).getId())));
    }

    @Post("/ex")
    HttpResponse<Genre> saveExceptions(@Body @Valid GenreSaveCommand cmd) {
        try {
            Genre genre = genreRepository.saveWithException(cmd.getName());
            return HttpResponse
                    .created(genre)
                    .headers(headers -> headers.location(location(genre.getId())));
        } catch(PersistenceException e) {
            return HttpResponse.noContent();
        }
    }

    @Delete("/{id}")
    HttpResponse<?> delete(Long id) {
        genreRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    private URI location(Long id) {
        return URI.create("/genres/" + id);
    }
}