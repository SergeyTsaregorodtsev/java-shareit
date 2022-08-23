package ru.practicum.shareit.feedback;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Feedback {
    private final int userID;
    private final String comment;
}
