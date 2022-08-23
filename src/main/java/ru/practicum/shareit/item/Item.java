package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.feedback.Feedback;
import ru.practicum.shareit.requests.ItemRequest;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Item {
    private int id;
    @NonNull @NotBlank
    private String name;
    @NonNull @NotBlank
    private String description;
    private int ownerId;
    @NonNull
    private boolean available;
    private ItemRequest request;
    private List<Feedback> feedbacks;
}
