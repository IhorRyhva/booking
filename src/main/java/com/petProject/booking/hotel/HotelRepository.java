package com.petProject.booking.hotel;

import com.petProject.booking.room.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    boolean existsById(Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO hotel (star, country, name_of_hotel, city, removed) VALUES (:values)")
    public void saveAllHotels(@Param("values") List<Hotel> hotels);
}
