package ru.practicum.shareit.feedback;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor

public class Feedback {
    private int userID;
    private String comment;
}
