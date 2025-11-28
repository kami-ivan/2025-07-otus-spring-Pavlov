package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public String listPage(@RequestParam("book_id") long bookId, Model model) {
        List<CommentDto> commentDtos = commentService.findAllByBookId(bookId);
        model.addAttribute("comments", commentDtos);
        return "comments";
    }
}
