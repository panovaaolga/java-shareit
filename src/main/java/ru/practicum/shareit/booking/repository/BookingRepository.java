package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Booking> findAllByBookerIdAndStatus(long bookerId, Status status, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime now);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId, LocalDateTime start,
                                                                             LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId, LocalDateTime start,
                                                                             LocalDateTime end);

    Page<Booking> findAllByBookerIdOrderByStartDesc(long bookerId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "and b.status = ?2 " +
            "order by b.start desc")
    Page<Booking> findAllByOwnerAndStatus(long ownerId, Status status, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "order by b.start desc")
    Page<Booking> findAllByOwner(long ownerId, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "and b.start > ?2 " +
            "order by b.start desc")
    Page<Booking> findAllByOwnerAndStateFuture(long ownerId, LocalDateTime now, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "and b.end < ?2 " +
            "order by b.start desc")
    Page<Booking> findAllByOwnerAndStatePast(long ownerId, LocalDateTime now, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.owner as u " +
            "where u.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?2 " +
            "order by b.start desc")
    Page<Booking> findAllByOwnerAndStateCurrent(long ownerId, LocalDateTime now, Pageable pageable);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.id = ?1 " +
            "and b.status = 'APPROVED' " +
            "and (b.end < ?2 or " +
            "b.start < ?2 and b.end > ?2) " +
            "order by b.start desc ")
    List<Booking> findLastBooking(long itemId, LocalDateTime now);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.id = ?1 " +
            "and b.status = 'APPROVED' " +
            "and b.start > ?2 " +
            "order by b.start asc")
    List<Booking> findNextBooking(long itemId, LocalDateTime now);


}
