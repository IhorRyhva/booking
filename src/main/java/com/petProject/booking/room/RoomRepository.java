package com.petProject.booking.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    @Query("select room from Room room where room.hotel.location.country = :country" +
            " and room.hotel.location.town = :city" +
            " and not exists (" +
            "select book from Book book where book.room = room" +
            " and book.bookedData.startDate < :end" +
            " and book.bookedData.endDate > :start)")
    List<Room> findRoomByLocationAndData(@Param("country") String country, @Param("city") String city, @Param("start") LocalDate startDate, @Param("end") LocalDate endDate);

}
