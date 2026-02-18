package com.petProject.booking.accommodation.hotel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petProject.booking.accommodation.room.Room;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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
    @GeneratedValue
    private Long id;

    private String nameOfHotel;

    private Star star;

    @Embedded
    private Location location;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
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
