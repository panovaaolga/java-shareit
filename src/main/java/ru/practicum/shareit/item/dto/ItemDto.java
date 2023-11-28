package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.ValidationGroups;

import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    @Null(groups = {ValidationGroups.Create.class})
    private Long id;
    @NotBlank(groups = {ValidationGroups.Create.class})
    private String name;
    @NotBlank(groups = {ValidationGroups.Create.class})
    private String description;
    @NotNull(groups = {ValidationGroups.Create.class})
    private Boolean available;
}
