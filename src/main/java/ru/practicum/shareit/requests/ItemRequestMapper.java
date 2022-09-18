package ru.practicum.shareit.requests;

import ru.practicum.shareit.exceptions.BadParameterException;

import java.time.LocalDateTime;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto dto, int userId) {
         String description = dto.getDescription();
         if (description == null || description.isBlank()) {
             throw new BadParameterException("Отсутствует описание в запросе.");
         }
         ItemRequest request = new ItemRequest();
         request.setDescription(description);
         request.setRequesterId(userId);
         request.setCreated(LocalDateTime.now());
         return request;
    }

    public static ItemRequestDto toDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequesterId(),
                request.getCreated());
    }
}