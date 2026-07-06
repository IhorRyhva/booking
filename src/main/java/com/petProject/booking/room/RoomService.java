package com.petProject.booking.room;

import com.petProject.booking.common.exception.IncorrectMaxMinPriceException;
import com.petProject.booking.room.dto.BookedData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public List<Room> getRoomsByDataAndLocation(String country, String city, BookedData bookedData) {
        return this.roomRepository.findRoomByLocationAndData(country, city, bookedData.getStartDate(), bookedData.getEndDate());
    }
}
