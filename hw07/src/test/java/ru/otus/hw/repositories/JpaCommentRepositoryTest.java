package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldFindCommentById() {
        Optional<Comment> actualComment = commentRepository.findById(1);
        Comment expectedComment = testEntityManager.find(Comment.class, 1);
        assertThat(actualComment).isPresent().get().isEqualTo(expectedComment);
    }

    @DisplayName("должен возвращать все комментарии по id книги")
    @Test
    void shouldFindCommentsByBookId() {
        List<Comment> actualComments = commentRepository.findAllByBookId(1);
        assertThat(actualComments).hasSize(2);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        Book book = testEntityManager.find(Book.class, 2);
        Comment expectedComment = new Comment(0, book, "TestComment_5");
        assertThat(testEntityManager.find(Comment.class, 5)).isNull();
        commentRepository.save(expectedComment);
        assertThat(testEntityManager.find(Comment.class, 5)).isNotNull();
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    void shouldUpdatedComment() {
        Comment expectedComment = testEntityManager.find(Comment.class, 1);
        assertThat(expectedComment).hasFieldOrPropertyWithValue("text", "TestComment_1");
        expectedComment.setText("TestComment_1_Update");
        commentRepository.save(expectedComment);
        Comment actualComment = testEntityManager.find(Comment.class, 1);
        assertThat(actualComment).hasFieldOrPropertyWithValue("text", "TestComment_1_Update");
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(commentRepository.findById(1)).isPresent();
        commentRepository.deleteById(1L);
        assertThat(commentRepository.findById(1)).isEmpty();
    }
}
