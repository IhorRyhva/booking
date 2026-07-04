package com.petProject.booking.room.dto;

import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.RoomCategory;

public record SortDTO(
        int min,
        int max,
        Star star,
        RoomCategory roomCategory
) {
}
