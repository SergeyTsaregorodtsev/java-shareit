package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {
    static LocalDateTime time = LocalDateTime.now();

    @Test
    void toItemRequest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "Request desc.", 1, time);
        Object object = ItemRequestMapper.toItemRequest(itemRequestDto, 1);
        assertEquals(ItemRequest.class, object.getClass());
        ItemRequest request = (ItemRequest) object;
        assertEquals("Request desc.", request.getDescription());
        assertEquals(1, request.getRequesterId());
        long seconds = ChronoUnit.SECONDS.between(time, request.getCreated());
        assertTrue(seconds < 1);
    }

    @Test
    void toDto() {
        ItemRequest request = new ItemRequest(1,"Request desc.", 1, time);
        Object object = ItemRequestMapper.toDto(request);
        assertEquals(ItemRequestDto.class, object.getClass());
        ItemRequestDto requestDto = (ItemRequestDto) object;
        assertEquals("Request desc.", requestDto.getDescription());
        assertEquals(1, requestDto.getRequesterId());
        assertEquals(time, requestDto.getCreated());
    }
}