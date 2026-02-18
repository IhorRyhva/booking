package com.petProject.booking.accommodation.room;

import com.petProject.booking.accommodation.hotel.Hotel;
import com.petProject.booking.accommodation.hotel.HotelRequest;
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
                .hotel(getHotelRequest(room.getHotel()))
                .build();
    }

    private static HotelRequest getHotelRequest(Hotel hotel) {
        return HotelRequest.builder()
                .nameOfHotel(hotel.getNameOfHotel())
                .star(hotel.getStar())
                .location(hotel.getLocation())
                .build();
    }

    public List<Long> getRoomId(List<Room> rooms) {
        return rooms.stream().map(Room::getId).toList();
    }

    public ArrayList<RoomResponse> toResponse(ArrayList<Room> result) {
        ArrayList<RoomResponse> roomResponses = new ArrayList<>();

        for (Room room: result) {
            roomResponses.add(getRoom(room));
        }
        return roomResponses;
    }

}
