package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с книгами")
@DataMongoTest
@Import(BookServiceImpl.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("Не должен выбрасывать исключение при запросе книги по id")
    @Test
    void shouldNotThrowExceptionForFindBookById() {
        assertDoesNotThrow(() -> bookService.findById("2")
                .get().getAuthor().getFullName());

        assertDoesNotThrow(() -> bookService.findById("2")
                .get().getGenre().getName());
    }

    @DisplayName("Не должен выбрасывать исключение при запросе всех книг")
    @Test
    void shouldNotThrowExceptionForFindAllBooks() {
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

    @DisplayName("должен загружать все книги")
    @Test
    void shouldReturnAllBooks() {
        List<Book> expectedBooks = mongoTemplate.find(new Query(), Book.class, "books");
        List<Book> actualBooks = bookService.findAll();
        assertThat(expectedBooks.size()).isEqualTo(actualBooks.size());
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnBookById() {
        Query query = new Query(Criteria.where("_id").is("1"));
        Book expectedBook = mongoTemplate.findOne(query, Book.class, "books");
        Optional<Book> actualBook = bookService.findById("1");

        assertThat(actualBook).contains(expectedBook);
    }

    @DisplayName("должен добавлять книгу")
    @Test
    void shouldInsertBook() {
        Book expectedBook = bookService.save("4", "TestBook", "1", "2");
        Query query = new Query(Criteria.where("_id").is("4"));
        Book actualBook = mongoTemplate.findOne(query, Book.class, "books");

        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("должен изменять книгу")
    @Test
    void shouldUpdateBook() {
        Book expectedBook = bookService.save("1", "editedBook", "1", "1");
        Query query = new Query(Criteria.where("_id").is("1"));
        Book actualBook = mongoTemplate.findOne(query, Book.class, "books");

        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() {
        Query queryBeforeDelete = new Query(Criteria.where("_id").is("1"));
        Book bookBeforeDelete = mongoTemplate.findOne(queryBeforeDelete, Book.class, "books");
        assertThat(bookBeforeDelete).isNotNull();

        bookService.deleteById("1");

        Query queryAfterDelete = new Query(Criteria.where("_id").is("1"));
        Book bookAfterDelete = mongoTemplate.findOne(queryAfterDelete, Book.class, "books");
        assertThat(bookAfterDelete).isNull();
    }
}
