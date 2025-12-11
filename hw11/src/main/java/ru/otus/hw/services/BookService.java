package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.BookFlatDto;

public interface BookService {
    Mono<BookDto> findById(long id);

    Flux<BookDto> findAll();

    Mono<BookFlatDto> save(Book book);

    Mono<Void> deleteById(long id);
}
