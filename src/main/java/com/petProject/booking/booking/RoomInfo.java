package com.petProject.booking.booking;

import com.petProject.booking.room.dto.BookedData;
import jakarta.persistence.Embeddable;

@Embeddable
public record RoomInfo(
        BookedData bookedData,
        int price,
        String hotelName
) {
}
