package com.coworking.reservationservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Schema(description = "Représentation d'une réservation de salle")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique de la réservation", example = "1")
    private Long id;

    @Schema(description = "Identifiant de la salle réservée", example = "1")
    private Long roomId;

    @Schema(description = "Identifiant du membre ayant réservé", example = "1")
    private Long memberId;

    @Schema(description = "Date et heure de début de la réservation", example = "2026-03-20T09:00:00")
    private LocalDateTime startDateTime;

    @Schema(description = "Date et heure de fin de la réservation", example = "2026-03-20T11:00:00")
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Statut de la réservation", example = "CONFIRMED")
    private ReservationStatus status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}
