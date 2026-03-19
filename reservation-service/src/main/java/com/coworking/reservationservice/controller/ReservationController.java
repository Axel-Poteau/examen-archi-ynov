package com.coworking.reservationservice.controller;

import com.coworking.reservationservice.model.Reservation;
import com.coworking.reservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public Reservation getById(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @GetMapping("/member/{memberId}")
    public List<Reservation> getByMemberId(@PathVariable Long memberId) {
        return reservationService.findByMemberId(memberId);
    }

    @GetMapping("/room/{roomId}")
    public List<Reservation> getByRoomId(@PathVariable Long roomId) {
        return reservationService.findByRoomId(roomId);
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        return new ResponseEntity<>(reservationService.create(reservation), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancel")
    public Reservation cancel(@PathVariable Long id) {
        return reservationService.cancel(id);
    }

    @PatchMapping("/{id}/complete")
    public Reservation complete(@PathVariable Long id) {
        return reservationService.complete(id);
    }
}
