package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(long ownerId);

    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String text, String text2);

    Optional<Item> findByOwnerIdAndId(long ownerId, long itemId);

    void deleteByOwnerIdAndId(long userId, long itemId);

    @Query("select i " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.id = ?1")
    Optional<Item> findByItemIdWithComments(long itemId);

    @Query("select i " +
            "from Item as i " +
            "join i.request as r " +
            "where r.id = ?1 " +
            "order by r.created desc")
    List<Item> findByRequestIdOrderByCreated(long requestId);
}
