package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DisplayName("Сервис для работы с авторами")
@DataMongoTest
@Import(AuthorServiceImpl.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class AuthorServiceTest {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        List<Author> expectedAuthors = mongoTemplate.find(new Query(), Author.class, "authors");
        List<Author> actualAuthors = authorService.findAll();
        assertThat(expectedAuthors.size()).isEqualTo(actualAuthors.size());
    }
}
