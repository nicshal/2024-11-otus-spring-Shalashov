package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations namedParameterJdbc;

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbc.query("""
                        SELECT id, 
                               name 
                        FROM genres
                        """,
                new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        return namedParameterJdbc.query("""
                        SELECT id, 
                               name 
                        FROM genres 
                        WHERE id = :id
                        """,
                Map.of("id", id),
                new GenreRowMapper()
        ).stream().findFirst();
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
