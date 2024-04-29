package example.micronaut.dto;

import example.micronaut.domain.Genre;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public class BookUpdateCommandDTO {

    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String isbn;

    private Genre genre;

    public BookUpdateCommandDTO(long id, String name, String isbn, Genre genre) {
        this.id = id;
        this.isbn = isbn;
        this.name = name;
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}