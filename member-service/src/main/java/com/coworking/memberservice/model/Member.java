package com.coworking.memberservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(description = "Représentation d'un membre de la plateforme de coworking")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique du membre", example = "1")
    private Long id;

    @Schema(description = "Nom complet du membre", example = "Jean Dupont")
    private String fullName;

    @Schema(description = "Adresse email du membre", example = "jean.dupont@email.com")
    private String email;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type d'abonnement du membre", example = "PRO")
    private SubscriptionType subscriptionType;

    @Schema(description = "Indique si le membre est suspendu (quota atteint)", example = "false")
    private boolean suspended;

    @Schema(description = "Nombre maximum de réservations simultanées autorisées", example = "5")
    private Integer maxConcurrentBookings;

    @PrePersist
    @PreUpdate
    private void setMaxBookings() {
        if (subscriptionType != null) {
            switch (subscriptionType) {
                case BASIC -> maxConcurrentBookings = 2;
                case PRO -> maxConcurrentBookings = 5;
                case ENTERPRISE -> maxConcurrentBookings = 10;
            }
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public SubscriptionType getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(SubscriptionType subscriptionType) { this.subscriptionType = subscriptionType; }

    public boolean isSuspended() { return suspended; }
    public void setSuspended(boolean suspended) { this.suspended = suspended; }

    public Integer getMaxConcurrentBookings() { return maxConcurrentBookings; }
    public void setMaxConcurrentBookings(Integer maxConcurrentBookings) { this.maxConcurrentBookings = maxConcurrentBookings; }
}
