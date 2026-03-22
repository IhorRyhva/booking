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
        String town
) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        if (country != null && !country.equalsIgnoreCase(location.country)) {
            return false;
        }
        if (town != null && !town.equalsIgnoreCase(location.town)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, town);
    }
}
