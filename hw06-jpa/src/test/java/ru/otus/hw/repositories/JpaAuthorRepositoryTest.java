package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе jpa для работы с авторами")
@DataJpaTest
@Import({JpaAuthorRepository.class})
public class JpaAuthorRepositoryTest {

    private static final long TEST_AUTHOR_ID = 1L;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Author> authors;

    @BeforeEach
    void init() {
        authors = getDbAuthors();
    }

    @DisplayName("Должен загружать список всех авторов")
    @Test
    void shouldReturnCorretAuthorsList() {
        List<Author> actualAuthors = jpaAuthorRepository.findAll();
        List<Author> expectedAuthors = authors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("Должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var actualAuthor = jpaAuthorRepository.findById(TEST_AUTHOR_ID);
        var expectedAuthor = entityManager.find(Author.class, TEST_AUTHOR_ID);
        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id)).toList();
    }
}