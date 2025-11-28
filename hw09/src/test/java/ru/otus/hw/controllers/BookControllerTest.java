package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

    private static AuthorDto authorDto;
    private static GenreDto genreDto;
    private static List<BookDto> bookDtos;

    @BeforeAll
    static void setUpAll() {
        authorDto = new AuthorDto(1, "Test_Author_1");
        genreDto = new GenreDto(1, "Test_Genre_1");
        bookDtos = List.of(
                new BookDto(1, "Test_Book_1", authorDto.toDomainObject(), genreDto.toDomainObject()),
                new BookDto(2, "Test_Book_2", authorDto.toDomainObject(), genreDto.toDomainObject())
        );
    }

    @DisplayName("должен отображать страницу списка с корректными данными")
    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findAll()).thenReturn(bookDtos);

        mvc.perform(get("/"))
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", bookDtos));
    }

    @DisplayName("должен отображать страницу редактирования с корректными данными")
    @Test
    void shouldRenderEditPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findById(1)).thenReturn(Optional.of(bookDtos.get(0)));
        when(authorService.findAll()).thenReturn(List.of(authorDto));
        when(genreService.findAll()).thenReturn(List.of(genreDto));

        BookDto expectedBookDto = bookDtos.get(0);
        List<AuthorDto> expectedAuthorDtos = List.of(authorDto);
        List<GenreDto> expectedGenreDtos = List.of(genreDto);

        mvc.perform(get("/edit/book").param("id", "1"))
                .andExpect(view().name("edit_book"))
                .andExpect(model().attribute("book", expectedBookDto))
                .andExpect(model().attribute("authors", expectedAuthorDtos))
                .andExpect(model().attribute("genres", expectedGenreDtos));
    }

    @DisplayName("должен отображать страницу добавления с корректными данными")
    @Test
    void shouldRenderAddPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findAll()).thenReturn(bookDtos);
        when(authorService.findAll()).thenReturn(List.of(authorDto));
        when(genreService.findAll()).thenReturn(List.of(genreDto));

        BookDto expectedBookDto = BookDto.fromDomainObject(new Book(0, null, null, null));
        List<AuthorDto> expectedAuthorDtos = List.of(authorDto);
        List<GenreDto> expectedGenreDtos = List.of(genreDto);

        mvc.perform(get("/add/book"))
                .andExpect(view().name("add_book"))
                .andExpect(model().attribute("book", expectedBookDto))
                .andExpect(model().attribute("authors", expectedAuthorDtos))
                .andExpect(model().attribute("genres", expectedGenreDtos));
    }

    @DisplayName("должен отображать страницу ошибки когда книга не найдена")
    @Test
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        when(bookService.findById(1)).thenThrow(new EntityNotFoundException("Book not found"));

        mvc.perform(get("/edit/book").param("id", "1"))
                .andExpect(view().name("error"));
    }

    @DisplayName("не должен сохранять книгу и перенаправлять на текущую страницу с ошибками")
    @Test
    void shouldNotSaveBookAndRedirectToCurrentPageWithErrors() throws Exception {
        mvc.perform(post("/edit/book")
                        .param("id", "3")
                        .param("title", "NewTitle"))
                .andExpect(view().name("edit_book"));
    }

    @DisplayName("должен удалять книгу и перенаправлять на начальную страницу")
    @Test
    void shouldDeleteBookAndRedirectToStartPage() throws Exception {
        mvc.perform(delete("/delete/book").param("id", "1"))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteById(any(Long.class));
    }
}
