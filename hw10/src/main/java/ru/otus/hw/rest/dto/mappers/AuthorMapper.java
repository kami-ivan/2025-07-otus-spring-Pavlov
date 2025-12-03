package ru.otus.hw.rest.dto.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author author) {
        if (author == null) {
            return null;
        }

        return new AuthorDto(author.getId(), author.getFullName());
    }

    public Author toEntity(AuthorDto authorDto) {
        if (authorDto == null) {
            return null;
        }

        return new Author(authorDto.getId(), authorDto.getFullName());
    }
}