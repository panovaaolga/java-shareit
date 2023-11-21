package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.ValidationGroups;
import ru.practicum.shareit.booking.Booking;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class ItemDtoWithDates {
    @Null(groups = {ValidationGroups.Create.class})
    private Long id;
    @NotBlank(groups = {ValidationGroups.Create.class})
    private String name;
    @NotBlank(groups = {ValidationGroups.Create.class})
    @Size(min = 3, max = 600)
    private String description;
    @NotNull(groups = {ValidationGroups.Create.class})
    private Boolean available;
    private Booking lastBooking;
    private Booking nextBooking;
}
