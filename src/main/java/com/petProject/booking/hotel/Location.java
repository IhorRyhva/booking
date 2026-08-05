package com.petProject.booking.hotel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.Embeddable;
import lombok.Builder;

import java.util.Objects;

@Builder
@Embeddable
@JsonAutoDetect
public record Location(
        String country,
        String city
) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        if (country != null && !country.equalsIgnoreCase(location.country)) {
            return false;
        }
        if (city != null && !city.equalsIgnoreCase(location.city)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city);
    }
}
