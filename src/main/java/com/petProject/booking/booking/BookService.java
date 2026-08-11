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
import jakarta.validation.Valid;
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
    private final BookRepository bookRepository;
    private final RoomMapper roomMapper;


    /*TODO: add email to booking response*/

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
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        if (roomOptional.isEmpty()) {
            throw new NotExistingRoomException();
        }
        if (userOptional.isEmpty()) {
            throw new NotExistUserException();
        }
        User user = userOptional.get();
        Room room = roomOptional.get();
        if (room.isRemoved()) {
            throw new ForbiddenBookException();
        }
        Book book = new Book(user, bookedData, room);
        this.bookRepository.save(book);
    }


    public Room getRoom(long id) {
        Optional<Room> roomOptional = this.roomRepository.findById(id);
        if (roomOptional.isEmpty()) {
            throw new NotExistingRoomException();
        }
        return roomOptional.get();
    }
}
