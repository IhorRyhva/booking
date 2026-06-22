package com.petProject.booking.hotel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petProject.booking.room.Room;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "hotel_seq", sequenceName = "hotel_seq_name", allocationSize = 50)
    private Long id;

    private String nameOfHotel;

    private Star star;

    @Embedded
    private Location location;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "hotel")
    @JsonManagedReference
    private List<Room> rooms;

    @Override
    public String toString() {
        return "Hotel{" +
                "nameOfHotel='" + nameOfHotel + '\'' +
                ", star=" + star +
                ", location=" + location +
                ", rooms=" + rooms +
                '}';
    }
}
