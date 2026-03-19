package com.coworking.reservationservice.state;

import com.coworking.reservationservice.model.Reservation;

public interface ReservationState {

    void cancel(Reservation reservation);

    void complete(Reservation reservation);
}