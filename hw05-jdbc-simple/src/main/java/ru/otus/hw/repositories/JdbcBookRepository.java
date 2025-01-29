package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbc;

    @Override
    public Optional<Book> findById(long id) {
        return namedParameterJdbc.query("""
                        SELECT b.id AS book_id, 
                               b.title AS book_title,
                               a.id AS author_id, 
                               a.full_name AS author_full_name,
                               g.id AS genre_id, 
                               g.name AS genre_name 
                        FROM books b
                        INNER JOIN authors a ON a.id = b.author_id 
                        INNER JOIN genres g ON g.id = b.genre_id
                        WHERE b.id = :id
                        """,
                Map.of("id", id),
                new BookRowMapper()
        ).stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbc.query("""
                        SELECT b.id AS book_id, 
                               b.title AS book_title,
                               a.id AS author_id, 
                               a.full_name AS author_full_name,
                               g.id AS genre_id, 
                               g.name AS genre_name
                        FROM books b
                        INNER JOIN authors a ON a.id = b.author_id 
                        INNER JOIN genres g ON g.id = b.genre_id
                        """,
                new BookRowMapper()
        );
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
        namedParameterJdbc.update("""
                        DELETE 
                        FROM books 
                        WHERE id = :id
                        """,
                Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        namedParameterJdbc.update("""
                        INSERT INTO books (title, author_id, genre_id) 
                        VALUES (:title, :author_id, :genre_id)
                        """,
                params,
                keyHolder,
                new String[]{"id"});
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int result = namedParameterJdbc.update("""
                        UPDATE books 
                        SET id = :id, 
                            title = :title, 
                            author_id = :author_id, 
                            genre_id = :genre_id 
                        WHERE id = :id
                        """,
                Map.of("id", book.getId(),
                        "title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId())
        );
        if (result <= 0) {
            throw new EntityNotFoundException(String.format("Book with id = %s not found in database", book.getId()));
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            String bookTitle = rs.getString("book_title");
            Author author = new Author(rs.getLong("author_id"),
                    rs.getString("author_full_name"));
            Genre genre = new Genre(rs.getLong("genre_id"),
                    rs.getString("genre_name"));
            return new Book(bookId, bookTitle, author, genre);
        }
    }
}
