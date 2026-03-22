package com.petProject.booking.room.dto;

import com.petProject.booking.hotel.dto.HotelResponse;
import com.petProject.booking.room.RoomCategory;
import jakarta.persistence.Embeddable;
import lombok.Builder;

@Builder
@Embeddable
public record RoomResponse(
        int price,
        RoomCategory roomCategory,
        int number,
        HotelResponse hotel
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
