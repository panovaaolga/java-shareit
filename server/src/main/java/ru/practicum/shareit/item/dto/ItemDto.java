package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.ValidationGroups;

import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    @Null(groups = {ValidationGroups.Create.class})
    private Long id;
    @NotBlank(groups = {ValidationGroups.Create.class})
    @Size(max = 255)
    private String name;
    @NotBlank(groups = {ValidationGroups.Create.class})
    @Size(max = 600)
    private String description;
    @NotNull(groups = {ValidationGroups.Create.class})
    private Boolean available;
    private Long requestId;
}
