package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(long id);

    @Query("""
           select c from Comment c
           left join c.book b
           where b.id = :id
           """)
    List<Comment> findAllByBookId(@Param("id") long id);
}
