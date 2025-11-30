package ru.otus.hw.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.pages.AuthorPagesController;
import ru.otus.hw.pages.BookPagesController;
import ru.otus.hw.pages.CommentPagesController;
import ru.otus.hw.pages.GenrePagesController;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        AuthorPagesController.class,
        BookPagesController.class,
        CommentPagesController.class,
        GenrePagesController.class
})
@Import(SecurityConfiguration.class)
class MvcControllersSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private UserService userService;

    private static final AuthorDto authorDto = new AuthorDto(1L, "Test Author");
    private static final GenreDto genreDto = new GenreDto(1L, "Test Genre");
    private static final BookDto bookDto = new BookDto(1L, "Test Book", authorDto, genreDto);

    @BeforeEach
    void setUp() {
        when(authorService.findAll()).thenReturn(List.of(authorDto));
        when(genreService.findAll()).thenReturn(List.of(genreDto));
        when(bookService.findAll()).thenReturn(List.of(bookDto));
        when(bookService.findById(anyLong())).thenReturn(Optional.of(bookDto));
        when(commentService.findAllByBookId(anyLong())).thenReturn(List.of());
    }

    @DisplayName("MVC endpoints должны возвращать ожидаемый статус для разных пользователей")
    @ParameterizedTest(name = "{0} {1} for user ({2}) should return {3} status")
    @MethodSource("mvcEndpointsProvider")
    void mvcEndpointsShouldReturnExpectedStatus(String method, String url, String userName,
                                                String[] roles, int expectedStatus) throws Exception {
        var request = RequestBuilderUtils.method2RequestBuilder(method, url);

        if (userName != null && !userName.equals("anonymous")) {
            request = request.with(user(userName).roles(roles));
        }

        mockMvc.perform(request)
                .andExpect(status().is(expectedStatus));
    }

    static Stream<Arguments> mvcEndpointsProvider() {
        var userRoles = new String[]{"USER"};
        var adminRoles = new String[]{"ADMIN"};

        return Stream.of(
                Arguments.of("get", "/", "anonymous", null, 302),
                Arguments.of("get", "/", "user", userRoles, 200),
                Arguments.of("get", "/", "admin", adminRoles, 200),

                Arguments.of("get", "/add/book", "anonymous", null, 302),
                Arguments.of("get", "/add/book", "user", userRoles, 403),
                Arguments.of("get", "/add/book", "admin", adminRoles, 200),

                Arguments.of("get", "/edit/book?id=1", "anonymous", null, 302),
                Arguments.of("get", "/edit/book?id=1", "user", userRoles, 403),
                Arguments.of("get", "/edit/book?id=1", "admin", adminRoles, 200),

                Arguments.of("get", "/authors", "anonymous", null, 302),
                Arguments.of("get", "/authors", "user", userRoles, 200),
                Arguments.of("get", "/authors", "admin", adminRoles, 200),

                Arguments.of("get", "/comments?book_id=1", "anonymous", null, 302),
                Arguments.of("get", "/comments?book_id=1", "user", userRoles, 200),
                Arguments.of("get", "/comments?book_id=1", "admin", adminRoles, 200),

                Arguments.of("get", "/genres", "anonymous", null, 302),
                Arguments.of("get", "/genres", "user", userRoles, 200),
                Arguments.of("get", "/genres", "admin", adminRoles, 200));
    }
}