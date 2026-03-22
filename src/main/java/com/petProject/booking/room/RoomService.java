package com.petProject.booking.room;

import com.petProject.booking.common.exception.IncorrectMaxMinPriceException;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.room.dto.RoomResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {
   private final RoomMapper roomMapper;
   private final RoomRepository roomRepository;

    public void filterByPrice(int min, int max, ArrayList<RoomResponse> newResponses) throws IncorrectMaxMinPriceException {
        if (min < 0 || min > max) {
            throw new IncorrectMaxMinPriceException("Please check your min and max price");
        }
        newResponses.removeIf(room -> (room.price() < min || room.price() >= max));
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

    public ArrayList<RoomResponse> getRoomsById(List<Long> rooms) {
        ArrayList<Room> result = new ArrayList<>();
        for (Long id: rooms) {
            Optional<Room> room = this.roomRepository.findById(id);
            room.ifPresent(result::add);
        }
        return this.roomMapper.toResponse(result);
    }
}
