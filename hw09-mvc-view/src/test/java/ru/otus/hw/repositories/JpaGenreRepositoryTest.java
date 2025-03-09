package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе jpa для работы с жанрами")
@DataJpaTest
public class JpaGenreRepositoryTest {

    private static final long TEST_GENRE_ID = 1L;

    @Autowired
    private GenreRepository jpaGenreRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Genre> genres;

    @BeforeEach
    void init() {
        genres = getDbGenres();
    }

    @DisplayName("Должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        List<Genre> actualGenres = jpaGenreRepository.findAll();
        List<Genre> expectedGenres = genres;
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("Должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var actualGenre = jpaGenreRepository.findById(TEST_GENRE_ID);
        var expectedGenre = entityManager.find(Genre.class, TEST_GENRE_ID);
        assertThat(actualGenre)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id)).toList();
    }
}