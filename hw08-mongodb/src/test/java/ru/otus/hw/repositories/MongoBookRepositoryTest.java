package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataMongoTest
class MongoBookRepositoryTest {

    private static final String TEST_BOOK_ID = "1";

    private static final String UPDATE_NEW_BOOK_ID = "2";

    private static final String DELETE_TEST_BOOK_ID = "3";

    private static final String TEST_NEW_BOOK_ID = "4";

    private static final String TEST_NEW_BOOK_TITLE = "BookTitle_17777";

    private static final int TEST_NUMBER_OF_BOOKS = 3;

    @Autowired
    private BookRepository bookRepository;

    private List<Book> dbBooks;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("Должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = bookRepository.findById(TEST_BOOK_ID);
        var expectedBook = dbBooks.get(0);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();
        assertThat(actualBooks).hasSize(TEST_NUMBER_OF_BOOKS);
    }

    @DisplayName("Должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Author expectedAuthor = dbAuthors.get(0);
        Genre expectedGenre = dbGenres.get(0);
        var expectedBook = new Book(TEST_NEW_BOOK_ID, TEST_NEW_BOOK_TITLE,
                expectedAuthor, expectedGenre);
        bookRepository.save(expectedBook);
        var newBook = bookRepository.findById(TEST_NEW_BOOK_ID);
        assertThat(newBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = dbBooks.get(1);
        expectedBook.setTitle(TEST_NEW_BOOK_TITLE);
        bookRepository.save(expectedBook);
        var updatedBook = bookRepository.findById(UPDATE_NEW_BOOK_ID);
        assertThat(updatedBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        assertThat(bookRepository.findById(DELETE_TEST_BOOK_ID)).isPresent();
        bookRepository.deleteById(DELETE_TEST_BOOK_ID);
        assertThat(bookRepository.findById(DELETE_TEST_BOOK_ID)).isEmpty();
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