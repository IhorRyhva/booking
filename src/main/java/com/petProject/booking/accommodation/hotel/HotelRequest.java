package com.petProject.booking.accommodation.hotel;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Builder
@Embeddable
public record HotelRequest(
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
