package ru.otus.hw.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Book;

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

    public Book toDomainObject() {
        return new Book(id, title, author.toDomainObject(), genre.toDomainObject());
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                AuthorDto.fromDomainObject(book.getAuthor()),
                GenreDto.fromDomainObject(book.getGenre()));
    }
}
