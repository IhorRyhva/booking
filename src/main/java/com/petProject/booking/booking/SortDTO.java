package com.petProject.booking.booking;

import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.RoomCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortDTO {
    private String country;
    private String town;
    private String nameOfHotel;
    private Star star;  int number;
    private RoomCategory category;
    private int price;

    public SortDTO() {}
}
