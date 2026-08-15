package com.petProject.booking.specification;

import com.petProject.booking.booking.Book;
import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomCategory;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Vector;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class RoomSpecification {

    public static Specification<Room> filterByStar(Star star) {
        return (root, query, criteriaBuilder) -> {
            if (star == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("hotel").get("star"), star);
        };
    }

    public static Specification<Room> filterByCountry (String country) {
        return (root, query, criteriaBuilder) -> {
            if (country == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("hotel").get("location").get("country"), country);
        };
    }

    public static Specification<Room> filterByTown(String town) {
        return (root, query, criteriaBuilder) -> {
            if (town == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("hotel").get("location").get("city"), town);
        };
    }

    public static Specification<Room> filterByPrice(Integer min, Integer max) {
        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) {
                return criteriaBuilder.conjunction();
            }else if (min == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), max);
            } else if (max == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), min);
            } else {
                return criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), min),
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), max)
                );
            }
        };
    }

    public static Specification<Room> filterByCategory(RoomCategory category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Room> filterByDate(LocalDate start, LocalDate end) {
        return (root, query, criteriaBuilder) -> {
            if (start == null || end == null) {
                return criteriaBuilder.conjunction();
            }
            Subquery<Book> subquery = query.subquery(Book.class);
            Root<Book> bookRoot = subquery.from(Book.class);
            subquery.select(bookRoot)
                    .where(criteriaBuilder
                            .and(criteriaBuilder.equal(bookRoot.get("room"), root),
                                 criteriaBuilder.lessThan(bookRoot.get("bookedData").get("startDate"), end),
                                 criteriaBuilder.greaterThan(bookRoot.get("bookedData").get("endDate"), start))
                            );
            return criteriaBuilder.not(criteriaBuilder.exists(subquery));
        };
    }

    public static Specification<Room> getNotRemovedRoom() {
        return (root, query, criteriaBuilder) -> {
          return criteriaBuilder.equal(root.get("removed"), false);
        };
    }

    public static Specification<Room> getRoomByBedNumber(Integer bedNumber) {
        return (root, query, criteriaBuilder) -> {
          if (bedNumber == null) {
              return criteriaBuilder.conjunction();
          }
          return criteriaBuilder.equal(root.get("bedNumber"), bedNumber);
        };
    }
}
