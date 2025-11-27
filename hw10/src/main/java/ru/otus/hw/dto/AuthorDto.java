package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Author;

@Data
@AllArgsConstructor
public class AuthorDto {
    private long id;

    @NotBlank(message = "fullName should not be blank")
    private String fullName;

    public Author toDomainObject() {
        return new Author(id, fullName);
    }

    public static AuthorDto fromDomainObject(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
