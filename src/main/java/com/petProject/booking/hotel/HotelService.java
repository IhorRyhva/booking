package com.petProject.booking.hotel;

import com.petProject.booking.hotel.room.BookedData;
import com.petProject.booking.hotel.room.Room;
import com.petProject.booking.hotel.room.RoomCategory;
import com.petProject.booking.hotel.room.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public Hotel addHotel(Hotel hotel) {
        if (!hotelRepository.existsHotelByNameOfHotel(hotel.getNameOfHotel())) {
            hotelRepository.save(hotel);
            return hotel;
        }
        return hotelRepository.getHotelByNameOfHotel(hotel.getNameOfHotel());
    }

    public boolean bookRoom (Hotel hotel, int roomNumber, LocalDateTime start, LocalDateTime end) {
        if (!hotelRepository.existsHotelByNameOfHotel(hotel.getNameOfHotel())) {
            if (roomNumber >= 0 && hotel.getRooms().size() > roomNumber) {
                BookedData bookedData = new BookedData(start, end);
                Room room = hotel.getRooms().get(roomNumber - 1);

                boolean timeCrossing = isCrossing(bookedData, room);

                if (!timeCrossing) {
                    room.getBookedData().add(bookedData);
                    return true;
                }
            }
        }
        return false;
    }

    public List<Hotel> findHotelsByStar (Star star) {
        return this.hotelRepository.getHotelByStar(star);
    }

    public List<Room> findRoomByCategory (RoomCategory category) {
        return this.roomRepository.findRoomsByCategory(category);
    }
/**TODO**/
//додай ще сортування по готелю локації ціні за ніч ось ті всі комбінаціїї
// і не забудь що користувач має зберігати які кімнати він забронював і ще додай emailService
    private boolean isCrossing(BookedData bookedData, Room room) {
        for (BookedData data: room.getBookedData()) {
            if (data.timeCrossing(bookedData) || data.equals(bookedData)) {
                return true;
            }
        }
        return false;
    }
}
