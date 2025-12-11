package ru.otus.hw.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Comment;

@Data
@AllArgsConstructor
public class CommentDto {
    private long id;

    @NotNull(message = "book must be selected")
    private BookDto book;

    @NotBlank(message = "text should not be blank")
    private String text;

    public Comment toDomainObject() {
        return new Comment(id, book.toDomainObject(), text);
    }

    public static CommentDto fromDomainObject(Comment comment) {
        return new CommentDto(comment.getId(),
                BookDto.fromDomainObject(comment.getBook()),
                comment.getText());
    }
}
