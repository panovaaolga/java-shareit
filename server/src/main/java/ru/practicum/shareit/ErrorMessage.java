package ru.practicum.shareit;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ErrorMessage {
    private final HttpStatus status;
    private final String error;
}
