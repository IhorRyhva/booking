package com.petProject.booking.accommodation.hotel;

import com.petProject.booking.accommodation.room.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public boolean bookRoom (Hotel hotel, int roomNumber, LocalDate start, LocalDate end) {
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


/**TODO**/
// ще додай emailService
    private boolean isCrossing(BookedData bookedData, Room room) {
        for (BookedData data: room.getBookedData()) {
            if (data.timeCrossing(bookedData) || data.equals(bookedData)) {
                return true;
            }
        }
        return false;
    }

    public List<Long> getRoomsByDataAndLocation (String country, String city, LocalDate start, LocalDate end) {
        List<Hotel> hotels = this.hotelMapper.getHotelByLocation(country, city, this.hotelRepository.findAll());

        List<Room> rooms = getRoomList(hotels);
        rooms = this.roomService.getRoomByData(start, end, rooms);

        return this.roomMapper.getRoomId(rooms);
    }

    private List<Room> getRoomList(List<Hotel> hotels) {
        List<Room> rooms = new ArrayList<>();
        for (Hotel hotel: hotels) {
            rooms.addAll(hotel.getRooms());
        }
        return rooms;
    }

    public ArrayList<RoomResponse> getRoomsByAnotherInput(int min, int max, Star star, RoomCategory roomCategory, ArrayList<RoomResponse> responses) {
        ArrayList<RoomResponse> newResponses = (ArrayList<RoomResponse>) responses.clone();
        this.hotelMapper.filterByStar(star, newResponses);
        this.hotelMapper.filterByCategory(roomCategory, newResponses);
        this.roomService.filterByPrice(min, max, newResponses);

        return newResponses;
    }
}
