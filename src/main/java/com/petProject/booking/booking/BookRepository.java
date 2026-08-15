package com.petProject.booking.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(nativeQuery = true, value = """
        SELECT *
        FROM book WHERE book.user_id = :userId
        ORDER BY book.start_date DESC
""")
    List<Book> getBooksByUser(long userId);
}
