package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.rest.exceptions.ErrorDto;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private UserService userService;

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

    @DisplayName("должен вернуть корректный список книг для аутентифицированных пользователей")
    @ParameterizedTest
    @MethodSource("provideAuthenticatedUsers")
    void shouldReturnCorrectBooksListForAuthenticatedUsers(String username, String[] roles) throws Exception {
        when(bookService.findAll()).thenReturn(bookDtos);

        mvc.perform(get("/api/v1/book")
                        .with(user(username).roles(roles)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDtos)));
    }

    @DisplayName("должен вернуть ожидаемую ошибку когда книги не найдены для аутентифицированных пользователей")
    @ParameterizedTest
    @MethodSource("provideAuthenticatedUsers")
    void shouldReturnExpectedErrorWhenBooksNotFoundForAuthenticatedUsers(String username, String[] roles)
            throws Exception {
        when(bookService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/v1/book")
                        .with(user(username).roles(roles)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(mapper.writeValueAsString(
                        new ErrorDto("NOT_FOUND", "Books not found"))));
    }

    private static Stream<Arguments> provideAuthenticatedUsers() {
        return Stream.of(
                Arguments.of("tester", new String[]{"USER"}),
                Arguments.of("admin", new String[]{"ADMIN"}));
    }

    @DisplayName("должен запретить POST для USER")
    @Test
    @WithMockUser(username = "tester", roles = {"USER"})
    void shouldForbidPostForUser() throws Exception {
        Book book = new Book(1L, "Test_NewBook_1", authorDto.toDomainObject(), genreDto.toDomainObject());
        String bookJson = mapper.writeValueAsString(BookDto.fromDomainObject(book));

        mvc.perform(post("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON).content(bookJson)
                .with(csrf())).andExpect(status().isForbidden());
    }

    @DisplayName("должен разрешить POST для ADMIN")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldAllowPostForAdmin() throws Exception {
        Book book = new Book(1L, "Test_NewBook_1", authorDto.toDomainObject(), genreDto.toDomainObject());
        String bookJson = mapper.writeValueAsString(BookDto.fromDomainObject(book));
        when(bookService.save(any(Book.class))).thenReturn(book);

        mvc.perform(post("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON).content(bookJson)
                .with(csrf())).andExpect(status().isOk());
    }

    @DisplayName("должен запретить DELETE для USER")
    @Test
    @WithMockUser(username = "tester", roles = {"USER"})
    void shouldForbidDeleteForUser() throws Exception {
        mvc.perform(delete("/api/v1/book/1").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @DisplayName("должен разрешить DELETE для ADMIN")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldAllowDeleteForAdmin() throws Exception {
        mvc.perform(delete("/api/v1/book/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("должен вернуть корректную книгу")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnCorrectBook() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.ofNullable(bookDtos.get(0)));

        String url = "/api/v1/book/" + 1L;

        mvc.perform(get(url)).andExpect(status().isOk()).andExpect(content()
                .json(mapper.writeValueAsString(bookDtos.get(0))));
    }

    @DisplayName("должен корректно сохранить книгу")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
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
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCorrectDeleteBook() throws Exception {
        String url = "/api/v1/book/" + 1L;
        mvc.perform(delete(url).with(csrf())).andExpect(status().isOk());
    }

}
