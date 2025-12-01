package ru.otus.hw.dto.mappers;


import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

@Component
public class BookMapper {
    public BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                book.getAuthor(), book.getGenre());
    }

    public Book toEntity(BookDto bookDto) {
        return new Book(bookDto.getId(), bookDto.getTitle(),
                bookDto.getAuthor(), bookDto.getGenre());
    }
}
