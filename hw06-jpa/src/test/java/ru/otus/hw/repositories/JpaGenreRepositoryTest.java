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
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе jpa для работы с жанрами")
@DataJpaTest
@Import({JpaGenreRepository.class})
public class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

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
        List<Genre> expectedGenres = getDbGenres();
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("Должен загружать жанр по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void shouldReturnCorrectGenreById(Genre testGenre) {
        Optional<Genre> actualGenre = jpaGenreRepository.findById(testGenre.getId());
        var expectedGenre = entityManager.find(Genre.class, testGenre.getId());
        assertThat(actualGenre).isPresent()
                .get()
                .isEqualTo(expectedGenre);

    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id)).toList();
    }
}
