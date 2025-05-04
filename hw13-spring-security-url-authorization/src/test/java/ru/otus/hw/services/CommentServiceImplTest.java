package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работа с комментариями")
@DataJpaTest
@ComponentScan({"ru.otus.hw.repositories", "ru.otus.hw.services"})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    private static final long EXPECTED_COMMENT_ID = 1L;

    private static final long EXPECTED_NEW_COMMENT_ID = 4L;

    private static final String EXPECTED_COMMENT_TEXT = "Comment_1";

    private static final String EXPECTED_NEW_COMMENT_TEXT = "New_Comment_1";

    private static final int EXPECTED_NUMBER_OF_COMMENTS_BY_BOOK_ID = 1;

    private static final long EXPECTED_BOOK_ID = 1L;

    private static final String EXPECTED_BOOK_TITLE = "BookTitle_1";

    private static final long EXPECTED_AUTHOR_ID = 1L;

    private static final String EXPECTED_AUTHOR_FULL_NAME = "Author_1";

    private static final long EXPECTED_GENRE_ID = 1L;

    private static final String EXPECTED_GENRE_NAME = "Genre_1";

    private Comment expectedComment;

    private Comment expectedNewComment;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        Author expectedAuthor = new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_FULL_NAME);
        Genre expectedGenre = new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);
        Book expectedBook = new Book(EXPECTED_BOOK_ID, EXPECTED_BOOK_TITLE, expectedAuthor, expectedGenre);
        expectedComment = new Comment(EXPECTED_COMMENT_ID, EXPECTED_COMMENT_TEXT, expectedBook);
        expectedNewComment = new Comment(EXPECTED_NEW_COMMENT_ID, EXPECTED_NEW_COMMENT_TEXT, expectedBook);
    }

    @DisplayName("Должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = commentService.findById(EXPECTED_COMMENT_ID);
        assertThat(actualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);
    }

    @DisplayName("Должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorretAllCommentsByBookId() {
        var actualComments = commentService.findAllByBookId(EXPECTED_BOOK_ID);
        assertThat(actualComments)
                .hasSize(EXPECTED_NUMBER_OF_COMMENTS_BY_BOOK_ID)
                .containsExactlyInAnyOrder(expectedComment);
    }

    @DisplayName("Должен сохранять новый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewComment() {
        var newComment = commentService.insert(EXPECTED_BOOK_ID, EXPECTED_NEW_COMMENT_TEXT);
        assertThat(newComment).usingRecursiveComparison().isEqualTo(expectedNewComment);
    }

    @DisplayName("Должен удалять комментарий по id")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteComment() {
        assertThat(commentService.findById(EXPECTED_COMMENT_ID)).isPresent();
        commentService.deleteById(EXPECTED_COMMENT_ID);
        assertThat(commentService.findById(EXPECTED_COMMENT_ID)).isNotPresent();
    }
}