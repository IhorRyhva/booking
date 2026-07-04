package com.petProject.booking.booking;

import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.hotel.HotelRepository;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomMapper;
import com.petProject.booking.room.RoomRepository;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.user.User;
import com.petProject.booking.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
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
                                .room(roomMapper.toResponse(book.getRoom()))
                                .build())
                        .toList()
                ).orElseGet(ArrayList::new);
    }

    public String getFormattedDate(LocalDate start) {
        return start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Transactional
    public void registerBook(String email, BookedData bookedData, long roomId) {
        Optional<Room> roomOptional = this.roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) throw new RuntimeException(); /**TODO* Change here this logic*/
        Room room = roomOptional.get();
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        /**TODO*null юзер означає що користувач незареєстрований і для цього треба окрему логіку придумати*/
        Book book = new Book(userOptional.get(), bookedData, room); /**TODO*userOptional.get() тимчасове рішення*/
        this.bookRoom(room, bookedData);
        userOptional.get().getBooks().add(book);
        this.bookRepository.save(book);
    }


    private Room bookRoom(Room room,  BookedData bookedData) {
        if (room == null) {
            throw new IllegalArgumentException();
        }

        if (bookedData == null) {
            throw new IllegalArgumentException("Book data equals null");
        }
        //room.getBookedData().add(bookedData);
        /**TODO* тут створи Book і добав до Room*/
        this.roomRepository.save(room);
        return room;
    }

    public Room getRoom(long id) {
        /**TODO* додай тут помилку */
        return this.roomRepository.findById(id).get();
    }
}
