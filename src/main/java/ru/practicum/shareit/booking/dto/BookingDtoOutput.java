package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class BookingDtoOutput {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private User booker;
    private Item item;
}
