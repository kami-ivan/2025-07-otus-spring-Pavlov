package ru.otus.hw.services;

import ru.otus.hw.rest.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreDto> findAll();

    Optional<GenreDto> findById(long id);
}
