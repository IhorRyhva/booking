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

    public void filterByPrice(int min, int max, ArrayList<Room> newResponses) throws IncorrectMaxMinPriceException {
        if (min < 0 || min > max) {
            throw new IncorrectMaxMinPriceException("Please check your min and max price");
        }
        newResponses.removeIf(room -> (room.getPrice() < min || room.getPrice() >= max));
    }

    public List<Room> getRoomByData (BookedData bookedData, List<Room> rooms) {
        List<Room> result = new ArrayList<>();
        for (Room room: rooms) {
            boolean canBeAdd = true;
            for (BookedData bookedDataOfRoom: room.getBookedData()) {
                if (bookedDataOfRoom.timeCrossing(bookedData)) {
                    canBeAdd = false;
                    break;
                }
            }
            if (canBeAdd) {
                result.add(room);
            }
        }

        return result;
    }

    public List<Room> getRoomsById(List<Long> rooms) {
        List<Room> result = new ArrayList<>();
        for (Long id: rooms) {
            Optional<Room> room = this.roomRepository.findById(id);
            room.ifPresent(result::add);
        }
        return result;
    }
}
