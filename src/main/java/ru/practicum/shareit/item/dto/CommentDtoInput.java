package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentDtoInput {
    @NotBlank
    @Size(max = 600)
    private String text;
}
