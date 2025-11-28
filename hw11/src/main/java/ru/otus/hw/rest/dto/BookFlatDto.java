package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.BookFlat;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookFlatDto {
    private Long id;

    private String title;

    private Long authorId;

    private Long genreId;

    public BookFlat toDomainObject() {
        return new BookFlat(id, title, authorId, genreId);
    }

    public static BookFlatDto fromDomainObject(BookFlat bookFlat) {
        return new BookFlatDto(bookFlat.getId(),
                bookFlat.getTitle(),
                bookFlat.getAuthorId(),
                bookFlat.getGenreId());
    }
}
