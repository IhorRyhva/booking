package com.petProject.booking.booking;

import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.hotel.HotelMapper;
import com.petProject.booking.hotel.HotelRepository;
import com.petProject.booking.hotel.Star;
import com.petProject.booking.hotel.dto.HotelResponse;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomCategory;
import com.petProject.booking.room.RoomMapper;
import com.petProject.booking.room.RoomRepository;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.room.dto.RoomResponse;
import com.petProject.booking.user.User;
import com.petProject.booking.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final HotelMapper hotelMapper;
    private final BookRepository bookRepository;

    /*TODO: sort bookings by start date*/
    /*TODO: add email to booking response*/
    /* TODO: require authentication only for booking action*/

    public List<BookResponse> getBooksByUser (String email) {
        return this.userRepository
                .findByEmail(email)
                .map(u -> u.getBooks().stream()
                        .map(book -> BookResponse.builder()
                                .bookedData(book.getBookedData())
                                .room(book.getRoom())
                                .build())
                        .toList()
                ).orElseGet(ArrayList::new);
    }

    public String getFormattedDate(LocalDate start) {
        return start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Transactional
    public void registerBook(String nameOfHotel, int number, User user, BookedData bookedData) {
        RoomResponse room = bookRoomAndCreateResponse(nameOfHotel, number, bookedData);
        Book book = Book.builder()
                .user(user)
                .room(room)
                .bookedData(bookedData)
                .build();
        user.getBooks().add(book);
        this.bookRepository.save(book);
    }

    private RoomResponse bookRoomAndCreateResponse(String nameOfHotel, int number, BookedData bookedData) {
        Room room = bookRoom(nameOfHotel, number, bookedData);
        HotelResponse hotel = this.hotelMapper.getHotelResponse(nameOfHotel, room);
        return this.roomMapper.getResponse(room, hotel, number);
    }

    private Room bookRoom(String nameOfHotel, int number, BookedData bookedData) {
        Hotel hotel = this.hotelRepository.getHotelByNameOfHotel(nameOfHotel);
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel " + nameOfHotel + " doesn't exist");
        }

        Room room = this.roomRepository.findRoomByHotelAndNumber(hotel, number);
        if (room == null) {
            throw new IllegalArgumentException("Room " + number + " in the hotel " + nameOfHotel + " doesn't exist");
        }

        if (bookedData == null) {
            throw new IllegalArgumentException("Book data equals null");
        }
        room.getBookedData().add(bookedData);
        this.roomRepository.save(room);
        return room;
    }

}
