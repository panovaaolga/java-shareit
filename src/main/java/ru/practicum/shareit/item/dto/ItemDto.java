package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    @NotBlank
    private String name;
    @Size(min = 3, max = 200)
    private String description;
    private boolean available;
}
