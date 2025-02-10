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
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class})
class JpaBookRepositoryTest {

    private static final long TEST_BOOK_ID = 1L;
    private static final long TEST_NEW_BOOK_ID = 4L;
    private static final String TEST_NEW_BOOK_TITLE = "BookTitle_10500";
    private static final long TEST_AUTHOR_ID = 1L;
    private static final long TEST_GENRE_ID = 1L;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Book> dbBooks;

    private Genre expectedGenre;

    private Author expectedAuthor;

    private Book expectedBook;

    @BeforeEach
    void setUp() {
        List<Author> dbAuthors = getDbAuthors();
        List<Genre> dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        expectedAuthor = entityManager.find(Author.class, TEST_AUTHOR_ID);
        expectedGenre = entityManager.find(Genre.class, TEST_GENRE_ID);
        expectedBook = entityManager.find(Book.class, TEST_BOOK_ID);
    }

    @DisplayName("Должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book testBook) {
        var actualBook = jpaBookRepository.findById(testBook.getId());
        var expectedBook = entityManager.find(Book.class, testBook.getId());
        assertThat(actualBook)
                .isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = jpaBookRepository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("Должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(TEST_NEW_BOOK_ID, TEST_NEW_BOOK_TITLE,
                expectedAuthor, expectedGenre);
        var newBook = new Book(0, TEST_NEW_BOOK_TITLE,
                expectedAuthor, expectedGenre);
        var returnedBook = jpaBookRepository.save(newBook);
        entityManager.detach(returnedBook);
        assertThat(entityManager.find(Book.class, TEST_NEW_BOOK_ID))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var updatedBook =
                jpaBookRepository.save(new Book(TEST_BOOK_ID, TEST_NEW_BOOK_TITLE, expectedAuthor, expectedGenre));
        entityManager.flush();
        entityManager.detach(updatedBook);
        expectedBook.setTitle(TEST_NEW_BOOK_TITLE);
        assertThat(entityManager.find(Book.class, TEST_BOOK_ID))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        assertThat(jpaBookRepository.findById(TEST_BOOK_ID)).isPresent();
        jpaBookRepository.deleteById(TEST_BOOK_ID);
        assertThat(jpaBookRepository.findById(TEST_BOOK_ID)).isEmpty();
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