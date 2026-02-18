package com.petProject.booking.accommodation.room;

import com.petProject.booking.accommodation.hotel.HotelRequest;
import jakarta.persistence.Embeddable;
import lombok.Builder;

@Builder
@Embeddable
public record RoomResponse(
        int price,
        RoomCategory roomCategory,
        int number,
        HotelRequest hotel
) {
    @Override
    public String toString() {
        return "RoomResponse{" +
                "price=" + price +
                ", roomCategory=" + roomCategory +
                ", number=" + number +
                ", hotel=" + hotel +
                '}';
    }
}
