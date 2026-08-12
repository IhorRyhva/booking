package com.petProject.booking.tool;

import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

@Service
@AllArgsConstructor
public class ExtractDataFromBooking {
    private final EntityManager entityManager;
    private RoomRepository roomRepository;
    private final EmbeddingModel embeddingModel;

    @Transactional
    public void extractData() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Hotel> hotels = objectMapper.readValue(new File(getClass().getResource("/seed_data.json").getFile()),
                new TypeReference<>() {
                });
        int count = 0;
        for (Hotel hotel: hotels) {
            for (Room room: hotel.getRooms()) {
                room.setHotel(hotel);
            }
            count++;
            entityManager.persist(hotel);
            if (count % 250 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void addEmbedding() {
        int pageNumber = 0;
        int pageSize = 250;
        Pageable pageable;
        Page<Room> roomPage;
        do {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
            roomPage = roomRepository.findAll(pageable);
            for (Room room: roomPage) {
                room.setEmbedding(embeddingModel.embed(room.getDescription()));
            }
            entityManager.flush();
            entityManager.clear();
            pageNumber++;

        } while (roomPage.hasNext());
    }

}
