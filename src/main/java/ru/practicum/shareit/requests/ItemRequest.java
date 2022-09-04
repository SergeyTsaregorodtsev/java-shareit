package ru.practicum.shareit.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "requester_id", nullable = false)
    private int requesterId;
}
