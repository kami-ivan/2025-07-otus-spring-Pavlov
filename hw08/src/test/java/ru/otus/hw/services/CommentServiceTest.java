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
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями")
@DataMongoTest
@Import(CommentServiceImpl.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCommentById() {
        Query query = new Query(Criteria.where("_id").is("2"));
        Comment expectedComment = mongoTemplate.findOne(query, Comment.class, "comments");
        Optional<Comment> actualComment = commentService.findById("2");

        assertThat(actualComment).contains(expectedComment);
    }

    @DisplayName("должен загружать все комментарии по id книги")
    @Test
    void shouldReturnAllCommentsByBookId() {
        Query query = new Query(Criteria.where("book").is("1"));
        List<Comment> expectedComments = mongoTemplate.find(query, Comment.class, "comments");
        List<Comment> actualComments = commentService.findAllByBookId("1");
        assertThat(actualComments.size()).isEqualTo(expectedComments.size());
    }

    @DisplayName("должен добавлять комментарий")
    @Test
    void shouldInsertComment() {
        Comment expectedComment = commentService.insert("5", "2", "NewComment");
        Query query = new Query(Criteria.where("_id").is("5"));
        Comment actualComment = mongoTemplate.findOne(query, Comment.class, "comments");

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен изменять комментарий")
    @Test
    void shouldUpdateComment() {
        Comment expectedComment = commentService.update("4", "1", "UpdateComment");
        Query query = new Query(Criteria.where("_id").is("4"));
        Comment actualComment = mongoTemplate.findOne(query, Comment.class, "comments");

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        Query queryBeforeDelete = new Query(Criteria.where("_id").is("1"));
        Comment commentBeforeDelete = mongoTemplate.findOne(queryBeforeDelete, Comment.class, "comments");
        assertThat(commentBeforeDelete).isNotNull();

        commentService.deleteById("1");

        Query queryAfterDelete = new Query(Criteria.where("_id").is("1"));
        Comment commentAfterDelete = mongoTemplate.findOne(queryAfterDelete, Comment.class, "comments");
        assertThat(commentAfterDelete).isNull();
    }
}
