package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    ItemDto itemDto;
    UserDto userDto;
    BookingDto bookingDto;
    BookingDtoOut bookingDtoOut;
    static String HEADER = "X-Sharer-User-Id";
    static LocalDateTime START = LocalDateTime.now();
    static LocalDateTime END = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1,"John Doe", "john.doe@mail.com");
        itemDto = new ItemDto(1, "Hammer", "John Doe's hammer",
                true, null, null, null);
        bookingDto = new BookingDto(1,1, START, END, Booking.Status.WAITING);
        bookingDtoOut = new BookingDtoOut(1, itemDto, userDto, START, END, Booking.Status.WAITING);
    }

    @Test
    void add() throws Exception {
        when(bookingService.addBooking(any(), anyInt())).thenReturn(bookingDtoOut);
        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header(HEADER, 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is(bookingDtoOut.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoOut.getBooker().getName())));
        verify(bookingService, times(1)).addBooking(bookingDto,1);
    }

    @Test
    void approve() throws Exception {
        when(bookingService.approve(anyInt(), anyInt(), anyString())).thenReturn(bookingDtoOut);
        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}?approved={approved}",
                                1, "true")
                .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is(bookingDtoOut.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoOut.getBooker().getName())));
        verify(bookingService, times(1)).approve(1, 1, "true");
    }

    @Test
    void get() throws Exception {
        when(bookingService.get(anyInt(), anyInt())).thenReturn(bookingDtoOut);
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", 1)
                .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is(bookingDtoOut.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoOut.getBooker().getName())));
        verify(bookingService, times(1)).get(1, 1);
    }

    @Test
    void getAll() throws Exception {
        when(bookingService.getAll(anyInt(), anyString(), anyBoolean(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings?state={state}&from={from}&size={size}",
                                "WAITING", 1, 1)
                .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1))
                .getAll(1, "WAITING", false, 1, 1);
    }

    @Test
    void getOwn() throws Exception {
        when(bookingService.getAll(anyInt(), anyString(), anyBoolean(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner?state={state}&from={from}&size={size}",
                                "WAITING", 1, 1)
                        .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1))
                .getAll(1, "WAITING", true, 1, 1);
    }
}