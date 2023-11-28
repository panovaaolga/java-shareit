package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentDtoOutput {
    private Long id;
    private String text;
    private String authorName;
    private Instant created;
}
