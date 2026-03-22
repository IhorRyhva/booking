package com.petProject.booking.booking;

import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.room.dto.RoomResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {

    private BookedData bookedData;

    private RoomResponse room;
}
