package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с книгами")
@DataJpaTest
@Import(BookServiceImpl.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @DisplayName("должен загружать все связи книги при запросе по id")
    @Test
    void shouldNotThrowLazyExceptionForFindBookById() {
        assertDoesNotThrow(() -> bookService.findById(1)
                .get().getAuthor().getFullName());

        assertDoesNotThrow(() -> bookService.findById(1)
                .get().getGenre().getName());
    }

    @DisplayName("должен загружать все связи книги при запросе всех книг")
    @Test
    void shouldNotThrowLazyExceptionForFindAllBooks() {
        assertDoesNotThrow(() -> bookService
                .findAll()
                .get(0)
                .getAuthor()
                .getFullName());

        assertDoesNotThrow(() -> bookService
                .findAll()
                .get(0)
                .getGenre()
                .getName());
    }
}
