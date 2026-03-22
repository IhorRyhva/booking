package com.petProject.booking.hotel;

import com.petProject.booking.hotel.dto.HotelResponse;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomCategory;
import com.petProject.booking.room.dto.RoomResponse;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelMapper {

    public List<Hotel> getHotelByLocation (String country, String city, List<Hotel> hotels) {
        if (country != null || city != null) {
            Location location = new Location(country, city);
            hotels.removeIf(h -> !location.equals(h.getLocation()));
        }
        return hotels;
    }



    private static HotelResponse getRequest(Hotel hotel) {
        return HotelResponse.builder()
                .nameOfHotel(hotel.getNameOfHotel())
                .location(hotel.getLocation())
                .star(hotel.getStar())
                .build();
    }

    public void filterByStar(Star star, ArrayList<RoomResponse> responses) {
        if (star != Star.ANY) {
            responses.removeIf(roomResponse -> roomResponse.hotel().star() != star);
        }
    }

    public void filterByCategory(RoomCategory roomCategory, ArrayList<RoomResponse> newResponses) {
        if (roomCategory != RoomCategory.ANY) {
            newResponses.removeIf(roomResponse -> roomResponse.roomCategory() != roomCategory);
        }
    }

    public HotelResponse getHotelResponse(String nameOfHotel, Room room) {
        return HotelResponse.builder()
                .nameOfHotel(nameOfHotel)
                .location(room.getHotel().getLocation())
                .star(room.getHotel().getStar())
                .build();
    }
}
