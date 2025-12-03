package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.rest.dto.mappers.AuthorMapper;
import ru.otus.hw.rest.dto.mappers.BookMapper;
import ru.otus.hw.rest.dto.mappers.GenreMapper;
import ru.otus.hw.rest.exceptions.ErrorDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private BookMapper bookMapper;

    @MockitoBean
    private AuthorMapper authorMapper;

    @MockitoBean
    private GenreMapper genreMapper;

    @Autowired
    private ObjectMapper mapper;

    private static AuthorDto authorDto;
    private static GenreDto genreDto;
    private static BookDto bookDto;
    private static Author author;
    private static Genre genre;
    private static Book book;

    @BeforeAll
    static void setUpAll() {
        authorDto = new AuthorDto(1L, "Test_Author_1");
        genreDto = new GenreDto(1L, "Test_Genre_1");
        bookDto = new BookDto(1L, "Test_Book_1", authorDto, genreDto);    
        
        author = new Author(1L, "Test_Author_1");
        genre = new Genre(1L, "Test_Genre_1");
        book = new Book(1L, "Test_Book_1", author, genre);
    }

    @DisplayName("должен вернуть корректный список книг")
    @Test
    void shouldReturnCorrectABooksList() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(bookDto));

        mvc.perform(get("/api/v1/book")).andExpect(status().isOk()).andExpect(content()
                .json(mapper.writeValueAsString(List.of(bookDto))));
    }

    @DisplayName("должен вернуть корректную книгу")
    @Test
    void shouldReturnCorrectBook() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.ofNullable(bookDto));

        String url = "/api/v1/book/" + 1L;

        mvc.perform(get(url)).andExpect(status().isOk()).andExpect(content()
                .json(mapper.writeValueAsString(bookDto)));
    }

    @DisplayName("должен вернуть ожидаемую ошибку когда книга не найдена")
    @Test
    void shouldReturnExpectedErrorWhenBookNotFound() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.empty());

        String url = "/api/v1/book/" + 1L;

        mvc.perform(get(url)).andExpect(status().isNotFound()).andExpect(content()
                .json(mapper.writeValueAsString(new ErrorDto("error", "Book not found"))));
    }

    @DisplayName("должен корректно сохранять новую книгу")
    @Test
    void shouldCorrectlySaveNewBook() throws Exception {
        when(bookMapper.toDto(any())).thenReturn(bookDto);
        when(bookMapper.toEntity(any())).thenReturn(book);
        when(bookService.save(any())).thenReturn(book);

        String expectedBook = mapper.writeValueAsString(bookDto);

        mvc.perform(post("/api/v1/book")
                        .contentType(APPLICATION_JSON)
                        .content(expectedBook))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBook));
    }

    @DisplayName("должен корректно удалить книгу")
    @Test
    void shouldCorrectDeleteBook() throws Exception {
        String url = "/api/v1/book/" + 1L;
        mvc.perform(delete(url)).andExpect(status().isOk());
    }
}
