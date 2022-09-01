package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findBookingById(int bookingId);
}
