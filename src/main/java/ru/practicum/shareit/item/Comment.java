package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NonNull
    @NotBlank @Column(name = "text", nullable = false, length = 255)
    String text;

    @NonNull
    @ManyToOne @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @NonNull
    @ManyToOne @JoinColumn(name = "author_id", nullable = false)
    User author;

    @NonNull
    @Column(name = "created")
    LocalDateTime created;
}