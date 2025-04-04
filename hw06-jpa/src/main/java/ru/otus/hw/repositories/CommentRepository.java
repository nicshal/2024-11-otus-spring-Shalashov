package ru.otus.hw.repositories;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    List<Comment> findAllByBookId(long bookId);

    void deleteById(long id);
}
