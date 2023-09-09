package ru.practicum.shareit.item.model;

import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long itemId;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private boolean available;
    private User owner;

    public Item (String name, String description, boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
