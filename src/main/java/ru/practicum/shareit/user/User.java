package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private Long userId; //или сделать сущность типа long?
    @NotBlank
    private String name;
    @Email
    private String email;
}
