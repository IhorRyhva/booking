package com.petProject.booking;

import com.petProject.booking.booking.Book;
import com.petProject.booking.booking.BookRepository;
import com.petProject.booking.booking.RoomInfo;
import com.petProject.booking.common.exception.IncorrectBookTimeException;
import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.hotel.HotelRepository;
import com.petProject.booking.hotel.Location;
import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomRepository;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.user.User;
import com.petProject.booking.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
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
    RoomRepository roomRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

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

    @Test
    public void constantRoomInfoAfterRoomChangeTest() throws IncorrectBookTimeException {
        Room room = Room.builder()
                .price(50)
                .number(4)
                .build();
        int currentPrice = room.getPrice();
        Hotel hotel = Hotel.builder()
                .nameOfHotel("Test")
                .rooms(List.of(room))
                .build();
        room.setHotel(hotel);
        LocalDate now = LocalDate.now();
        User user = User.builder().build();
        Book book = new Book(user, new BookedData(now, now.plusYears(1)), room);
        userRepository.save(user);
        hotelRepository.save(hotel);
        long roomId = roomRepository.save(room).getId();
        long bookId = bookRepository.save(book).getId();
        em.flush();
        em.clear();
        room = roomRepository.findById(roomId).get();
        room.setPrice(room.getPrice() + 20);
        roomRepository.save(room);
        em.flush();
        em.clear();
        book = bookRepository.findById(bookId).get();
        assertEquals(currentPrice, book.getRoomInfo().price());
    }
}
