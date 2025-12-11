package ru.otus.hw.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Comment;

public interface CommentRepository extends R2dbcRepository<Comment, Long> {
    Mono<Comment> findById(long id);

    Flux<Comment> findAllByBookId(@Param("bookId") long bookId);
}
