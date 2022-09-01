package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findBookingById(int bookingId);

    List<Booking> findBookingsByBookerOrderByStartDesc(int bookerId);

    List<Booking> findBookingsByBookerAndEndIsBeforeOrderByStartDesc(int bookerId, LocalDateTime date);

    List<Booking> findBookingsByBookerAndStartIsAfterOrderByStartDesc(int bookerId, LocalDateTime date);
}
