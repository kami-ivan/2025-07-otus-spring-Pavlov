package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.rest.exceptions.ErrorDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
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

    @DisplayName("должен вернуть корректный список жанров")
    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        when(genreService.findAll()).thenReturn(genreDtos);

        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genreDtos)));
    }

    @DisplayName("должен вернуть ожидаемую ошибку когда жанры не найдены")
    @Test
    void shouldReturnExpectedErrorWhenGenresNotFound() throws Exception {
        when(genreService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(mapper.writeValueAsString(
                        new ErrorDto("error", "Genres not found"))));
    }
}
