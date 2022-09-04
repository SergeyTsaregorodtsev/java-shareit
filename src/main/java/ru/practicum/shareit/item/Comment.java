package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @NotBlank
    @Column(name = "text", nullable = false, length = 255)
    String text;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NonNull
    @Column(name = "created")
    private LocalDateTime created;
}
