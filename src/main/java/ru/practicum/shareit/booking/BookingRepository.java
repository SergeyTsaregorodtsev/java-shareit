package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findBookingsByBookerIdOrderByStartDesc(int bookerId, Pageable page);

    Page<Booking> findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(
            int bookerId, LocalDateTime date, Pageable page);

    Page<Booking> findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
            int bookerId, LocalDateTime date, Pageable page);

    Page<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            int bookerId, LocalDateTime start, LocalDateTime end, Pageable page);

    Page<Booking> findBookingsByBookerIdAndStatus(
            int bookerId, Booking.Status status, Pageable page);

    @Query("select booking from Booking booking " +
            "where booking.item.owner.id = ?1 " +
            "order by booking.start desc")
    Page<Booking> findOwn(int userId, Pageable page);

    @Query("select booking from Booking booking " +
            "where booking.item.owner.id = ?1 and booking.start > ?2 " +
            "order by booking.start desc")
    Page<Booking> findOwnFuture(int userId, LocalDateTime date, Pageable page);

    @Query("select booking from Booking booking " +
            "where booking.item.owner.id = ?1 and booking.end < ?2 " +
            "order by booking.start desc")
    Page<Booking> findOwnPast(int userId, LocalDateTime date, Pageable page);

    @Query("select booking from Booking booking " +
            "where booking.item.owner.id = ?1 " +
            "and booking.start < ?2 and booking.end > ?2 " +
            "order by booking.start desc")
    Page<Booking> findOwnCurrent(int userId, LocalDateTime date, Pageable page);

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
    Page<Booking> findOwnByStatus(int itemId, Booking.Status status, Pageable page);

    @Query("select booking from Booking booking " +
            "where booking.item.id = ?1 " +
            "and booking.booker.id = ?2 " +
            "and booking.end < ?3")
    List<Booking> findBookingsByItemAndBooker(int itemId, int userId, LocalDateTime date);
}