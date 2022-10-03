package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingApproveStatus {
    TRUE,
    FALSE;

    public static Optional<BookingApproveStatus> from(String approveStatus) {
        for (BookingApproveStatus status : values()) {
            if (status.name().equalsIgnoreCase(approveStatus)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
