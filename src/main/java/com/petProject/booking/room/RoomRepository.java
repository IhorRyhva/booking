package com.petProject.booking.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    @Query(nativeQuery = true, value = """
            SELECT *
            FROM room
            WHERE removed = false
            AND (embedding <=> CAST(:embedding AS vector)) <= :distance
            ORDER BY embedding <=> CAST(:embedding AS vector)
            LIMIT :limit
""")
    List<Room> searchNotRemovedAndByEmbeddingWithLimit(float[] embedding, float distance, int limit);
}
