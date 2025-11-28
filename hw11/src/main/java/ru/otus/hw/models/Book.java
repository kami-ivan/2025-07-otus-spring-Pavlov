package ru.otus.hw.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("books")
public class Book {
    @Id
    private Long id;

    private String title;

    @Transient
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Author author;

    @Transient
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Genre genre;

    @Column("author_id")
    private Long authorId;

    @Column("genre_id")
    private Long genreId;

    public Book(long id, String title, Long authorId, Long genreId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
    }

    public BookFlat toBookFlat() {
        return new BookFlat(id, title, authorId, genreId);
    }
}
