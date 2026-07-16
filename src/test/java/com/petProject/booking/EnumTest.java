package com.petProject.booking;

import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.hotel.HotelRepository;
import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomCategory;
import com.petProject.booking.room.RoomRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertNull;


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EnumTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:18.4");

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void enumStarTest() {
        Star[] stars = Star.values();
        for(Star star: stars) {
            Hotel hotel = Hotel.builder()
                    .star(star)
                    .build();
            hotelRepository.save(hotel);
            entityManager.flush();
        }
    }

    @Test
    void enumRoomCategoryTest() {
        RoomCategory[] categories = RoomCategory.values();
        Hotel hotel = Hotel.builder().star(Star.FOUR).build();
        hotelRepository.save(hotel);
        entityManager.flush();
        for(RoomCategory roomCategory: categories) {
            Room room = Room.builder()
                    .hotel(hotel)
                    .category(roomCategory)
                    .build();
            roomRepository.save(room);
            entityManager.flush();
        }
    }
}
