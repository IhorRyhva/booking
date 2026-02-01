package com.petProject.booking.hotel.room;

import jakarta.persistence.Embeddable;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BookedData {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public boolean timeCrossing (BookedData bookedData) {
        return this.start.isBefore(bookedData.getEnd()) && this.end.isAfter(bookedData.getStart());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookedData that = (BookedData) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
