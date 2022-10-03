package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    int id;
    @NotBlank
    String text;
    String authorName;
    LocalDateTime created;
}
