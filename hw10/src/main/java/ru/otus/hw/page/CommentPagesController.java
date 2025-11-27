package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentPagesController {

    @GetMapping("/comments")
    public String listCommentsPage(@RequestParam("book_id") long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "comments";
    }
}
