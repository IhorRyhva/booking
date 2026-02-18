package com.petProject.booking.book;

import com.petProject.booking.accommodation.room.BookedData;
import com.petProject.booking.accommodation.room.RoomResponse;
import com.petProject.booking.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {

    private BookedData bookedData;

    private RoomResponse room;
}
