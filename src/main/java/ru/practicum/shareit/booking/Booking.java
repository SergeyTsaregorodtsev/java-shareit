package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @NonNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @NonNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @NonNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    enum Status {
        WAITING,    // ожидает одобрения
        APPROVED,   // подтверждено владельцем
        REJECTED,   // отклонено владельцем
        CANCELED    // отменено создателем
    }
}
