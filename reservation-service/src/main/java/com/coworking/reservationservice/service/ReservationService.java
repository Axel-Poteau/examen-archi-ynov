package com.coworking.reservationservice.service;

import com.coworking.reservationservice.dto.MemberDTO;
import com.coworking.reservationservice.dto.RoomDTO;
import com.coworking.reservationservice.model.Reservation;
import com.coworking.reservationservice.model.ReservationStatus;
import com.coworking.reservationservice.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestTemplate restTemplate;

    public ReservationService(ReservationRepository reservationRepository, RestTemplate restTemplate) {
        this.reservationRepository = reservationRepository;
        this.restTemplate = restTemplate;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return reservationRepository.findByMemberId(memberId);
    }

    public List<Reservation> findByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    public Reservation create(Reservation reservation) {
        RoomDTO room = restTemplate.getForObject(
                "http://room-service/api/rooms/" + reservation.getRoomId(), RoomDTO.class);

        if (room == null) {
            throw new RuntimeException("Room not found");
        }

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservation.getRoomId(), reservation.getStartDateTime(), reservation.getEndDateTime());

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room is already booked on this time slot");
        }

        MemberDTO member = restTemplate.getForObject(
                "http://member-service/api/members/" + reservation.getMemberId(), MemberDTO.class);

        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        if (member.isSuspended()) {
            throw new RuntimeException("Member is suspended");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        Reservation saved = reservationRepository.save(reservation);

        restTemplate.patchForObject(
                "http://room-service/api/rooms/" + reservation.getRoomId() + "/availability?available=false",
                null, Void.class);

        return saved;
    }

    public Reservation cancel(Long id) {
        Reservation reservation = findById(id);

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed reservations can be cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        restTemplate.patchForObject(
                "http://room-service/api/rooms/" + reservation.getRoomId() + "/availability?available=true",
                null, Void.class);

        return reservation;
    }

    public Reservation complete(Long id) {
        Reservation reservation = findById(id);

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed reservations can be completed");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservationRepository.save(reservation);

        restTemplate.patchForObject(
                "http://room-service/api/rooms/" + reservation.getRoomId() + "/availability?available=true",
                null, Void.class);

        return reservation;
    }
}
