package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDtoShort;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    BookingDtoShort lastBooking;
    ItemDto itemDto;
    static String TEMPLATE = "/items";
    static String HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        lastBooking = new BookingDtoShort(1, 1, start, end, Booking.Status.WAITING);
        itemDto = new ItemDto(1,"Item1", "Item1Desc",
                true, 0, lastBooking, null);
    }

    @Test
    void add() throws Exception {
        when(itemService.addItem(any(), anyInt())).thenReturn(itemDto);
        mockMvc.perform(MockMvcRequestBuilders.post(TEMPLATE)
                        .header(HEADER,1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
        verify(itemService, times(1)).addItem(itemDto, 1);
    }

    @Test
    void update() throws Exception {
        when(itemService.updateItem(any(), anyInt(), anyInt())).thenReturn(itemDto);
        mockMvc.perform(MockMvcRequestBuilders.patch(TEMPLATE + "/1")
                        .header(HEADER,1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
        verify(itemService, times(1)).updateItem(itemDto, 1, 1);
    }

    @Test
    void getAll() throws Exception {
        when(itemService.getItems(anyInt(), anyInt(), anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get(TEMPLATE + "?from={from}&size={size}", 1, 1)
                        .header(HEADER,1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).getItems(1,1,1);
    }

    /*@Test
    void getItem() throws Exception {

    }

    @Test
    void search() throws Exception {

    }

    @Test
    void addComment() throws Exception {

    }

    @Test
    void getComment() throws Exception {

    }*/
}