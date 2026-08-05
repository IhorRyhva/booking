package com.petProject.booking.tool;

import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.room.Room;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
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
}
