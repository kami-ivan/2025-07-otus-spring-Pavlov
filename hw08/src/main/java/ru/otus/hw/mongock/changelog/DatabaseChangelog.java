package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog
public class DatabaseChangelog {

    private Author author1;

    private Author author2;

    private Author author3;

    private Genre genre1;

    private Genre genre2;

    private Genre genre3;

    @ChangeSet(order = "001", id = "dropDb", author = "admin", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "admin", runAlways = true)
    public void insertAuthors(AuthorRepository repository) {
        author1 = repository.save(new Author("1", "Author_1"));
        author2 = repository.save(new Author("2", "Author_2"));
        author3 = repository.save(new Author("3", "Author_3"));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "admin", runAlways = true)
    public void insertGenres(GenreRepository repository) {
        genre1 = repository.save(new Genre("1", "Genre_1"));
        genre2 = repository.save(new Genre("2", "Genre_2"));
        genre3 = repository.save(new Genre("3", "Genre_3"));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "admin", runAlways = true)
    public void insertBooks(BookRepository repository) {
        repository.save(new Book("1", "BookTitle_1", author1, genre1));
        repository.save(new Book("2", "BookTitle_2", author2, genre2));
        repository.save(new Book("3", "BookTitle_3", author3, genre3));
    }
}
