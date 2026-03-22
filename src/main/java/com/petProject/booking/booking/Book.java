package com.petProject.booking.booking;

import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.room.dto.RoomResponse;
import com.petProject.booking.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Embedded
    private BookedData bookedData;

    @Embedded
    private RoomResponse room;
}
