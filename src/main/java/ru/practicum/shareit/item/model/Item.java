package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
public class Item {
    private Long itemId;
    @NotBlank
    private String name;
    @Size(min = 3, max = 600)
    private String description;
    @NotNull
    private Boolean available;
    @NotNull
    private User owner;
    private ItemRequest request;

}
