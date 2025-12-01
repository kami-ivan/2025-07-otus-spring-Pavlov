package ru.otus.hw.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.rest.exceptions.EntityNotFoundException;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> getAllGenres() {
        List<GenreDto> genreDtos = genreService.findAll();
        if (genreDtos.isEmpty()) {
            throw new EntityNotFoundException("Genres not found");
        }
        return genreDtos;
    }
}
