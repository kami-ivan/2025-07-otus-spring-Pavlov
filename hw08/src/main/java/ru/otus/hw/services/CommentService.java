package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(String id);

    List<Comment> findAllByBookId(String id);

    Comment insert(String id, String bookId, String text);

    Comment update(String id, String bookId, String text);

    void deleteById(String id);
}
