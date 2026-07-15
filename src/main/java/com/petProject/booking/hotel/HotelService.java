package com.petProject.booking.hotel;

import com.petProject.booking.room.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;



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
