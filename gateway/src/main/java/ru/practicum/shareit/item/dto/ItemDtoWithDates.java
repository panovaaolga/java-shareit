package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.ValidationGroups;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithDates {
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
    private BookingDtoOutput lastBooking;
    private BookingDtoOutput nextBooking;
    private List<CommentDtoOutput> comments;
}
