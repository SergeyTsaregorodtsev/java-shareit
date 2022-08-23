package ru.practicum.shareit.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ItemRequest {
    private final int requestID;
    private final String description;
    private final int requesterID;
    private final LocalDateTime created;
}
