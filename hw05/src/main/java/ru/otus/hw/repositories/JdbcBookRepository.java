package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private static final String BOOK_ID = "book_id";

    private static final String BOOK_TITLE = "title";

    private static final String GENRE_ID = "genre_id";

    private static final String GENRE_NAME = "genre_name";

    private static final String AUTHOR_ID = "author_id";

    private static final String AUTHOR_FULL_NAME = "full_name";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<Book> findById(long id) {
        try {
            SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
            String sql = "SELECT b.id AS book_id, b.title, a.id AS author_id, " +
                    "a.full_name, g.id AS genre_id, g.name AS genre_name " +
                    "FROM books b " +
                    "LEFT JOIN authors a ON b.author_id = a.id " +
                    "LEFT JOIN genres g ON b.genre_id = g.id " +
                    "WHERE b.id = :id " +
                    "ORDER BY b.id";
            return jdbcTemplate.queryForStream(sql, params, new BookRowMapper()).findFirst();
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Exception when finding book by id");
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            String sql = "SELECT b.id AS book_id, b.title, a.id AS author_id, " +
                    "a.full_name, g.id AS genre_id, g.name AS genre_name " +
                    "FROM books b " +
                    "LEFT JOIN authors a ON b.author_id = a.id " +
                    "LEFT JOIN genres g ON b.genre_id = g.id " +
                    "ORDER BY b.id";
            return jdbcTemplate.query(sql, new BookRowMapper());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Exception when finding all books");
        }
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        try {
            final SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
            String sql = "DELETE FROM books WHERE id = :id";
            if (jdbcTemplate.update(sql, params) == 0) {
                throw new EntityNotFoundException("Can't delete this book (id=%d). Book not found.".formatted(id));
            }
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Exception when deleting book by id");
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        try {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue(BOOK_TITLE, book.getTitle())
                    .addValue(AUTHOR_ID, book.getAuthor().getId())
                    .addValue(GENRE_ID, book.getGenre().getId());
            String sql = "INSERT INTO books (title, author_id, genre_id) VALUES (:title, :author_id, :genre_id)";
            jdbcTemplate.update(sql, params, keyHolder);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Exception when inserting new book");
        }
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        try {
            final SqlParameterSource params = new MapSqlParameterSource()
                    .addValue(BOOK_ID, book.getId())
                    .addValue(BOOK_TITLE, book.getTitle())
                    .addValue(AUTHOR_ID, book.getAuthor().getId())
                    .addValue(GENRE_ID, book.getGenre().getId());
            String sql = "UPDATE books b SET title = :title, author_id = :author_id, genre_id = :genre_id " +
                    "WHERE b.id = :book_id";
            if (jdbcTemplate.update(sql, params) == 0) {
                throw new EntityNotFoundException("Can't update this book (id=%d).".formatted(book.getId()));
            }
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Exception when updating book with id");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return constructBook(rs);
        }
    }

    private static Book constructBook(ResultSet rs) {
        Book book = new Book();
        try {
            book.setId(rs.getLong(BOOK_ID));
            book.setTitle(rs.getString(BOOK_TITLE));
            Author author = constructAuthor(rs);
            book.setAuthor(author);
            Genre genre = constructGenre(rs);
            book.setGenre(genre);
            return book;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Exception when constructing book object");
        }
    }

    private static Author constructAuthor(ResultSet rs) {
        Author author = new Author();
        try {
            author.setId(rs.getLong(AUTHOR_ID));
            author.setFullName(rs.getString(AUTHOR_FULL_NAME));
            return author;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Exception when constructing author object");
        }
    }

    private static Genre constructGenre(ResultSet rs) {
        Genre genre = new Genre();
        try {
            genre.setId(rs.getLong(GENRE_ID));
            genre.setName(rs.getString(GENRE_NAME));
            return genre;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Exception when constructing genre object");
        }
    }
}
