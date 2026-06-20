package com.petProject.booking.hotel;

import com.petProject.booking.common.exception.IncorrectMaxMinPriceException;
import com.petProject.booking.room.*;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.room.dto.RoomResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    public Hotel addHotel(Hotel hotel) {
        if (!hotelRepository.existsHotelByNameOfHotel(hotel.getNameOfHotel())) {
            hotelRepository.save(hotel);
            return hotel;
        }
        return hotelRepository.getHotelByNameOfHotel(hotel.getNameOfHotel());
    }


/**TODO**/
// ще додай emailService

    public List<Long> getRoomsByDataAndLocation (String country, String city, BookedData bookedData) {
        List<Hotel> hotels = this.hotelMapper.getHotelByLocation(country, city, this.hotelRepository.findAll());

        List<Room> rooms = getRoomList(hotels);
        rooms = this.roomService.getRoomByData(bookedData, rooms);

        return this.roomMapper.getRoomId(rooms);
    }

    private List<Room> getRoomList(List<Hotel> hotels) {
        List<Room> rooms = new ArrayList<>();
        for (Hotel hotel: hotels) {
            rooms.addAll(hotel.getRooms());
        }
        return rooms;
    }

    public ArrayList<RoomResponse> getRoomsByAnotherInput(int min, int max, Star star, RoomCategory roomCategory, ArrayList<RoomResponse> responses) throws IncorrectMaxMinPriceException {
        ArrayList<RoomResponse> newResponses = (ArrayList<RoomResponse>) responses.clone();
        this.hotelMapper.filterByStar(star, newResponses);
        this.hotelMapper.filterByCategory(roomCategory, newResponses);
        this.roomService.filterByPrice(min, max, newResponses);

        return newResponses;
    }

    @Transactional
    public boolean remove(int id) {
        if (!this.hotelRepository.existsById((long) id)) {
            return false;
        }
        this.hotelRepository.removeById((long) id);
        return hotelRepository.existsById((long) id);
    }
}
