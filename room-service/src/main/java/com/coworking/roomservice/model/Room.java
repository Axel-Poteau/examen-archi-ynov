package com.coworking.roomservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Schema(description = "Représentation d'une salle de coworking")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique de la salle", example = "1")
    private Long id;

    @Schema(description = "Nom de la salle", example = "Salle Innovation")
    private String name;

    @Schema(description = "Ville où se situe la salle", example = "Paris")
    private String city;

    @Schema(description = "Capacité maximale de la salle", example = "10")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type de salle", example = "MEETING_ROOM")
    private RoomType type;

    @Schema(description = "Tarif horaire en euros", example = "25.00")
    private BigDecimal hourlyRate;

    @Schema(description = "Disponibilité de la salle", example = "true")
    private boolean available;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public RoomType getType() { return type; }
    public void setType(RoomType type) { this.type = type; }

    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
