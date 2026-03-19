package com.coworking.reservationservice.service;

import com.coworking.reservationservice.dto.MemberDTO;
import com.coworking.reservationservice.dto.RoomDTO;
import com.coworking.reservationservice.model.Reservation;
import com.coworking.reservationservice.model.ReservationStatus;
import com.coworking.reservationservice.repository.ReservationRepository;
import com.coworking.reservationservice.state.ReservationState;
import com.coworking.reservationservice.state.ReservationStateFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestTemplate restTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ReservationService(ReservationRepository reservationRepository, RestTemplate restTemplate,
                              KafkaTemplate<String, String> kafkaTemplate) {
        this.reservationRepository = reservationRepository;
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
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

        long activeCount = reservationRepository
                .findByMemberIdAndStatus(reservation.getMemberId(), ReservationStatus.CONFIRMED).size();
        if (activeCount >= member.getMaxConcurrentBookings()) {
            kafkaTemplate.send("member-suspended", reservation.getMemberId().toString());
        }

        return saved;
    }

    public Reservation cancel(Long id) {
        Reservation reservation = findById(id);

        ReservationState state = ReservationStateFactory.getState(reservation.getStatus());
        state.cancel(reservation);
        reservationRepository.save(reservation);

        restTemplate.patchForObject(
                "http://room-service/api/rooms/" + reservation.getRoomId() + "/availability?available=true",
                null, Void.class);

        checkAndUnsuspendMember(reservation.getMemberId());

        return reservation;
    }

    public Reservation complete(Long id) {
        Reservation reservation = findById(id);

        ReservationState state = ReservationStateFactory.getState(reservation.getStatus());
        state.complete(reservation);
        reservationRepository.save(reservation);

        restTemplate.patchForObject(
                "http://room-service/api/rooms/" + reservation.getRoomId() + "/availability?available=true",
                null, Void.class);

        checkAndUnsuspendMember(reservation.getMemberId());

        return reservation;
    }

    public void cancelByRoomId(Long roomId) {
        List<Reservation> reservations = reservationRepository.findByRoomIdAndStatus(roomId, ReservationStatus.CONFIRMED);
        for (Reservation reservation : reservations) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
            checkAndUnsuspendMember(reservation.getMemberId());
        }
    }

    public void deleteByMemberId(Long memberId) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);
        reservationRepository.deleteAll(reservations);
    }

    private void checkAndUnsuspendMember(Long memberId) {
        MemberDTO member = restTemplate.getForObject(
                "http://member-service/api/members/" + memberId, MemberDTO.class);
        if (member != null && member.isSuspended()) {
            long activeCount = reservationRepository
                    .findByMemberIdAndStatus(memberId, ReservationStatus.CONFIRMED).size();
            if (activeCount < member.getMaxConcurrentBookings()) {
                kafkaTemplate.send("member-unsuspended", memberId.toString());
            }
        }
    }
}
