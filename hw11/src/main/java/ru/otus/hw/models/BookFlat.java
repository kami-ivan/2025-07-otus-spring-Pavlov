package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class BookFlat {
    @Id
    private Long id;

    private String title;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private long authorId;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private long genreId;
}
