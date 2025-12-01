package ru.otus.hw.dto.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;


@Component
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getBook(), comment.getText());
    }

    public Comment toEntity(CommentDto commentDto) {
        return new Comment(commentDto.getId(), commentDto.getBook(), commentDto.getText());
    }
}
