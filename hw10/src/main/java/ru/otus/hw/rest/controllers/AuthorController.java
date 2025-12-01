package ru.otus.hw.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        List<AuthorDto> authorDtos = authorService.findAll();
        if (authorDtos.isEmpty()) {
            throw new EntityNotFoundException("Authors not found");
        }
        return authorDtos;
    }
}
