package com.petProject.booking;

import com.petProject.booking.booking.Book;
import com.petProject.booking.booking.BookRepository;
import com.petProject.booking.booking.BookService;
import com.petProject.booking.booking.ForbiddenBookException;
import com.petProject.booking.common.exception.IncorrectBookTimeException;
import com.petProject.booking.common.exception.RoomNotExistException;
import com.petProject.booking.hotel.*;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomCategory;
import com.petProject.booking.room.RoomRepository;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.specification.RoomSpecification;
import com.petProject.booking.user.User;
import com.petProject.booking.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AdminService.class)
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
    AdminService adminService;

    @Autowired
    EntityManager em;

    @Test
    void roomNotExistMustThrowExceptionTest() {
        //Here room db haven't any room
        assertThrows(RoomNotExistException.class, () -> adminService.removeRoom(5));
    }

    @Test
    void mustReturnTrueWithoutBooksTest() {
        Hotel hotel = Hotel.builder()
                .star(Star.FIVE)
                .rooms(new ArrayList<>())
                .build();
        Room room = Room.builder()
                .category(RoomCategory.BASIC)
                .books(new ArrayList<>())
                .hotel(hotel)
                .price(500)
                .number(4)
                .build();
        hotel.getRooms().add(room);
        hotel = hotelRepository.save(hotel);
        em.flush();
        em.clear();
        room = hotel.getRooms().getFirst();
        roomRepository.save(room);
        em.flush();
        assertTrue(adminService.removeRoom(room.getId()));
    }

    @Test
    void bookStartIsFiveDaysAfterTodayMustBeForbiddenRemoveRoomTest() throws IncorrectBookTimeException {
        Hotel hotel = Hotel.builder()
                .star(Star.FIVE)
                .rooms(new ArrayList<>())
                .build();
        Room room = Room.builder()
                .category(RoomCategory.BASIC)
                .books(new ArrayList<>())
                .hotel(hotel)
                .price(500)
                .number(4)
                .build();
        User user = User.builder().build();
        hotel.getRooms().add(room);
        hotel = hotelRepository.save(hotel);
        user = userRepository.save(user);
        em.flush();
        em.clear();
        room = hotel.getRooms().getFirst();
        Book book = new Book(user, new BookedData(LocalDate.now().plusDays(5), LocalDate.now().plusDays(10)), room);
        room.getBooks().add(book);
        roomRepository.save(room);
        bookRepository.save(book);
        em.flush();
        assertFalse(adminService.removeRoom(room.getId()));
    }

    @Test
    void bookStartIsTodayMustBeForbiddenRemoveRoomTest() throws IncorrectBookTimeException {
        Hotel hotel = Hotel.builder()
                .star(Star.FIVE)
                .rooms(new ArrayList<>())
                .build();
        Room room = Room.builder()
                .category(RoomCategory.BASIC)
                .books(new ArrayList<>())
                .hotel(hotel)
                .price(500)
                .number(4)
                .build();
        User user = User.builder().build();
        hotel.getRooms().add(room);
        hotel = hotelRepository.save(hotel);
        user = userRepository.save(user);
        em.flush();
        em.clear();
        room = hotel.getRooms().getFirst();
        Book book = new Book(user, new BookedData(LocalDate.now(), LocalDate.now().plusDays(5)), room);
        room.getBooks().add(book);
        roomRepository.save(room);
        bookRepository.save(book);
        em.flush();
        assertFalse(adminService.removeRoom(room.getId()));
    }

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
        User user = User.builder().build();
        userRepository.save(user);
        Room room = Room.builder()
                .price(50)
                .number(4)
                .category(RoomCategory.BASIC)
                .build();
        int currentPrice = room.getPrice();
        Hotel hotel = Hotel.builder()
                .nameOfHotel("Test")
                .rooms(List.of(room))
                .star(Star.FOUR)
                .build();
        room.setHotel(hotel);
        LocalDate now = LocalDate.now();
        Book book = new Book(user, new BookedData(now, now.plusYears(1)), room);
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

    @Test
    void correctSpecificationPriceFilterWorkTest() {
        Hotel hotel = Hotel.builder()
                .star(Star.FOUR)
                .rooms(new ArrayList<>())
                .build();
        Room room = Room.builder()
                .hotel(hotel)
                .category(RoomCategory.BASIC)
                .price(10)
                .build();
        Room room1 = Room.builder()
                .hotel(hotel)
                .category(RoomCategory.BASIC)
                .price(5)
                .build();
        Room room2 = Room.builder()
                .hotel(hotel)
                .price(200)
                .category(RoomCategory.BASIC)
                .build();
        Room room3 = Room.builder()
                .hotel(hotel)
                .price(100)
                .category(RoomCategory.BASIC)
                .build();
        hotel.getRooms().addAll(List.of(room, room1, room2, room3));
        hotelRepository.save(hotel);
        em.flush();
        List<Room> rooms = this.roomRepository.findAll(
                Specification.allOf(
                        RoomSpecification.filterByPrice(10, null)
                )
        );
        assertTrue(rooms.containsAll(List.of(room, room2, room3)) && rooms.size() == 3);
        rooms = this.roomRepository.findAll(
                Specification.allOf(
                        RoomSpecification.filterByPrice(null, 10)
                )
        );
        assertTrue(rooms.containsAll(List.of(room, room1)) && rooms.size() == 2);
        rooms = this.roomRepository.findAll(
                Specification.allOf(
                        RoomSpecification.filterByPrice(null, null)
                )
        );
        assertTrue(rooms.containsAll(List.of(room, room1, room2, room3)) && rooms.size() == 4);
        rooms = this.roomRepository.findAll(
                Specification.allOf(
                        RoomSpecification.filterByPrice(5, 100)
                )
        );
        assertTrue(rooms.containsAll(List.of(room, room1, room3)) && rooms.size() == 3);
    }

    @Test
    void checkFilterByStarWork() {
        Hotel hotel4 = Hotel.builder()
                .star(Star.FOUR)
                .rooms(new ArrayList<>())
                .build();
        Room room4 = Room.builder()
                .hotel(hotel4)
                .category(RoomCategory.BASIC)
                .price(10)
                .build();
        Hotel hotel5 = Hotel.builder()
                .star(Star.FIVE)
                .rooms(new ArrayList<>())
                .build();
        Room room5 = Room.builder()
                .hotel(hotel5)
                .category(RoomCategory.BASIC)
                .price(10)
                .build();
        hotel4.getRooms().add(room4);
        hotel5.getRooms().add(room5);
        this.hotelRepository.saveAll(List.of(hotel4, hotel5));
        List<Room> rooms = roomRepository.findAll(
                RoomSpecification.filterByStar(null)
        );
        assertTrue(rooms.containsAll(List.of(room4, room5)) && rooms.size() == 2);
        rooms = roomRepository.findAll(
                RoomSpecification.filterByStar(Star.FIVE)
        );
        assertTrue(rooms.contains(room5) && rooms.size() == 1);
    }

    @Test
    void correctFilterByCountryAndTownTest() {
        Hotel hotel = Hotel.builder()
                .star(Star.FOUR)
                .rooms(new ArrayList<>())
                .location(Location.builder()
                        .country("Ukraine")
                        .town("Lviv")
                        .build())
                .build();
        Room room = Room.builder()
                .hotel(hotel)
                .category(RoomCategory.BASIC)
                .price(10)
                .build();
        Hotel hotel2 = Hotel.builder()
                .star(Star.FOUR)
                .rooms(new ArrayList<>())
                .location(Location.builder()
                        .country("Czechia")
                        .town("Praha")
                        .build())
                .build();
        Room room2 = Room.builder()
                .hotel(hotel2)
                .category(RoomCategory.BASIC)
                .price(10)
                .build();
        hotel.getRooms().add(room);
        hotel2.getRooms().add(room2);
        hotelRepository.saveAll(List.of(hotel, hotel2));
        em.flush();
        List<Room> rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByCountry("Ukraine"),
                RoomSpecification.filterByTown("Kyiv")
        ));
        assertTrue(rooms.isEmpty());
        rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByCountry("Slovakia"),
                RoomSpecification.filterByTown("Bratislava")
        ));
        assertTrue(rooms.isEmpty());
        rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByCountry("Ukraine"),
                RoomSpecification.filterByTown("Lviv")
        ));
        assertTrue(rooms.contains(room) && rooms.size() == 1);
        rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByCountry(null),
                RoomSpecification.filterByTown(null)
        ));
        assertTrue(rooms.containsAll(List.of(room, room2)) && rooms.size() == 2);
    }

    @Test
    void correctFilterByCategory() {
        Hotel hotel = Hotel.builder()
                .star(Star.FOUR)
                .rooms(new ArrayList<>())
                .build();
        Room room = Room.builder()
                .hotel(hotel)
                .category(RoomCategory.BASIC)
                .price(10)
                .build();
        hotel.getRooms().add(room);
        hotelRepository.save(hotel);
        em.flush();
        List<Room> rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByCategory(RoomCategory.BASIC)
        ));
        assertTrue(rooms.contains(room) && rooms.size() == 1);
        rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByCategory(RoomCategory.LUX)
        ));
        assertTrue(rooms.isEmpty());
    }

    @Test
    void correctFilterByDateWork() throws IncorrectBookTimeException {
        User user = User.builder().build();
        user = userRepository.save(user);
        em.flush();
        Hotel hotel = Hotel.builder()
                .star(Star.FOUR)
                .rooms(new ArrayList<>())
                .build();
        Room room = Room.builder()
                .hotel(hotel)
                .category(RoomCategory.BASIC)
                .price(10)
                .books(new ArrayList<>())
                .build();
        hotel.getRooms().add(room);
        hotel = hotelRepository.save(hotel);
        room = hotel.getRooms().getFirst();
        BookedData bookedData = new BookedData(LocalDate.now().plusDays(1), LocalDate.now().plusDays(10));
        Book book = new Book(user, bookedData, room);
        room.getBooks().add(book);
        roomRepository.save(room);
        bookRepository.save(book);
        em.flush();
        List<Room> rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByDate(LocalDate.now(), LocalDate.now().plusDays(5))
        ));
        assertTrue(rooms.isEmpty());
        rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByDate(LocalDate.now(), LocalDate.now().plusDays(11))
        ));
        assertTrue(rooms.isEmpty());
        rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByDate(LocalDate.now().plusDays(5), LocalDate.now().plusDays(11))
        ));
        assertTrue(rooms.isEmpty());
        rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByDate(LocalDate.now().plusDays(1), LocalDate.now().plusDays(10))
        ));
        assertTrue(rooms.isEmpty());
        rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByDate(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12))
        ));
        assertTrue(rooms.contains(room) && rooms.size() == 1);
        rooms = roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByDate(LocalDate.now(), LocalDate.now().plusDays(1))
        ));
        assertTrue(rooms.contains(room) && rooms.size() == 1);
    }
}
