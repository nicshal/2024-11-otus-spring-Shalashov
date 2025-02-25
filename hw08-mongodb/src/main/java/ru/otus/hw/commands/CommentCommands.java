package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.converters.CommentConverter;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    // cbid 2
    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(String id) {
        Optional<Comment> comment = commentService.findById(id);
        if (comment.isEmpty()) {
            return "Comment with id %s not found".formatted(id);
        }
        return commentConverter.commentToString(comment.get());
    }

    // cins 1 text_comment
    @ShellMethod(value = "Insert new comment for book", key = "cins")
    public String insertComment(String bookId, String text) {
        Comment comment = commentService.insert(bookId, text);
        return commentConverter.commentToString(comment);
    }

    // cupd 1 text_comment
    @ShellMethod(value = "Update comment by ID", key = "cupd")
    public String updateComment(String id, String text) {
        Comment savedComment = commentService.update(id, text);
        return commentConverter.commentToString(savedComment);
    }

    // acbbid 4
    @ShellMethod(value = "Find all comments by book id", key = "acbbid")
    public String findAllCommentsByBookId(String bookId) {
        List<Comment> comments = commentService.findAllByBookId(bookId);
        return commentConverter.commentsToString(comments);
    }

    // cdel 4
    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public String deleteCommentById(String id) {
        commentService.deleteById(id);
        return "Comment with %s deleted".formatted(id);
    }
}
