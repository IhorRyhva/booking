package com.petProject.booking.hotel;

import com.petProject.booking.common.exception.IncorrectMaxMinPriceException;
import com.petProject.booking.room.*;
import com.petProject.booking.room.dto.BookedData;
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


    @Transactional
    public boolean remove(long id) {
        if (!this.hotelRepository.existsById(id)) {
            return false;
        }
        this.hotelRepository.removeById(id);
        return hotelRepository.existsById(id);
    }

    public ArrayList<Room> getRoomsByAnotherInput(int min, int max, Star star, RoomCategory roomCategory, ArrayList<Room> newResponses) {
        return null;
    }
}
