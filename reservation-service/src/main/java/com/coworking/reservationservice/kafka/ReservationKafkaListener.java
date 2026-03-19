package com.coworking.reservationservice.kafka;

import com.coworking.reservationservice.service.ReservationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationKafkaListener {

    private final ReservationService reservationService;

    public ReservationKafkaListener(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @KafkaListener(topics = "room-deleted", groupId = "reservation-service")
    public void onRoomDeleted(String roomId) {
        reservationService.cancelByRoomId(Long.parseLong(roomId));
    }

    @KafkaListener(topics = "member-deleted", groupId = "reservation-service")
    public void onMemberDeleted(String memberId) {
        reservationService.deleteByMemberId(Long.parseLong(memberId));
    }
}
