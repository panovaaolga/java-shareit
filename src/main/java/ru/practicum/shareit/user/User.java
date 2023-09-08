package ru.practicum.shareit.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
public class User {
    long userId;
    @NotBlank
    String name;
    @Email
    String email;
}
