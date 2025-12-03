package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private long id;

    @NotBlank(message = "name should not be blank")
    private String name;
}
