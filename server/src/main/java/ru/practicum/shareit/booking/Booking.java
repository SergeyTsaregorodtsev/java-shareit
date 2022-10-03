package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @ManyToOne @JoinColumn(name = "booker_id", nullable = false)
    User booker;

    @Column(name = "start_date", nullable = false)
    LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    LocalDateTime end;

    @Column(name = "status") @Enumerated(EnumType.STRING)
   Status status;

    public enum Status {
        WAITING, APPROVED, REJECTED, CANCELED
    }
}