package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.services.BookService;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/books")
    public String findAllBook(Model model) {
        List<BookDto> books = bookService.findAll().stream()
                .map(BookDto::fromDomainObject).toList();
        model.addAttribute("books", books);
        return "book-list";
    }

    @PostMapping("/books/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    public String updateBook(@PathVariable long id, Model model) {
        BookDto book = BookDto.fromDomainObject(bookService.findById(id).orElseThrow(EntityNotFoundException::new));
        List<AuthorDto> authors = authorService.findAll().stream()
                .map(AuthorDto::fromDomainObject).toList();
        List<GenreDto> genres = genreService.findAll().stream()
                .map(GenreDto::fromDomainObject).toList();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("book", book);
        return "book-edit";
    }

    @PostMapping(value = "/books/edit")
    public String saveBook(@ModelAttribute("book") BookDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "book-edit";
        }
        bookService.update(dto.getId(), dto.getTitle(), dto.getAuthor().getId(), dto.getGenre().getId());
        return "redirect:/books";
    }

    @GetMapping("/books/insert")
    public String insertBook(Model model) {
        BookDto book = BookDto.builder().title("--> Enter title").build();
        List<AuthorDto> authors = authorService.findAll().stream()
                .map(AuthorDto::fromDomainObject).toList();
        List<GenreDto> genres = genreService.findAll().stream()
                .map(GenreDto::fromDomainObject).toList();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book-insert";
    }

    @PostMapping("/books/insert")
    public String saveNewBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "book-insert";
        }
        bookService.insert(bookDto.getTitle(), bookDto.getAuthor().getId(), bookDto.getGenre().getId());
        return "redirect:/books";
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    private ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body("%s. Maybe book already was deleted".formatted(ex.getMessage()));
    }
}