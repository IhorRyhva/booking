package com.petProject.booking.room;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.petProject.booking.booking.Book;
import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.room.dto.BookedData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
public class Room  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "room_seq", sequenceName = "room_seq_name", allocationSize = 50)
    private Long id;

    private int price;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 10, nullable = false)
    @NotNull
    private RoomCategory category;

    private int number;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id")
    @JsonBackReference
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Book> books;

    @Override
    public String toString() {
        return "Room{" +
                "price=" + price +
                ", category=" + category +
                ", number=" + number +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Room room = (Room) object;
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
