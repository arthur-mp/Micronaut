package example.micronaut.controller;

import example.micronaut.domain.Genre;
import example.micronaut.dto.GenreCommandDTO;
import example.micronaut.repository.elasticsearch.GenreRepositoryES;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/genresEs")
public class GenreEsController {

    private final GenreRepositoryES genreRepositoryES;

    GenreEsController(GenreRepositoryES genreRepositoryES) {
        this.genreRepositoryES = genreRepositoryES;
    }

    @Post("/saveEs")
    HttpResponse<String> saveEs(@Body @Valid GenreCommandDTO cmd){
        String idDocumentoEs = genreRepositoryES.saveGenreEs(cmd.getName());

        return HttpResponse
                .created(idDocumentoEs)
                .headers(headers -> headers.location(location(idDocumentoEs)));
    }

    @Get("/getGenreByName")
    HttpResponse<Genre> getGenreByNameEs(@QueryValue String genreName){
        Genre  result = genreRepositoryES.findByGenreName(genreName);

        if (Objects.isNull(result)) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(result);
    }

    @Get("/getAllEs")
    HttpResponse<List<Genre>> getAllEs(){
        List<Genre> result = genreRepositoryES.listAllGenreEs();

        return HttpResponse
                .created(result)
                .headers(headers -> headers.location(location(result.get(0).getId())));
    }

    @Delete("/delete:{id}")
    HttpResponse<?> deleteEs(String id) {
        return genreRepositoryES.deleteById(id) ? HttpResponse.noContent() : HttpResponse.notFound();
    }

    private URI location(String id) {
        return URI.create("/genres/" + id);
    }
}
