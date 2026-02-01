package com.petProject.booking.hotel;

import com.petProject.booking.hotel.room.Room;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue
    private Long id;

    private String nameOfHotel;

    private Star star;

    @Embedded
    private Location location;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
    private List<Room> rooms;
}
