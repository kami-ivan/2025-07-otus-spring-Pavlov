package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.rest.dto.AuthorDto;


public interface AuthorService {
    Flux<AuthorDto> findAll();

    Mono<AuthorDto> findById(long id);
}
