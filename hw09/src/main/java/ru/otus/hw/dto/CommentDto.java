package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;

    @NotNull(message = "book must be selected")
    private Book book;

    @NotBlank(message = "text should not be blank")
    private String text;
}
