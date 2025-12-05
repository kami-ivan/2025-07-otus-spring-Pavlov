package ru.otus.hw.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.Book;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        List<BookDto> books = bookService.findAll();
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Books not found");
        }
        return books;
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable("id") long id) {
        return bookService.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book not found"));
    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@Valid
                                           @RequestBody
                                           BookDto bookDto) {
        Book book = bookService.save(bookDto.toDomainObject());
        return ResponseEntity.ok(BookDto.fromDomainObject(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> editBook(@PathVariable("id") long id,
                                            @Valid @RequestBody
                                            BookDto bookDto) {
        bookDto.setId(id);
        Book book = bookService.save(bookDto.toDomainObject());
        return ResponseEntity.ok(BookDto.fromDomainObject(book));
    }


    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
    }
}
