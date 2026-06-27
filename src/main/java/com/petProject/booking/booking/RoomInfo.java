package com.petProject.booking.booking;

import jakarta.persistence.Embeddable;

@Embeddable
public record RoomInfo(
        int price,
        String hotelName,
        int number
) {
}
