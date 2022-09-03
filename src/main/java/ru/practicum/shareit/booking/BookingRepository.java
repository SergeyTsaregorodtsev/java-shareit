package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findBookingById(int bookingId);

    List<Booking> findBookingsByBookerIdOrderByStartDesc(int bookerId);

    List<Booking> findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(
            int bookerId, LocalDateTime date);

    List<Booking> findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
            int bookerId, LocalDateTime date);

    List<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            int bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingsByBookerIdAndStatus(
            int bookerId, Booking.Status status);

    @Query("select booking from Booking booking " +
            "where booking.item.owner.id = ?1 " +
            "order by booking.start desc")
    List<Booking> findOwn(int userId);

    @Query("select booking from Booking booking " +
            "where booking.item.owner.id = ?1 and booking.start > ?2 " +
            "order by booking.start desc")
    List<Booking> findOwnFuture(int userId, LocalDateTime date);

    @Query("select booking from Booking booking " +
            "where booking.item.owner.id = ?1 and booking.end < ?2 " +
            "order by booking.start desc")
    List<Booking> findOwnPast(int userId, LocalDateTime date);

    @Query("select booking from Booking booking " +
            "where booking.item.owner.id = ?1 " +
            "and booking.start < ?2 and booking.end > ?2 " +
            "order by booking.start desc")
    List<Booking> findOwnCurrent(int userId, LocalDateTime date);

    @Query("select booking from Booking booking " +
            "where booking.item.id = ?1 and booking.end < ?2 " +
            "order by booking.end desc")
    List<Booking> findPastBookings(int itemId, LocalDateTime date);

    @Query("select booking from Booking booking " +
            "where booking.item.id = ?1 and booking.start > ?2 " +
            "order by booking.start")
    List<Booking> findFutureBookings(int itemId, LocalDateTime date);

    @Query("select booking from Booking booking " +
            "where booking.item.owner.id = ?1 " +
            "and booking.status = ?2")
    List<Booking> findOwnByStatus(int itemId, Booking.Status status);

    @Query("select booking from Booking booking " +
            "where booking.item.id = ?1 " +
            "and booking.booker.id = ?2 " +
            "and booking.end < ?3")
    List<Booking> findBookingsByItemAndBooker(int itemId, int userId, LocalDateTime date);
}