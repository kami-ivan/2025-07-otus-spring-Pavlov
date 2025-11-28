package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .map(GenreDto::fromDomainObject);
    }

    @Override
    public Mono<GenreDto> findById(long id) {
        return genreRepository.findById(id).map(GenreDto::fromDomainObject);
    }
}
