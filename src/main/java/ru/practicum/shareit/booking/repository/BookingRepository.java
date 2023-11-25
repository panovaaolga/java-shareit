package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 and " +
            "b.status = ?2 " +
            "order by b.start desc ")
    List<Booking> findAllByBookerIdAndStatus(long bookerId, Status status);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerAndStatus(long ownerId, Status status);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "order by b.start desc")
    List<Booking> findAllByOwner(long ownerId);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "and b.start > ?2 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerAndStateFuture(long ownerId, LocalDateTime now);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "and b.end < ?2 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerAndStatePast(long ownerId, LocalDateTime now);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerAndStateCurrent(long ownerId, LocalDateTime now);


}
