package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.rest.exceptions.ErrorDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    private static AuthorDto authorDto;
    private static GenreDto genreDto;
    private static List<BookDto> bookDtos;

    @BeforeAll
    static void setUpAll() {
        authorDto = new AuthorDto(1L, "Test_Author_1");
        genreDto = new GenreDto(1L, "Test_Genre_1");
        bookDtos = List.of(new BookDto(1, "Test_Book_1", authorDto, genreDto),
                new BookDto(2, "Test_Book_2", authorDto, genreDto));
    }

    @DisplayName("должен вернуть корректный список книг")
    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnCorrectABooksList() throws Exception {
        when(bookService.findAll()).thenReturn(bookDtos);

        mvc.perform(get("/api/v1/book")).andExpect(status().isOk()).andExpect(content()
                .json(mapper.writeValueAsString(bookDtos)));
    }

    @DisplayName("должен вернуть корректную книгу")
    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnCorrectBook() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.ofNullable(bookDtos.get(0)));

        String url = "/api/v1/book/" + 1L;

        mvc.perform(get(url)).andExpect(status().isOk()).andExpect(content()
                .json(mapper.writeValueAsString(bookDtos.get(0))));
    }

    @DisplayName("должен вернуть ожидаемую ошибку когда книга не найдена")
    @Test
    @WithMockUser(username = "testuser")
    void shouldReturnExpectedErrorWhenBookNotFound() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.empty());

        String url = "/api/v1/book/" + 1L;

        mvc.perform(get(url)).andExpect(status().isNotFound()).andExpect(content()
                .json(mapper.writeValueAsString(new ErrorDto("error", "Book not found"))));
    }

    @DisplayName("должен корректно сохранить книгу")
    @Test
    @WithMockUser(username = "testuser")
    void shouldCorrectSaveBook() throws Exception {
        Book book = new Book(1L, "Test_NewBook_1",
                authorDto.toDomainObject(),
                genreDto.toDomainObject());
        when(bookService.save(any())).thenReturn(book);

        String expectedBook = mapper.writeValueAsString(BookDto.fromDomainObject(book));

        mvc.perform(post("/api/v1/book").contentType(APPLICATION_JSON)
                .content(expectedBook).with(csrf()))
                .andExpect(status().isOk()).andExpect(content().json(expectedBook));
    }

    @DisplayName("должен корректно удалить книгу")
    @Test
    @WithMockUser(username = "testuser")
    void shouldCorrectDeleteBook() throws Exception {
        String url = "/api/v1/book/" + 1L;
        mvc.perform(delete(url).with(csrf())).andExpect(status().isOk());
    }

}
