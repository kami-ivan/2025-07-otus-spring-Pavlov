package ru.otus.hw.rest.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import static org.mockito.Mockito.when;

@WebFluxTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AuthorService authorService;


    @DisplayName("должен вернуть корректный список авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        when(authorService.findAll()).thenReturn(Flux.just(new AuthorDto(1L, "Test_Author")));
        webTestClient.get().uri("/api/v1/authors").exchange()
                .expectStatus().isOk().expectBodyList(AuthorDto.class)
                .hasSize(1);
    }
}
