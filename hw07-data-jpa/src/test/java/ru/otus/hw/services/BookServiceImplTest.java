package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работа с книгами")
@DataJpaTest
@ComponentScan({"ru.otus.hw.repositories", "ru.otus.hw.services"})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    private static final int TEST_NUMBER_OF_BOOKS = 3;

    private static final long EXPECTED_BOOK_ID = 1L;

    private static final String EXPECTED_BOOK_TITLE = "BookTitle_1";

    private static final long EXPECTED_NEW_BOOK_ID = 4L;

    private static final String EXPECTED_NEW_BOOK_TITLE = "New_BookTitle_1";

    private static final long EXPECTED_AUTHOR_ID = 1L;

    private static final String EXPECTED_AUTHOR_FULL_NAME = "Author_1";

    private static final long EXPECTED_GENRE_ID = 1L;

    private static final String EXPECTED_GENRE_NAME = "Genre_1";

    private Book expectedBook;

    private Book expectedNewBook;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void setUp() {
        Author expectedAuthor = new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_FULL_NAME);
        Genre expectedGenre = new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);
        expectedBook = new Book(EXPECTED_BOOK_ID, EXPECTED_BOOK_TITLE, expectedAuthor, expectedGenre);
        expectedNewBook = new Book(EXPECTED_NEW_BOOK_ID, EXPECTED_BOOK_TITLE, expectedAuthor, expectedGenre);
    }

    @DisplayName("Должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = bookService.findById(EXPECTED_BOOK_ID);
        assertThat(actualBook)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("Должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBook = bookService.findAll();
        assertThat(actualBook).hasSize(TEST_NUMBER_OF_BOOKS);
    }

    @DisplayName("Должен сохранять новую книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewBook() {
        var newBook = bookService.insert(EXPECTED_BOOK_TITLE, EXPECTED_AUTHOR_ID, EXPECTED_GENRE_ID);
        assertThat(newBook).usingRecursiveComparison().isEqualTo(expectedNewBook);
    }

    @DisplayName("Должен сохранять измененную книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveUpdatedBook() {
        var updatedBook = bookService.update(
                EXPECTED_BOOK_ID,
                EXPECTED_NEW_BOOK_TITLE,
                EXPECTED_AUTHOR_ID,
                EXPECTED_GENRE_ID);
        expectedBook.setTitle(EXPECTED_NEW_BOOK_TITLE);
        assertThat(updatedBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Должен удалять книгу по id")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        assertThat(bookService.findById(EXPECTED_BOOK_ID)).isPresent();
        bookService.deleteById(EXPECTED_BOOK_ID);
        assertThat(bookService.findById(EXPECTED_BOOK_ID)).isNotPresent();
    }
}