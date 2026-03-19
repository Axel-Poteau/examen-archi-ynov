package com.coworking.roomservice.controller;

import com.coworking.roomservice.model.Room;
import com.coworking.roomservice.service.RoomService;
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
@RequestMapping("/api/rooms")
@Tag(name = "Rooms", description = "Gestion des salles de coworking")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @Operation(summary = "Lister toutes les salles")
    public List<Room> getAll() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une salle par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Salle trouvée"),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée")
    })
    public Room getById(@Parameter(description = "ID de la salle") @PathVariable Long id) {
        return roomService.findById(id);
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Lister les salles par ville")
    public List<Room> getByCity(@PathVariable String city) {
        return roomService.findByCity(city);
    }

    @GetMapping("/available")
    @Operation(summary = "Lister les salles disponibles")
    public List<Room> getAvailable() {
        return roomService.findAvailable();
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle salle")
    @ApiResponse(responseCode = "201", description = "Salle créée avec succès")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(roomService.create(room), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une salle")
    public Room update(@PathVariable Long id, @RequestBody Room room) {
        return roomService.update(id, room);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une salle")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Salle supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID de la salle") @PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Modifier la disponibilité d'une salle")
    public void setAvailability(@PathVariable Long id, @RequestParam boolean available) {
        roomService.setAvailability(id, available);
    }
}
