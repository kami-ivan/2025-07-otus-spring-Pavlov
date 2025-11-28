package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.rest.dto.CommentDto;

public interface CommentService {
    Mono<CommentDto> findById(long id);

    Flux<CommentDto> findAllByBookId(long bookId);

    Mono<Void> deleteById(long id);
}
