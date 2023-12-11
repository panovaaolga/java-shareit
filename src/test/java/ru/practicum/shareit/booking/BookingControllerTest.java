package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mvc;

    long bookerId = 1L;
    long bookingId = 0L;
    long itemId = 2L;
    private User booker = new User(bookerId, "name", "email@gmail.com");
    private Item item = new Item(itemId, "name", "desc", true,
            new User(2L, "n", "email@gmail.com"), null);
    private BookingDto bookingDto = new BookingDto(itemId,
            LocalDateTime.of(2024, Month.MARCH, 23, 14, 12),
            LocalDateTime.of(2024, Month.MARCH, 25, 14, 12));
    private Booking expectedBooking = new Booking(bookingId,
            LocalDateTime.of(2024, Month.MARCH, 23, 14, 12),
            LocalDateTime.of(2024, Month.MARCH, 25, 14, 12),
            Status.WAITING, booker, item);

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking(bookingDto, bookerId)).thenReturn(expectedBooking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", bookerId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBooking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.WAITING.toString())));
    }

    @Test
    void approveBooking() throws Exception { //переписать
        expectedBooking.setStatus(Status.APPROVED);
        when(bookingService.approveBooking(2L, bookingId, true)).thenReturn(expectedBooking);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                .header("X-Sharer-User-Id", bookerId)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBooking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED)));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBookingById(bookingId, bookerId)).thenReturn(expectedBooking);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                .header("X-Sharer-User-Id", bookerId)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBooking.getId()), Long.class));
    }

    @Test
    void getAllByUser() throws Exception {
        when(bookingService.getAllByBooker(bookerId, State.ALL.toString(), 0, 10))
                .thenReturn(List.of(expectedBooking));

        mvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", bookerId)
                .param("state", "ALL")
                .param("from", String.valueOf(0))
                .param("size", String.valueOf(10)))
                .andExpect(status().isOk());

    }

    @Test
    void getAllByOwner() throws Exception {
        expectedBooking.setStatus(Status.REJECTED);
        when(bookingService.getAllByBooker(2L, State.REJECTED.toString(), 0, 10))
                .thenReturn(List.of(expectedBooking));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 2L)
                        .param("state", "REJECTED")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk());
    }
}
