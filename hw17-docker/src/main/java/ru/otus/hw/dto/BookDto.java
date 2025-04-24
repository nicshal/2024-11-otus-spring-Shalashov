package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private long id;

    @NotBlank(message = "#{title-field-should-not-be-blank}")
    @Size(min = 2, max = 255, message = "#{title-field-should-be-expected-size}")
    private String title;

    @NotNull(message = "#{author-should-not-be-blank}")
    private AuthorDto author;

    @NotNull(message = "#{genre-should-not-be-blank}")
    private GenreDto genre;

    public Book toDomainObject() {
        return new Book(id, title, author.toDomainObject(), genre.toDomainObject());
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(),
                book.getTitle(),
                AuthorDto.fromDomainObject(book.getAuthor()),
                GenreDto.fromDomainObject(book.getGenre()));
    }
}
