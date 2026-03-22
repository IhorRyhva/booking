package com.petProject.booking.hotel.dto;

import com.petProject.booking.hotel.Location;
import com.petProject.booking.hotel.Star;
import jakarta.persistence.Embeddable;
import lombok.Builder;

@Builder
@Embeddable
public record HotelResponse(
        String nameOfHotel,
        Star star,
        Location location
) {
    @Override
    public String toString() {
        return "HotelRequest{" +
                "nameOfHotel='" + nameOfHotel + '\'' +
                ", star=" + star +
                ", location=" + location +
                '}';
    }
}
