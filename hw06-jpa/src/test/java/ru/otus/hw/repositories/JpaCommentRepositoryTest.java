package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе jpa для работы с комментариями")
@DataJpaTest
@Import({JpaCommentRepository.class})
public class JpaCommentRepositoryTest {

    private static final long TEST_BOOK_ID = 1L;
    private static final long TEST_COMMENT_ID = 1L;
    private static final long TEST_NEW_COMMENT_ID = 4L;
    private static final String TEST_NEW_COMMENT_TEXT = "Comment_10500";

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Comment> dbComments;

    private Book expectedBook;

    @BeforeEach
    void setUp() {
        List<Author> dbAuthors = getDbAuthors();
        List<Genre> dbGenres = getDbGenres();
        List<Book> dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbComments = getDbComments(dbBooks);
        expectedBook = entityManager.find(Book.class, TEST_BOOK_ID);
    }

    @DisplayName("Должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldReturnCorrectCommentById(Comment testComment) {
        Optional<Comment> actualComment = jpaCommentRepository.findById(testComment.getId());
        var expectedComment = entityManager.find(Comment.class, testComment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("Должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorretAllCommentsByBookId() {
        List<Comment> actualComments = jpaCommentRepository.findAllByBookId(TEST_BOOK_ID);
        List<Comment> expectedComments = dbComments.stream().filter(item -> item.getId() == TEST_BOOK_ID).toList();

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("Должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        assertThat(entityManager.find(Comment.class, TEST_COMMENT_ID)).isNotNull();
        jpaCommentRepository.deleteById(TEST_COMMENT_ID);
        assertThat(entityManager.find(Comment.class, TEST_COMMENT_ID)).isNull();
    }

    @DisplayName("Должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(TEST_NEW_COMMENT_ID, TEST_NEW_COMMENT_TEXT, expectedBook);
        var newComment = new Comment(0, TEST_NEW_COMMENT_TEXT, expectedBook);
        jpaCommentRepository.save(newComment);
        entityManager.flush();
        entityManager.clear();
        assertThat(entityManager.find(Comment.class, TEST_NEW_COMMENT_ID))
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);
    }

    private static List<Comment> getDbComments(List<Book> dbBooks) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Comment(id, "Comment_" + id, dbBooks.get(id - 1))).toList();
    }

    private static List<Comment> getDbComments() {
        var dbBooks = getDbBooks();
        return getDbComments(dbBooks);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id, "BookTitle_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

}