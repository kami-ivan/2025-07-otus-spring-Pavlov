package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами")
@DataJpaTest
@Import(JpaBookRepository.class)
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Optional<Book> actualBook = bookRepository.findById(1);
        Book expectedBook = testEntityManager.find(Book.class, 1);
        assertThat(actualBook).isPresent().get().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> actualBooks = bookRepository.findAll();
        assertThat(actualBooks).hasSize(3);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Author author = testEntityManager.find(Author.class, 1);
        Genre genre = testEntityManager.find(Genre.class, 1);
        Book expectedBook = new Book(0, "BookTitle_4", author, genre);

        assertThat(testEntityManager.find(Book.class, 4)).isNull();
        bookRepository.save(expectedBook);
        assertThat(testEntityManager.find(Book.class, 4)).isNotNull();
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book expectedBook = testEntityManager.find(Book.class, 1);
        assertThat(expectedBook).hasFieldOrPropertyWithValue("title", "BookTitle_1");

        expectedBook.setTitle("BookTitle_Test");
        bookRepository.save(expectedBook);
        Book actualBook = testEntityManager.find(Book.class, 1);
        assertThat(actualBook).hasFieldOrPropertyWithValue("title", "BookTitle_Test");
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        assertThat(bookRepository.findById(1)).isPresent();
        bookRepository.deleteById(1);
        assertThat(bookRepository.findById(1)).isEmpty();
    }
}