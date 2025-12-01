package ru.otus.hw.dto.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreMapper {
    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public Genre toEntity(GenreDto genreDto) {
        return new Genre(genreDto.getId(), genreDto.getName());
    }
}
