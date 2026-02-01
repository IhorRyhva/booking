package com.petProject.booking.hotel.room;

import com.petProject.booking.hotel.Hotel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue
    private Long id;

    private int price;

    private RoomCategory category;

    private int number;

    @ElementCollection
    private List<BookedData> bookedData;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel-id")
    Hotel hotel;
}
