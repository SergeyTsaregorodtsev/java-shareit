package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name", nullable = false, length = 255)
    String name;

    @Column(name = "description", nullable = false, length = 255)
    String description;

    @ManyToOne @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @Column(name = "is_available", nullable = false)
    boolean available;

    @Column(name = "request_id")
    int requestId;
}