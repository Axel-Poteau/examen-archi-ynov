package com.coworking.reservationservice.state;

import com.coworking.reservationservice.model.Reservation;

public class CancelledState implements ReservationState {

    @Override
    public void cancel(Reservation reservation) {
        throw new IllegalStateException("Reservation is already cancelled");
    }

    @Override
    public void complete(Reservation reservation) {
        throw new IllegalStateException("Cannot complete a cancelled reservation");
    }
}