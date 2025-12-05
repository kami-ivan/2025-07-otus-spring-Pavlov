package ru.otus.hw.rest.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorDto {

    private String code;

    private String text;
}
