package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.rest.dto.BookFlatDto;
import ru.otus.hw.rest.exceptions.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookDataLoader bookDataLoader;

    @Override
    public Mono<BookDto> findById(long id) {
        return bookRepository.findById(id).flatMap(bookDataLoader::loadRelations)
                .map(BookDto::fromDomainObject);
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll().switchIfEmpty(Flux.error(new EntityNotFoundException("Books not found")))
                .transform(bookDataLoader::loadRelations)
                .map(BookDto::fromDomainObject);
    }

    @Override
    public Mono<BookFlatDto> save(Book book) {
        return bookRepository.save(book).map(Book::toBookFlat)
                .map(BookFlatDto::fromDomainObject);

    }

    @Override
    public Mono<Void> deleteById(long id) {
        return bookRepository.deleteById(id);
    }
}