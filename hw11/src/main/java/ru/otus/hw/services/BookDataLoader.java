package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

@Service
@RequiredArgsConstructor
public class BookDataLoader {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public Mono<Book> loadRelations(Book book) {
        return Mono.zip(
                authorRepository.findById(book.getAuthorId()),
                genreRepository.findById(book.getGenreId())).map(
                        tuple -> {
                            book.setAuthor(tuple.getT1());
                            book.setGenre(tuple.getT2());
                            return book;
                        });
    }

    public Flux<Book> loadRelations(Flux<Book> booksFlux) {
        return booksFlux.flatMap(this::loadRelations);
    }

}
