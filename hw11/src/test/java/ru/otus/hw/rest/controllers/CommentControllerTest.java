package ru.otus.hw.rest.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import static org.mockito.Mockito.when;


@WebFluxTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CommentService commentService;


    @DisplayName("должен вернуть корректный список комментариев")
    @Test
    void shouldReturnCorrectCommentsList() {
        when(commentService.findAllByBookId(1L))
                .thenReturn(Flux.just(new CommentDto(1L, 1L, "Test_Comment")));

        webTestClient.get().uri("/api/v1/book/1/comment")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .hasSize(1);
    }
}
