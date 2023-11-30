package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByAuthorIdOrderByCreatedDesc(long authorId);

    Page<ItemRequest> findAllByAuthorIdNot(long userId, Pageable pageable);
}
