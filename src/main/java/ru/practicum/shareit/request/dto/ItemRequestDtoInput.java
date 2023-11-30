package ru.practicum.shareit.request.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ItemRequestDtoInput {
    @NotBlank
    @Size(max = 600)
    private String description;
}
