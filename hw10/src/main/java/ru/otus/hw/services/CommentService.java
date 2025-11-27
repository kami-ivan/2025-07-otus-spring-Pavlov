package ru.otus.hw.services;

import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(long id);

    List<CommentDto> findAllByBookId(long bookId);

    Comment insert(long bookId, String text);

    Comment update(long id, long bookId, String text);

    void deleteById(long id);
}
