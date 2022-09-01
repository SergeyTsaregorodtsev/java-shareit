package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
    @Column(name = "item_id", nullable = false)
    private int item;

    @Column(name = "booker_id", nullable = false)
    private int booker;

    @NonNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @NonNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

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
