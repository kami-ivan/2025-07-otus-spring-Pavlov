package ru.otus.hw.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/v1/book/{book_id}/comment")
    public List<CommentDto> getAllCommentsByBookId(@PathVariable("book_id") long bookId) {
        return commentService.findAllByBookId(bookId);
    }
}
