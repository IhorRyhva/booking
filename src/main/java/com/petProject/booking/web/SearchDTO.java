package com.petProject.booking.web;

import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.RoomCategory;

import java.time.LocalDate;

public record SearchDTO(
        String country,
        String city,
        LocalDate start,
        LocalDate end,
        Integer min,
        Integer max,
        RoomCategory roomCategory,
        Star star,
        String query,
        Integer bedNumber
) {
}
