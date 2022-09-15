package ru.practicum.shareit.requests;

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

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

//import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    ItemRequestDto itemRequestDto;
    static String TEMPLATE = "/requests";
    static String HEADER = "X-Sharer-User-Id";
    static LocalDateTime CREATED = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto(1,"In dire need of hammer!", 1, CREATED);
    }

    @Test
    void addRequest() throws Exception {
        when(itemRequestService.addRequest(any(), anyInt())).thenReturn(itemRequestDto);
        mockMvc.perform(post(TEMPLATE)
                        .header(HEADER, 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId())))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requesterId", is(itemRequestDto.getRequesterId())));
        verify(itemRequestService, times(1)).addRequest(itemRequestDto,1);
    }
}