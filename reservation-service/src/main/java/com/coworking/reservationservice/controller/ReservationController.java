package com.coworking.reservationservice.controller;

import com.coworking.reservationservice.model.Reservation;
import com.coworking.reservationservice.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Gestion des réservations de salles")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @Operation(summary = "Lister toutes les réservations")
    public List<Reservation> getAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une réservation par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée")
    })
    public Reservation getById(@Parameter(description = "ID de la réservation") @PathVariable Long id) {
        return reservationService.findById(id);
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Lister les réservations d'un membre")
    public List<Reservation> getByMemberId(@PathVariable Long memberId) {
        return reservationService.findByMemberId(memberId);
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "Lister les réservations d'une salle")
    public List<Reservation> getByRoomId(@PathVariable Long roomId) {
        return reservationService.findByRoomId(roomId);
    }

    @PostMapping
    @Operation(summary = "Créer une réservation", description = "Vérifie la disponibilité de la salle et le statut du membre avant de confirmer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Salle indisponible ou membre suspendu")
    })
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        return new ResponseEntity<>(reservationService.create(reservation), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Annuler une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation annulée"),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée")
    })
    public Reservation cancel(@Parameter(description = "ID de la réservation") @PathVariable Long id) {
        return reservationService.cancel(id);
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Marquer une réservation comme terminée")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation marquée comme terminée"),
            @ApiResponse(responseCode = "404", description = "Réservation non trouvée")
    })
    public Reservation complete(@Parameter(description = "ID de la réservation") @PathVariable Long id) {
        return reservationService.complete(id);
    }
}
