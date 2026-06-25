package com.petProject.booking.room;

import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.hotel.dto.HotelResponse;
import com.petProject.booking.room.dto.RoomResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomMapper {

    private static RoomResponse getRoom(Room room) {
        return RoomResponse.builder()
                .price(room.getPrice())
                .roomCategory(room.getCategory())
                .number(room.getNumber())
                .hotel(getHotelResponse(room.getHotel()))
                .build();
    }

    private static HotelResponse getHotelResponse(Hotel hotel) {
        return HotelResponse.builder()
                .nameOfHotel(hotel.getNameOfHotel())
                .star(hotel.getStar())
                .location(hotel.getLocation())
                .build();
    }

    public List<Long> getRoomId(List<Room> rooms) {
        return rooms.stream().map(Room::getId).toList();
    }


    public RoomResponse getResponse(Room room, HotelResponse hotel, int number) {
        return RoomResponse.builder()
                .roomCategory(room.getCategory())
                .number(number)
                .price(room.getPrice())
                .hotel(hotel)
                .build();
    }

    public RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .roomCategory(room.getCategory())
                .price(room.getPrice())
                .number(room.getNumber())
                .hotel(getHotelResponse(room.getHotel()))
                .build();
    }
}
