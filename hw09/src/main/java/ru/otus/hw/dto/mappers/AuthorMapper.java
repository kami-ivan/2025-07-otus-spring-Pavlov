package ru.otus.hw.dto.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public Author toEntity(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getFullName());
    }
}
