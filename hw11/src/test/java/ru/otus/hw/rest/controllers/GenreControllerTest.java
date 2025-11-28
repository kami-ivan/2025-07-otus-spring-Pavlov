package ru.otus.hw.rest.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import static org.mockito.Mockito.when;


@WebFluxTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GenreService genreService;


    @DisplayName("должен вернуть корректный список жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        when(genreService.findAll()).thenReturn(Flux.just(new GenreDto(1L, "Test_Genre")));
        webTestClient.get().uri("/api/v1/genres").exchange()
                .expectStatus().isOk().expectBodyList(GenreDto.class)
                .hasSize(1);
    }
}
