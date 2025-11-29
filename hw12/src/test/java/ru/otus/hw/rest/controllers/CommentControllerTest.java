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
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    private static List<CommentDto> commentDtos;

    @BeforeAll
    static void setUpAll() {
        AuthorDto authorDto = new AuthorDto(1L, "Test_Author_1");
        GenreDto genreDto = new GenreDto(1L, "Test_Genre_1");
        BookDto bookDto = new BookDto(1L, "Test_Book_1", authorDto, genreDto);

        commentDtos = List.of(
                new CommentDto(1L, bookDto, "Test_Comment_1"),
                new CommentDto(2L, bookDto, "Test_Comment_2"));
    }

    @DisplayName("должен вернуть корректный список комментариев")
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldReturnCorrectCommentsList() throws Exception {
        String url = "/api/v1/book/" + 1 + "/comment";

        when(commentService.findAllByBookId(1)).thenReturn(commentDtos);

        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDtos)));
    }
}
