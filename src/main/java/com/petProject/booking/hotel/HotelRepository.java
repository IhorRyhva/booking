package com.petProject.booking.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    boolean existsHotelByNameOfHotel(String nameOfHotel);
    Hotel getHotelByNameOfHotel(String nameOfHotel);

    List<Hotel> getHotelByStar(Star star);

    boolean existsById(Long id);

    void removeById(Long id);
}
