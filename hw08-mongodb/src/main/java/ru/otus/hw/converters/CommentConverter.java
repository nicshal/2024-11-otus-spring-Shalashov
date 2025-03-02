package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(Comment comment) {
        return "Id: %s, comment: %s".formatted(
                comment.getId(),
                comment.getComment());
    }

    public String commentsToString(List<Comment> comments) {
        return comments == null ? " " : comments.stream().
                map(c -> "Id: %s, Text: %s".formatted(c.getId(), c.getComment())).
                collect(Collectors.joining(","));
    }
}
