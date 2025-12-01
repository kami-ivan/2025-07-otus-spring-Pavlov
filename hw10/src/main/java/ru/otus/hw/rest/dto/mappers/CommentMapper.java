package ru.otus.hw.rest.dto.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.rest.dto.CommentDto;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final BookMapper bookMapper;

    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        return new CommentDto(comment.getId(),
                comment.getBook() != null ? bookMapper.toDto(comment.getBook()) : null,
                comment.getText());
    }

    public Comment toEntity(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }

        return new Comment(commentDto.getId(),
                commentDto.getBook() != null ? bookMapper.toEntity(commentDto.getBook()) : null,
                commentDto.getText());
    }
}
