package com.petProject.booking.specification;

import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomCategory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

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
            return criteriaBuilder.equal(root.get("hotel").get("location").get("town"), town);
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
}
