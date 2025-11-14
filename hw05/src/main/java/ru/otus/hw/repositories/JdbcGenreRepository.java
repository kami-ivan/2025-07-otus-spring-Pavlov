package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        try {
            String sql = "SELECT id, name FROM genres";
            return jdbcTemplate.query(sql, new GenreRowMapper());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Exception when finding all genres");
        }
    }

    @Override
    public Optional<Genre> findById(long id) {
        try {
            SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
            String sql = "SELECT id, name FROM genres WHERE id = :id";
            return jdbcTemplate.queryForStream(sql, params, new GenreRowMapper()).findFirst();
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Exception when finding genres by id");
        }
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            try {
                return new DataClassRowMapper<>(Genre.class).mapRow(rs, i);
            } catch (SQLException e) {
                throw new SQLException("Exception when mapping genre row");
            }
        }
    }
}
