package com.coworking.roomservice.controller;

import com.coworking.roomservice.model.Room;
import com.coworking.roomservice.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAll() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    public Room getById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @GetMapping("/city/{city}")
    public List<Room> getByCity(@PathVariable String city) {
        return roomService.findByCity(city);
    }

    @GetMapping("/available")
    public List<Room> getAvailable() {
        return roomService.findAvailable();
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(roomService.create(room), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @RequestBody Room room) {
        return roomService.update(id, room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    public void setAvailability(@PathVariable Long id, @RequestParam boolean available) {
        roomService.setAvailability(id, available);
    }
}
