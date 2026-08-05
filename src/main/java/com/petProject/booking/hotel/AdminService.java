package com.petProject.booking.hotel;

import com.petProject.booking.common.exception.HotelNotExistException;
import com.petProject.booking.common.exception.RoomNotExistException;
import com.petProject.booking.room.*;
import com.petProject.booking.specification.AdminSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
/**TODO**/
// ще додай emailService
    @Transactional
    public boolean removeHotel(long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isEmpty()) {
            throw new HotelNotExistException();
        }
        Hotel hotel = hotelOptional.get();
        if (!this.existBookedRooms(LocalDate.now(), hotel)) {
            hotel.setRemoved(true);
            for (Room room: hotel.getRooms()) {
                room.setRemoved(true);
            }
            return true;
        }
        return false;
    }

    private boolean existBookedRooms(LocalDate now, Hotel hotel) {
        return roomRepository.exists(Specification.allOf(
                AdminSpecification.getRooms(hotel.getId()),
                AdminSpecification.existBookedRoom(now)
        ));
    }

    @Transactional
    public boolean removeRoom(long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isEmpty()) {
            throw new RoomNotExistException();
        }
        boolean exist = roomRepository.exists(Specification.allOf(
                AdminSpecification.getRoom(id),
                AdminSpecification.existBookedRoom(LocalDate.now())
        ));
        if (exist) {
            return false;
        }
        Room room = roomOptional.get();
        room.setRemoved(true);
        return true;
    }

}
