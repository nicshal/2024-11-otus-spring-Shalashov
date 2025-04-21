package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тестирование контроллера книг")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = getDbBooks();
    }

    @DisplayName("Должен вернуть представление со списком книг")
    @Test
    public void shouldReturnBooksListView() throws Exception {
        given(bookService.findAll())
                .willReturn(dbBooks);
        this.mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(content().string(containsString(dbBooks.get(0).getTitle())))
                .andExpect(content().string(containsString(dbBooks.get(1).getTitle())))
                .andExpect(content().string(containsString(dbBooks.get(2).getTitle())));
        verify(bookService, times(1)).findAll();
    }

    @DisplayName("Должен удалить книгу и сделать редирект на /books")
    @Test
    public void shouldDeleteBookAndRedirected() throws Exception {
        long bookId = 1L;
        doNothing().when(bookService).deleteById(bookId);
        mvc.perform(post("/books/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1)).deleteById(1);
    }

    @DisplayName("Должен изменить книгу и сделать редирект на /books")
    @Test
    public void shouldEditBookAndRedirected() throws Exception {
        given(bookService.update(anyLong(),
                anyString(),
                anyLong(),
                anyLong())).willReturn(dbBooks.get(0));
        var testBook = BookDto.builder()
                .title("Change_Title")
                .genre(GenreDto.fromDomainObject(dbBooks.get(0).getGenre()))
                .author(AuthorDto.fromDomainObject(dbBooks.get(0).getAuthor()))
                .build();
        mvc.perform(post("/books/edit")
                        .flashAttr("book", testBook)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1)).update(anyLong(),
                anyString(),
                anyLong(),
                anyLong());
    }

    @DisplayName("Должен сохранить книгу и сделать редирект на /books")
    @Test
    public void shouldInsertBookAndRedirected() throws Exception {
        given(bookService.insert(anyString(),
                anyLong(),
                anyLong())).willReturn(dbBooks.get(0));
        var testBook = BookDto.builder()
                .title("New_Book_Title")
                .genre(GenreDto.fromDomainObject(dbBooks.get(1).getGenre()))
                .author(AuthorDto.fromDomainObject(dbBooks.get(1).getAuthor()))
                .build();
        mvc.perform(post("/books/insert")
                        .flashAttr("book", testBook)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1)).insert(anyString(),
                anyLong(),
                anyLong());
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