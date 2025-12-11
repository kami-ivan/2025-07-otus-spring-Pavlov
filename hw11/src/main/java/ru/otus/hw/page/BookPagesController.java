package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookPagesController {

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listBooksPage() {
        return "books";
    }

    @GetMapping("/edit/book")
    public Mono<String> editBookPage(@RequestParam("id") long id, Model model) {
        Mono<List<AuthorDto>> authorsMono = authorService.findAll().collectList();
        Mono<List<GenreDto>> genreMono = genreService.findAll().collectList();

        return Mono.zip(authorsMono, genreMono).map(
                tuple -> {
                    model.addAttribute("bookId", id);
                    addToModelAG(model, tuple);
                    return "edit_book";
                });
    }

    @GetMapping("/add/book")
    public Mono<String> addBookPage(Model model) {
        BookDto bookDto = new BookDto();
        Mono<List<AuthorDto>> authorsMono = authorService.findAll().collectList();
        Mono<List<GenreDto>> genreMono = genreService.findAll().collectList();

        return Mono.zip(authorsMono, genreMono).map(
                tuple -> {
                    model.addAttribute("book", bookDto);
                    addToModelAG(model, tuple);
                    return "add_book";
                });
    }

    private void addToModelAG(Model model, Tuple2<List<AuthorDto>, List<GenreDto>> tuple) {
        List<AuthorDto> authorDtos = tuple.getT1();
        List<GenreDto> genreDtos = tuple.getT2();

        model.addAttribute("authors", authorDtos);
        model.addAttribute("genres", genreDtos);
    }
}
