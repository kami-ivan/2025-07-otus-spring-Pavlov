package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.exceptions.ErrorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper mapper;

    private static List<AuthorDto> authorDtos;

    @BeforeAll
    static void setUpAll() {
        authorDtos = List.of(
                new AuthorDto(1L, "Test_Author_1"),
                new AuthorDto(2L, "Test_Author_2"));
    }

    @DisplayName("должен вернуть корректный список авторов для аутентифицированных пользователей")
    @ParameterizedTest
    @MethodSource("provideAuthenticatedUsers")
    void shouldReturnCorrectAuthorsListForAuthenticatedUsers(String username, String[] roles) throws Exception {
        when(authorService.findAll()).thenReturn(authorDtos);

        mvc.perform(get("/api/v1/authors")
                        .with(user(username).roles(roles)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authorDtos)));
    }

    @DisplayName("должен вернуть ожидаемую ошибку когда авторы не найдены для аутентифицированных пользователей")
    @ParameterizedTest
    @MethodSource("provideAuthenticatedUsers")
    void shouldReturnExpectedErrorWhenAuthorsNotFoundForAuthenticatedUsers(String username, String[] roles)
            throws Exception {
        when(authorService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/v1/authors")
                        .with(user(username).roles(roles)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(mapper.writeValueAsString(
                        new ErrorDto("NOT_FOUND", "Authors not found"))));
    }

    private static Stream<Arguments> provideAuthenticatedUsers() {
        return Stream.of(
                Arguments.of("tester", new String[]{"USER"}),
                Arguments.of("admin", new String[]{"ADMIN"}));
    }
}
