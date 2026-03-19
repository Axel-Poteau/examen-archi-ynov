package com.coworking.reservationservice.state;

import com.coworking.reservationservice.model.Reservation;

public class CompletedState implements ReservationState {

    @Override
    public void cancel(Reservation reservation) {
        throw new IllegalStateException("Cannot cancel a completed reservation");
    }

    @Override
    public void complete(Reservation reservation) {
        throw new IllegalStateException("Reservation is already completed");
    }
}