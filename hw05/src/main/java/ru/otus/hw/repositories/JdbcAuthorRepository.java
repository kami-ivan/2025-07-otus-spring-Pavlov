package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbcTemplate;

    @Override
    public List<Author> findAll() {
        try {
            String sql = "SELECT id, full_name FROM authors";
            return jdbcTemplate.query(sql, new AuthorRowMapper());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Exception when finding all authors");
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
            String sql = "SELECT id, full_name FROM authors WHERE id = :id";
            return jdbcTemplate.queryForStream(sql, params, new AuthorRowMapper()).findFirst();
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Exception when finding author by id");
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            try {
                return new DataClassRowMapper<>(Author.class).mapRow(rs, i);
            } catch (SQLException e) {
                throw new IllegalArgumentException("Exception when mapping author row");
            }
        }
    }
}
