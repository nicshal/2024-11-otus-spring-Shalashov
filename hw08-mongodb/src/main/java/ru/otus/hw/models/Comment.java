package ru.otus.hw.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@EqualsAndHashCode(exclude = {"book"})
@ToString(exclude = {"book"})
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "comments")
public class Comment {

    @Id
    private String id;

    private String comment;

    @DBRef(lazy = true)
    private Book book;
}
