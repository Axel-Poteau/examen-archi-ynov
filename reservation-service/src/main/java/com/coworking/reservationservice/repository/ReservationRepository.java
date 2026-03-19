package com.coworking.reservationservice.repository;

import com.coworking.reservationservice.model.Reservation;
import com.coworking.reservationservice.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRoomIdAndStatus(Long roomId, ReservationStatus status);

    List<Reservation> findByMemberIdAndStatus(Long memberId, ReservationStatus status);

    List<Reservation> findByRoomId(Long roomId);

    List<Reservation> findByMemberId(Long memberId);

    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND r.status = 'CONFIRMED' " +
           "AND r.startDateTime < :endDateTime AND r.endDateTime > :startDateTime")
    List<Reservation> findConflictingReservations(@Param("roomId") Long roomId,
                                                   @Param("startDateTime") LocalDateTime startDateTime,
                                                   @Param("endDateTime") LocalDateTime endDateTime);
}
