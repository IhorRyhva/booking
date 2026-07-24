package com.petProject.booking.room.dto;

import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.RoomCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
public record FilterData(
        String country,
        String city,
        LocalDate start,
        LocalDate end,
        int min,
        int max,
        RoomCategory roomCategory,
        Star star
) {
}
