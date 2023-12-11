package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @MockBean
    ItemServiceImpl itemService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    long userId = 1L;
    long itemId = 2L;
    private Item item = new Item(itemId, "name", "desc", true,
            new User(2L, "n", "email@gmail.com"), null);
    private ItemDto itemDto = new ItemDto(null, item.getName(), item.getDescription(), true, null);
    private ItemDtoWithDates itemDtoWithDates = ItemMapper
            .mapToItemDtoWithDates(item, null, null, List.of());
    private CommentDtoOutput commentDtoOutput = new CommentDtoOutput(1L, "text", "name", Instant.now());

    @Test
    void createItem() throws Exception { //не возвращает ItemDto
        when(itemService.save(ItemMapper.mapToItemDto(item), userId)).thenReturn(itemDto);

        mvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId)));
    }

    @Test
    void updateItem() throws Exception { //не возвращает ItemDto
        when(itemService.update(userId, itemId, ItemMapper.mapToItemDto(item))).thenReturn(new ItemDto(itemId,
                "new name", item.getDescription(), item.getAvailable(), null));

        mvc.perform(patch("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(new ItemDto(itemId,
                        "new name", item.getDescription(), item.getAvailable(), null)))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("new name")));
    }

    @Test
    void getAllItemsOfUser() throws Exception {
        when(itemService.getAllItemsByUser(userId, 0, 10)).thenReturn(List.of());

        mvc.perform(get("/items")
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItem(itemId, userId)).thenReturn(itemDtoWithDates);

        mvc.perform(get("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId), Long.class))
                .andExpect(jsonPath("$.description", is("desc")));
    }

    @Test
    void getSearchedItems() throws Exception {
        when(itemService.getSearchedItems(anyString())).thenReturn(List.of());

        mvc.perform(get("/items/search")
                .param("text", anyString()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(new CommentDtoInput("text"), userId, itemId)).thenReturn(commentDtoOutput);

        mvc.perform(post("/items/{itemId}/comment", itemId)
                .header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(new CommentDtoInput("text")))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.text", is("text")));
    }
}
