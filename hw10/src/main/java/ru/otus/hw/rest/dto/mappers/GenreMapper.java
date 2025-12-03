package ru.otus.hw.rest.dto.mappers;


import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.dto.GenreDto;

@Component
public class GenreMapper {
    public GenreDto toDto(Genre genre) {
        if (genre == null) {
            return null;
        }

        return new GenreDto(genre.getId(), genre.getName());
    }

    public Genre toEntity(GenreDto genreDto) {
        if (genreDto == null) {
            return null;
        }

        return new Genre(genreDto.getId(), genreDto.getName());
    }
}