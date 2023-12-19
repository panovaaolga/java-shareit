package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.ValidationGroups;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(groups = {ValidationGroups.Create.class})
    @Size(max = 255)
    private String name;
    @Email(groups = {ValidationGroups.Create.class})
    @NotBlank(groups = {ValidationGroups.Create.class})
    @Size(max = 255)
    private String email;
}
