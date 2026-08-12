package com.petProject.booking.specification;

import com.petProject.booking.booking.Book;
import com.petProject.booking.room.Room;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AdminSpecification {
    public static Specification<Room> existBookedRoom(LocalDate now) {
        return (root, query, criteriaBuilder) -> {
            if (now == null) {
                return criteriaBuilder.conjunction();
            }
            Subquery<Book> bookSubquery = query.subquery(Book.class);
            Root<Book> bookRoot = bookSubquery.from(Book.class);
            bookSubquery.select(bookRoot)
                    .where(criteriaBuilder.and(criteriaBuilder.equal(bookRoot.get("room"), root),
                            criteriaBuilder.greaterThanOrEqualTo(bookRoot.get("bookedData").get("startDate"), now)));
            return criteriaBuilder.exists(bookSubquery);
        };
    }

    public static Specification<Room> getRooms(Long hotelId) {
        return (root, _, criteriaBuilder) -> {
            if (hotelId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("hotel").get("id"), hotelId);
        };
    }

    public static Specification<Room> getRoom(Long roomId) {
        return (root, _, criteriaBuilder) -> {
          if (roomId == null) {
              return criteriaBuilder.conjunction();
          }
          return criteriaBuilder.equal(root.get("id"), roomId);
        };
    }
}
