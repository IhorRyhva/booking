package com.petProject.booking.room.dto;

import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.RoomCategory;
import lombok.Builder;

@Builder
public record FilterData(
        String country,
        String city,
        BookedData bookedData,
        int min,
        int max,
        RoomCategory roomCategory,
        Star star
) {
}
