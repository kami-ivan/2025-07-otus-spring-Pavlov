package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listPage(Model model) {
        List<BookDto> bookDtos = bookService.findAll();
        model.addAttribute("books", bookDtos);
        return "books";
    }

    @GetMapping("/edit/book")
    public String editPage(@RequestParam("id") long id, Model model) {
        BookDto bookDto = bookService.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book (id= %d) not found".formatted(id)));
        addToModelBAG(model, bookDto);

        return "edit_book";
    }

    @PostMapping("/edit/book")
    public String saveBook(@Valid @ModelAttribute("book") BookDto bookDto,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            addToModelBAG(model, bookDto);

            return "edit_book";
        }
        bookService.save(bookDto.toDomainObject());
        return "redirect:/";
    }

    @GetMapping("/add/book")
    public String addPage(Model model) {
        BookDto newBookDto = new BookDto();
        addToModelBAG(model, newBookDto);
        return "add_book";
    }

    @DeleteMapping("/delete/book")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

    private void addToModelBAG(Model model, BookDto bookDto) {
        model.addAttribute("book", bookDto);

        List<AuthorDto> authorDtos = authorService.findAll();
        model.addAttribute("authors", authorDtos);

        List<GenreDto> genreDtos = genreService.findAll();
        model.addAttribute("genres", genreDtos);
    }
}
