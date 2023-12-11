package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    long requestId = 1L;
    long userId = 1L;
    private User author = new User(userId, "name", "email@gmail.com");
    private ItemRequestDtoInput requestDto = new ItemRequestDtoInput("some description");
    private ItemRequest expectedRequest = new ItemRequest(requestId, "some description",
            LocalDateTime.now(), author);

    @Test
    void createRequest() throws Exception { //переписать
        when(itemRequestService.createRequest(userId, requestDto))
                .thenReturn(RequestMapper.mapToRequestDtoOutput(expectedRequest, List.of()));

        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(requestDto))
                .header("X-Sharer-User-Id", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestId), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }

    @Test
    void getById_whenUserCorrect_thenReturn() throws Exception {
        when(itemRequestService.getRequestById(userId, requestId))
                .thenReturn(RequestMapper.mapToRequestDtoOutput(expectedRequest, new ArrayList<>()));

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestId), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }

    @Test
    void getAllRequests_whenArgsCorrect_thenReturn() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(new ItemRequestDto()));

        mvc.perform(get("/requests/all")
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByUser() throws Exception {
        when(itemRequestService.getRequestsByOwner(userId))
                .thenReturn(List.of(RequestMapper.mapToRequestDtoOutput(expectedRequest, List.of())));

        mvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }


}
