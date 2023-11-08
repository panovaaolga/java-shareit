package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {

    @NotBlank(groups = {ValidationGroups.Create.class})
    private String name;
    @Email(groups = {ValidationGroups.Create.class})
    @NotBlank(groups = {ValidationGroups.Create.class})
    private String email;
}
