package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе jpa для работы с авторами")
@DataMongoTest
public class MongoAuthorRepositoryTest {

    private static final String TEST_AUTHOR_ID = "1";

    @Autowired
    private AuthorRepository authorRepository;

    private List<Author> authors;

    @BeforeEach
    void init() {
        authors = getDbAuthors();
    }

    @DisplayName("Должен загружать список всех авторов")
    @Test
    void shouldReturnCorretAuthorsList() {
        List<Author> actualAuthors = authorRepository.findAll();
        List<Author> expectedAuthors = authors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("Должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var actualAuthor = authorRepository.findById(TEST_AUTHOR_ID);
        var expectedAuthor = authors.get(0);
        assertThat(actualAuthor)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(String.valueOf(id), "Author_" + id)).toList();
    }
}