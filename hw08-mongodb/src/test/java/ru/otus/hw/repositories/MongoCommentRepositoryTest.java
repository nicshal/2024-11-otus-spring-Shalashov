package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе jpa для работы с комментариями")
@DataMongoTest
public class MongoCommentRepositoryTest {

    private static final String DELETE_TEST_COMMENT_ID = "3";

    private static final String TEST_COMMENT_ID = "1";

    private static final String TEST_NEW_COMMENT_ID = "4";

    private static final String TEST_NEW_COMMENT_TEXT = "Comment_4";

    private static final int TEST_NUMBER_OF_COMMENTS = 3;

    private static final String TEST_BOOK_ID = "1";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    private List<Comment> dbComments;

    public MongoCommentRepositoryTest() {
    }

    @BeforeEach
    void setUp() {
        List<Author> dbAuthors = getDbAuthors();
        List<Genre> dbGenres = getDbGenres();
        List<Book> dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbComments = getDbComments(dbBooks);
    }

    @DisplayName("Должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = commentRepository.findById(TEST_COMMENT_ID);
        var expectedComment = dbComments.get(0);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("Должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorretAllCommentsByBookId() {
        var actualBooks = commentRepository.findAll();
        assertThat(actualBooks).hasSize(TEST_NUMBER_OF_COMMENTS);
    }

    @DisplayName("Должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        assertThat(commentRepository.findById(DELETE_TEST_COMMENT_ID)).isPresent();
        commentRepository.deleteById(DELETE_TEST_COMMENT_ID);
        assertThat(commentRepository.findById(DELETE_TEST_COMMENT_ID)).isEmpty();
    }

    @DisplayName("Должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedBook = bookRepository
                .findById(TEST_BOOK_ID)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(TEST_BOOK_ID)));
        var expectedComment = new Comment(TEST_NEW_COMMENT_ID, TEST_NEW_COMMENT_TEXT, expectedBook);
        commentRepository.save(expectedComment);
        var newComment = commentRepository.findById(TEST_NEW_COMMENT_ID);
        assertThat(newComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    private static List<Comment> getDbComments(List<Book> dbBooks) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Comment(String.valueOf(id), "Comment_" + id, dbBooks.get(id - 1))).toList();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(String.valueOf(id), "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(String.valueOf(id), "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(String.valueOf(id), "BookTitle_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }
}