package com.petProject.booking.booking;

import com.petProject.booking.room.Room;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "book_seq", sequenceName = "book_seq_name", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Embedded
    private BookedData bookedData;

    @Embedded
    private RoomInfo roomInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public Book(User user, BookedData bookedData, Room room) {
        this.user = user;
        this.bookedData = bookedData;
        this.room = room;
        this.roomInfo = new RoomInfo(room.getPrice(), room.getHotel().getNameOfHotel(), room.getNumber());
    }
}
