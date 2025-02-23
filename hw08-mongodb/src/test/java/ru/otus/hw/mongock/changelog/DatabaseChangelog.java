package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private static final String AUTHOR = "shalashov";

    private final List<Genre> genres = List.of(
            Genre.builder().id("1").name("Genre_1").build(),
            Genre.builder().id("2").name("Genre_2").build(),
            Genre.builder().id("3").name("Genre_3").build()
    );

    private final List<Author> authors = List.of(
            Author.builder().id("1").fullName("Author_1").build(),
            Author.builder().id("2").fullName("Author_2").build(),
            Author.builder().id("3").fullName("Author_3").build()
    );

    private final List<Book> books = List.of(
            Book.builder().id("1").title("BookTitle_1").author(authors.get(0)).genre(genres.get(0)).build(),
            Book.builder().id("2").title("BookTitle_2").author(authors.get(1)).genre(genres.get(1)).build(),
            Book.builder().id("3").title("BookTitle_3").author(authors.get(2)).genre(genres.get(2)).build()
    );

    private final List<Comment> comments = List.of(
            Comment.builder().id("1").comment("Comment_1").book(books.get(0)).build(),
            Comment.builder().id("2").comment("Comment_2").book(books.get(1)).build(),
            Comment.builder().id("3").comment("Comment_3").book(books.get(2)).build()
    );

    @ChangeSet(order = "001", id = "dropDb", runAlways = true, author = AUTHOR)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }


    @ChangeSet(order = "002", id = "insertGenres", author = AUTHOR)
    public void insertGenres(GenreRepository repository) {
        repository.saveAll(genres);
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = AUTHOR)
    public void insertAuthors(AuthorRepository repository) {
        repository.saveAll(authors);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = AUTHOR)
    public void insertBooks(BookRepository repository) {
        repository.saveAll(books);
    }

    @ChangeSet(order = "005", id = "insertComments", author = AUTHOR)
    public void insertComments(CommentRepository repository) {
        repository.saveAll(comments);
    }
}
