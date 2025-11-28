package ru.otus.hw.rest.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.BookFlatDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorRepository authorRepository;

    @MockitoBean
    private GenreRepository genreRepository;

    @MockitoBean
    private BookRepository bookRepository;

    private static Book book;

    private static BookDto bookDto;

    private static BookFlatDto bookFlatDto;


    @BeforeAll
    static void setUpAll() {
        book = new Book(1L, "Test_Book",
                new Author(1L, "Test_Author"),
                new Genre(1L, "Test_Genre"),
                1L, 1L);

        bookDto = new BookDto(1L, "Test_Book",
                new Author(1L, "Test_Author"),
                new Genre(1L, "Test_Genre"));

        bookFlatDto = new BookFlatDto(1L, "Test_Book", 1L, 1L);
    }

    @DisplayName("должен вернуть корректный список книг")
    @Test
    void shouldReturnCorrectABooksList() {
        when(bookService.findAll())
                .thenReturn(Flux.just(bookDto));

        webTestClient.get().uri("/api/v1/book")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(1).value(books -> {
                    BookDto book = books.get(0);
                    assert book.getId().equals(1L);
                    assert book.getTitle().equals("Test_Book");
                });
    }

    @DisplayName("должен вернуть корректную книгу")
    @Test
    void shouldReturnCorrectBook() {
        when(bookService.findById(1L))
                .thenReturn(Mono.just(bookDto));

        webTestClient.get().uri("/api/v1/book/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .value(book -> {
                    assert book.getId().equals(1L);
                    assert book.getTitle().equals("Test_Book");
                    assert book.getAuthor().getFullName().equals("Test_Author");
                    assert book.getGenre().getName().equals("Test_Genre");
                });
    }

    @DisplayName("должен корректно сохранить книгу")
    @Test
    void shouldCorrectSaveBook() {
        when(bookService.save(any(Book.class))).thenReturn(Mono.just(bookFlatDto));

        webTestClient.post().uri("/api/v1/book").contentType(APPLICATION_JSON)
                .bodyValue(book)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .value(createdBook -> {
                    assertThat(createdBook.getId()).isEqualTo(1L);
                    assertThat(createdBook.getTitle()).isEqualTo("Test_Book");
                    assertThat(createdBook.getAuthorId()).isEqualTo(1L);
                    assertThat(createdBook.getGenreId()).isEqualTo(1L);
                });
    }

    @DisplayName("должен корректно удалить книгу")
    @Test
    void shouldCorrectDeleteBook() {
        when(bookRepository.deleteById(1L))
                .thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/v1/book/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }
}

