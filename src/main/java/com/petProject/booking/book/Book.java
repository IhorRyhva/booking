package com.petProject.booking.book;

import com.petProject.booking.accommodation.room.BookedData;
import com.petProject.booking.accommodation.room.RoomResponse;
import com.petProject.booking.user.User;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
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
