package ru.otus.hw.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private long id;

    @NotBlank(message = "title should not be blank")
    private String title;

    @NotNull(message = "author must be selected")
    private AuthorDto author;

    @NotNull(message = "genre must be selected")
    private GenreDto genre;
}
