package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @CircuitBreaker(name = "commentService")
    @RateLimiter(name = "commentService")
    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    @CircuitBreaker(name = "commentService")
    @RateLimiter(name = "commentService")
    @Transactional(readOnly = true)
    public List<Comment> findAllByBookId(long bookId) {
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @CircuitBreaker(name = "commentService")
    @RateLimiter(name = "commentService")
    @Transactional
    public Comment insert(long bookId, String comment) {
        return commentRepository.save(new Comment(0, comment, getBook(bookId)));
    }

    @Override
    @CircuitBreaker(name = "commentService")
    @RateLimiter(name = "commentService")
    @Transactional
    public Comment update(long id, String comment) {
        return commentRepository
                .findById(id)
                .map(commentUpdated -> {
                    commentUpdated.setComment(comment);
                    return commentRepository.save(commentUpdated);
                })
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Override
    @CircuitBreaker(name = "commentService")
    @RateLimiter(name = "commentService")
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Book getBook(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
    }
}
