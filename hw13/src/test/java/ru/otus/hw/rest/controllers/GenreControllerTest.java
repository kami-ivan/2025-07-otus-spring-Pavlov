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
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.rest.exceptions.ErrorDto;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;

    private static List<GenreDto> genreDtos;

    @BeforeAll
    static void setUpAll() {
        genreDtos = List.of(
                new GenreDto(1L, "Test_Genre_1"),
                new GenreDto(2L, "Test_Genre_2"));
    }

    @DisplayName("должен вернуть корректный список жанров для аутентифицированных пользователей")
    @ParameterizedTest
    @MethodSource("provideAuthenticatedUsers")
    void shouldReturnCorrectGenresListForAuthenticatedUsers(String username, String[] roles) throws Exception {
        when(genreService.findAll()).thenReturn(genreDtos);

        mvc.perform(get("/api/v1/genres")
                        .with(user(username).roles(roles)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genreDtos)));
    }

    @DisplayName("должен вернуть ожидаемую ошибку когда жанры не найдены для аутентифицированных пользователей")
    @ParameterizedTest
    @MethodSource("provideAuthenticatedUsers")
    void shouldReturnExpectedErrorWhenGenresNotFoundForAuthenticatedUsers(String username, String[] roles)
            throws Exception {
        when(genreService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/v1/genres")
                        .with(user(username).roles(roles)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(mapper.writeValueAsString(
                        new ErrorDto("NOT_FOUND", "Genres not found"))));
    }

    private static Stream<Arguments> provideAuthenticatedUsers() {
        return Stream.of(
                Arguments.of("tester", new String[]{"USER"}),
                Arguments.of("admin", new String[]{"ADMIN"}));
    }
}
