package ru.practicum.shareit.item.model;

import org.springframework.boot.context.properties.bind.DefaultValue;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
public class Item {
    long itemId;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    boolean available;
    User owner;

    public Item (String name, String description, boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
