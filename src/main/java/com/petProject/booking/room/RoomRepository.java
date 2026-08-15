package com.petProject.booking.room;

import com.petProject.booking.hotel.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    @Query(nativeQuery = true, value = """
            SELECT *
            FROM room
            WHERE removed = false
            ORDER BY embedding <=> CAST(:embedding AS vector)
""")
    List<Room> searchNotRemovedAndByEmbeddingWithLimit(String embedding);

    @Query(nativeQuery = true, value = """
            SELECT room.*
            FROM room
            JOIN hotel ON room.hotel_id = hotel.id
            WHERE (:country IS NULL OR :country = country)
            AND (:city IS NULL OR :city = city)
            AND (:min IS NULL OR room.price >= :min)
            AND (:max IS NULL OR room.price <= :max)
            AND (:roomCategory IS NULL OR :roomCategory = category)
            AND (:star IS NULL OR :star = star)
            AND (:bedNumber IS NULL OR bed_number = :bedNumber)
            AND (room.removed = false)
            AND NOT EXISTS(SELECT 1
                           FROM book
                           WHERE book.room_id = room.id
                           AND end_date > :start
                           AND start_date < :end)
            ORDER BY embedding <=> CAST(:embedding AS vector), room.id
            LIMIT :limit
""")
    List<Room> searchRooms(
            @Param("country") String country,
            @Param("city") String city,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("min") Integer min,
            @Param("max") Integer max,
            @Param("roomCategory") RoomCategory roomCategory,
            @Param("star") Star star,
            @Param("bedNumber") Integer bedNumber,
            @Param("embedding") String embedding,
            @Param("limit") int limit
    );
}
