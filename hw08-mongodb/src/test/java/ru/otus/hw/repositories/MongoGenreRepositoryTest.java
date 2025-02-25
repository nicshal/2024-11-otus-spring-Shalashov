package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе jpa для работы с жанрами")
@DataMongoTest
public class MongoGenreRepositoryTest {

    private static final String TEST_GENRE_ID = "1";

    @Autowired
    private GenreRepository genreRepository;

    private List<Genre> genres;

    @BeforeEach
    void init() {
        genres = getDbGenres();
    }

    @DisplayName("Должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        List<Genre> actualGenres = genreRepository.findAll();
        List<Genre> expectedGenres = genres;
        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("Должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var actualGenre = genreRepository.findById(TEST_GENRE_ID);
        var expectedGenre = genres.get(0);
        assertThat(actualGenre)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(String.valueOf(id), "Genre_" + id)).toList();
    }
}