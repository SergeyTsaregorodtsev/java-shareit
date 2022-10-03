package ru.practicum.shareit.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "description", nullable = false, length = 255)
    String description;

    @Column(name = "requester_id", nullable = false)
    int requesterId;

    @Column(name = "created", nullable = false)
    LocalDateTime created;
}