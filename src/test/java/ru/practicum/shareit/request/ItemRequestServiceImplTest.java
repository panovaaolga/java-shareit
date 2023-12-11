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
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
    private ArgumentCaptor<ItemRequest> userArgumentCaptor;

    long requestId = 1L;
    long userId = 1L;
    LocalDateTime now = LocalDateTime.now();
    private User author = new User(userId, "name", "email@gmail.com");
    private ItemRequestDtoInput requestDtoInput = new ItemRequestDtoInput("some description");
    private ItemRequest expectedRequest = new ItemRequest(requestId, "some description",
            now, author);
    private ItemRequest secondExpectedRequest = new ItemRequest(2L, "some description",
            now, author);
    private ItemRequestDto itemRequestDto = new ItemRequestDto(requestId, "some description", now, List.of());

    @Test
    void createRequest_whenDataCorrect_thenReturn() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(author));
        when(requestRepository.save(RequestMapper.mapToItemRequest(requestDtoInput, author))).thenReturn(expectedRequest);

        ItemRequestDto savedItemRequestDto = requestService.createRequest(userId, requestDtoInput);

        assertEquals(expectedRequest.getId(), savedItemRequestDto.getId());
        assertEquals(expectedRequest.getDescription(), savedItemRequestDto.getDescription());
    }

    @Test
    void createRequest_whenUserNotFound_thenThrow() {
        when(userRepository.findById(userId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () ->requestService.createRequest(userId, requestDtoInput));
    }

    @Test
    void getRequestById_whenRequestFound_thenReturn() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(expectedRequest));
        when(itemRepository.findByRequestIdOrderByCreated(requestId)).thenReturn(List.of());

        ItemRequestDto actualRequest = requestService.getRequestById(userId, requestId);

        assertEquals(expectedRequest.getId(), actualRequest.getId(), "Id should be the same");
        assertEquals(requestDtoInput.getDescription(), actualRequest.getDescription());

    }

    @Test
    void getRequestById_whenRequestNotFound_thenThrow() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findById(requestId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> requestService.getRequestById(userId, requestId));
    }

    @Test
    void getRequestById_whenUserNotFound_thenThrow() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getRequestById(userId, requestId));
    }

    @Test
    void getRequestsByOwner_whenListEmpty_thenReturnEmpty() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdOrderByCreatedDesc(userId)).thenReturn(List.of());

        assertEquals(0, requestService.getRequestsByOwner(userId).size());
    }

    @Test
    void getRequestsByOwner_whenUserNotFound_thenThrow() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getRequestsByOwner(userId));
    }

    @Test
    void getRequestsByOwner_whenListNotEmpty_thenReturnList() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdOrderByCreatedDesc(userId))
                .thenReturn(List.of(expectedRequest, secondExpectedRequest));

        List<ItemRequestDto> requestDtos = requestService.getRequestsByOwner(userId);
        assertEquals(2, requestDtos.size());
        assertEquals("some description", requestDtos.get(0).getDescription());
    }


    @Test
    void getAllRequests_whenArgsCorrect_thenReturn() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdNot(userId,
                PageRequest.of(0, 10, Sort.by("created").descending()))).thenReturn(Page.empty());

        assertEquals(0, requestService.getAllRequests(userId, 0, 10).size());
    }

    @Test
    void getAllRequests_whenArgsNotCorrect_thenThrow() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdNot(anyLong(), any()))
                .thenThrow(ValidationException.class);

        assertThrows(ValidationException.class, () -> requestService.getAllRequests(userId, -1, 10));
    }

    @Test
    void getAllRequests_whenArgsCorrectNotEmpty_thenReturn() {
        PageImpl<ItemRequest> itemRequests = new PageImpl<>(List.of(expectedRequest, secondExpectedRequest));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByAuthorIdNot(userId,
                PageRequest.of(0, 10, Sort.by("created").descending())))
                .thenReturn(itemRequests);


        assertEquals(2, requestService.getAllRequests(userId, 0, 10).size());
    }

    @Test
    void getAllRequests_whenUserNotFound_thenThrow() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getAllRequests(userId, 0, 10));
    }
}
