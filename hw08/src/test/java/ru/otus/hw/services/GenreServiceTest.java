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
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с жанрами")
@DataMongoTest
@Import(GenreServiceImpl.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class GenreServiceTest {
    @Autowired
    private GenreService genreService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать все жанры")
    @Test
    void shouldReturnAllGenres() {
        List<Genre> expectedGenres = mongoTemplate.find(new Query(), Genre.class, "genres");
        List<Genre> actualGenres = genreService.findAll();
        assertThat(expectedGenres.size()).isEqualTo(actualGenres.size());
    }
}
