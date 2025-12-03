package ru.otus.hw.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private Long id;

    @NotBlank(message = "name should not be blank")
    private String name;
}
