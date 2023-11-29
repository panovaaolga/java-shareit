package ru.practicum.shareit.request.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ItemRequestDtoInput {
    @NotBlank
    @Size(max = 600)
    private String description;
}
