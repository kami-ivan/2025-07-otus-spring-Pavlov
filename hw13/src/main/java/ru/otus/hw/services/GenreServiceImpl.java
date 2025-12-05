package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreDto::fromDomainObject).toList();
    }

    @Override
    public Optional<GenreDto> findById(long id) {
        return genreRepository.findById(id).map(GenreDto::fromDomainObject);
    }
}
