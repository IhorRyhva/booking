package com.petProject.booking;

import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.hotel.HotelRepository;
import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomCategory;
import com.petProject.booking.room.RoomRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class AiTest {
    @Autowired
    EmbeddingModel embeddingModel;

    @Autowired
    EntityManager em;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    RoomRepository roomRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("pgvector/pgvector:0.8.6-pg18-trixie");


    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    @Transactional
    void moreSimilarDescriptionMustBeHigherTest() {
        Hotel hotel = Hotel.builder()
                .rooms(new ArrayList<>())
                .star(Star.FIVE)
                .build();
        Room quiet = Room.builder()
                .description("peaceful, away from noise")
                .embedding(embeddingModel.embed("peaceful, away from noise"))
                .category(RoomCategory.BASIC)
                .hotel(hotel)
                .number(4)
                .build();
        Room noise = Room.builder()
                .description("near the nightclub")
                .embedding(embeddingModel.embed("near the nightclub"))
                .category(RoomCategory.BASIC)
                .hotel(hotel)
                .number(4)
                .build();
        hotel.getRooms().addAll(List.of(quiet, noise));
        hotelRepository.save(hotel);
        em.flush();
        em.clear();
        List<Room> rooms = roomRepository.searchNotRemovedAndByEmbeddingWithLimit(embeddingModel.embed("Quiet room"));
        assertEquals(1, rooms.size());
        assertEquals( quiet.getDescription(), rooms.getFirst().getDescription());
    }
}
