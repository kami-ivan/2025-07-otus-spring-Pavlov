package ru.otus.hw.rest.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.rest.dto.BookDto;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }

        return new BookDto(book.getId(), book.getTitle(),
                book.getAuthor() != null ? authorMapper.toDto(book.getAuthor()) : null,
                book.getGenre() != null ? genreMapper.toDto(book.getGenre()) : null);
    }

    public Book toEntity(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }

        return new Book(bookDto.getId(), bookDto.getTitle(),
                bookDto.getAuthor() != null ? authorMapper.toEntity(bookDto.getAuthor()) : null,
                bookDto.getGenre() != null ? genreMapper.toEntity(bookDto.getGenre()) : null);
    }
}
