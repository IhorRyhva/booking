package com.petProject.booking;

import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.hotel.HotelRepository;
import com.petProject.booking.hotel.Location;
import com.petProject.booking.hotel.Star;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.net.URI;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DataBaseTest {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:18.4");

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    EntityManager em;

    @Test
    void repositoryReturnsSavedHotelTest() {
        Hotel hotel = Hotel.builder()
                .star(Star.FIVE)
                .nameOfHotel("Lala")
                .location(Location.builder()
                        .town("Town")
                        .country("Country")
                        .build())
                .build();
        Long id = hotelRepository.save(hotel).getId();
        hotelRepository.flush();
        em.clear();
        assertTrue(hotelRepository.findById(id).isPresent());
    }

    @Test
    void workPortEqualsContainerPortTest() {
        Session session = em.unwrap(Session.class);
        session.doWork(connection -> {
            String sURI = connection.getMetaData().getURL().substring(5);
            URI uri = URI.create(sURI);
            assertEquals(postgreSQLContainer.getFirstMappedPort(), uri.getPort());
        });
    }
}
