package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookRepository extends R2dbcRepository<Book, Long> {
    Mono<Book> findById(long id);

    @NonNull
    Flux<Book> findAll();
}
