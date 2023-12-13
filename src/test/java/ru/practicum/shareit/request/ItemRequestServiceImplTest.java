package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemRequestServiceImplTest {

    @Mock
    RequestRepository requestRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemRequestServiceImpl requestService;

    @Captor
    private ArgumentCaptor<ItemRequest> requestArgumentCaptor;

    long requestId = 1L;
    long authorId = 1L;
    LocalDateTime now = LocalDateTime.now();
    private User author = new User(authorId, "name", "email@gmail.com");
    private ItemRequestDtoInput requestDtoInput = new ItemRequestDtoInput("some description");
    private ItemRequest expectedRequest = new ItemRequest(requestId, "some description",
            now, author);
    private ItemRequest secondExpectedRequest = new ItemRequest(2L, "some description",
            now, author);
    private ItemRequestDto itemRequestDto = new ItemRequestDto(requestId, "some description", now, List.of());

    @Test
    void createRequest_whenDataCorrect_thenReturn() {
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(requestRepository.save(any())).thenReturn(expectedRequest);

        ItemRequestDto savedItemRequest = requestService.createRequest(authorId, requestDtoInput);

        assertEquals(expectedRequest.getDescription(), savedItemRequest.getDescription());
    }

    @Test
    void createRequest_whenUserNotFound_thenThrow() {
        when(userRepository.findById(authorId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> requestService.createRequest(authorId, requestDtoInput));
    }

    @Test
    void getRequestById_whenRequestFound_thenReturn() {
        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(expectedRequest));
        when(itemRepository.findByRequestIdOrderByCreated(requestId)).thenReturn(List.of());

        ItemRequestDto actualRequest = requestService.getRequestById(authorId, requestId);

        assertEquals(expectedRequest.getId(), actualRequest.getId(), "Id should be the same");
        assertEquals(requestDtoInput.getDescription(), actualRequest.getDescription());

    }

    @Test
    void getRequestById_whenRequestNotFound_thenThrow() {
        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findById(requestId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> requestService.getRequestById(authorId, requestId));
    }

    @Test
    void getRequestById_whenUserNotFound_thenThrow() {
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getRequestById(authorId, requestId));
    }

    @Test
    void getRequestsByOwner_whenListEmpty_thenReturnEmpty() {
        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdOrderByCreatedDesc(authorId)).thenReturn(List.of());

        assertEquals(0, requestService.getRequestsByOwner(authorId).size());
    }

    @Test
    void getRequestsByOwner_whenUserNotFound_thenThrow() {
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getRequestsByOwner(authorId));
    }

    @Test
    void getRequestsByOwner_whenListNotEmpty_thenReturnList() {
        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdOrderByCreatedDesc(authorId))
                .thenReturn(List.of(expectedRequest, secondExpectedRequest));

        List<ItemRequestDto> requestDtos = requestService.getRequestsByOwner(authorId);
        assertEquals(2, requestDtos.size());
        assertEquals("some description", requestDtos.get(0).getDescription());
    }


    @Test
    void getAllRequests_whenArgsCorrect_thenReturn() {
        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdNot(authorId,
                PageRequest.of(0, 10, Sort.by("created").descending()))).thenReturn(Page.empty());

        assertEquals(0, requestService.getAllRequests(authorId, 0, 10).size());
    }

    @Test
    void getAllRequests_whenArgsNotCorrect_thenThrow() {
        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdNot(anyLong(), any()))
                .thenThrow(ValidationException.class);

        assertThrows(ValidationException.class, () -> requestService.getAllRequests(authorId, -1, 10));
    }

    @Test
    void getAllRequests_whenArgsCorrectNotEmpty_thenReturn() {
        PageImpl<ItemRequest> itemRequests = new PageImpl<>(List.of(expectedRequest, secondExpectedRequest));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdNot(authorId,
                PageRequest.of(0, 10, Sort.by("created").descending())))
                .thenReturn(itemRequests);


        assertEquals(2, requestService.getAllRequests(authorId, 0, 10).size());
    }

    @Test
    void getAllRequests_whenUserNotFound_thenThrow() {
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getAllRequests(authorId, 0, 10));
    }
}
