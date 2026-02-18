package com.petProject.booking.accommodation.room;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.petProject.booking.accommodation.hotel.Hotel;
import com.petProject.booking.book.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
public class Room  {
    @Id
    @GeneratedValue
    private Long id;

    private int price;

    private RoomCategory category;

    private int number;

    @ElementCollection
    private List<BookedData> bookedData = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel-id")
    @JsonBackReference
    private Hotel hotel;

    @Override
    public String toString() {
        return "Room{" +
                "price=" + price +
                ", category=" + category +
                ", number=" + number +
                '}';
    }
}
