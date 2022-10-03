package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingApproveStatus;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient client;

	@PostMapping
	public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") int userId,
								@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return client.add(userId, requestDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") int userId,
										 @PathVariable int bookingId,
										 @RequestParam(value = "approved") String approved) {
		BookingApproveStatus status = BookingApproveStatus.from(approved)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS"));
		log.trace("Approve booking ID {} to status {}.", bookingId, approved);
		return client.approve(bookingId, userId, status);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") int userId,
								@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return client.get(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") int userId,
										 @RequestParam(name = "state", defaultValue = "all") String stateParam,
										 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
										 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS"));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return client.getAll(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwn(@RequestHeader("X-Sharer-User-Id") int userId,
									  	 @RequestParam(value = "state", defaultValue = "ALL") String stateParam,
										 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
										 @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS"));
		log.trace("Получен GET-запрос бронирований на вещи пользователя ID {}, статус - {}.", userId, state);
		return client.getOwn(userId, state, from, size);
	}
}