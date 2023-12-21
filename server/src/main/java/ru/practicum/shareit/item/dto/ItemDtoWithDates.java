package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithDates {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoOutput lastBooking;
    private BookingDtoOutput nextBooking;
    private List<CommentDtoOutput> comments;
}
