package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
public class GenreDto {
    private long id;

    @NotBlank(message = "name should not be blank")
    private String name;

    public Genre toDomainObject() {
        return new Genre(id, name);
    }

    public static GenreDto fromDomainObject(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
