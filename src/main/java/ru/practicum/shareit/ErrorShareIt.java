package ru.practicum.shareit;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ErrorShareIt {
    private final HttpStatus httpStatus;
    private final String message;
}
