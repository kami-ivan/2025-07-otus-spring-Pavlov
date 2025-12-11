package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Mono<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(CommentDto::fromDomainObject);
    }

    @Override
    public Flux<CommentDto> findAllByBookId(long bookId) {
        return commentRepository.findAllByBookId(bookId)
                .map(CommentDto::fromDomainObject);
    }

    @Override
    public Mono<Void> deleteById(long id) {
        return commentRepository.deleteById(id);
    }
}
