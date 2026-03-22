package com.petProject.booking.room;

import com.petProject.booking.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomsByCategory(RoomCategory category);
    Room findRoomByHotelAndNumber(Hotel hotel, int number);
}
