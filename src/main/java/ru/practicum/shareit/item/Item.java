package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.feedback.Feedback;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 255)
    @NonNull @NotBlank
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    @NonNull @NotBlank
    private String description;

    @Column(name = "owner_id", nullable = false)
    private int ownerId;

    @Column(name = "is_available", nullable = false)
    private boolean available;

    @Column(name = "request_id")
    private int requestId;

    @Transient
    private List<Feedback> feedbacks;
}
