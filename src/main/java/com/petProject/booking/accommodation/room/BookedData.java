package com.petProject.booking.accommodation.room;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.petProject.booking.exeption.IncorrectBookTimeException;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Data
@JsonAutoDetect
public class BookedData {

    private final LocalDate start;
    private final LocalDate end;

    public BookedData(LocalDate start, LocalDate end) {
        if (start.isBefore(LocalDate.now()) || start.isAfter(end)) {
            /*TODO**/
            //catch this exception
            throw new IncorrectBookTimeException("Check date what you wrote");
        }
        this.start = start;
        this.end = end;
    }

    public BookedData() {
        start = LocalDate.now();
        end = LocalDate.now().plusDays(2);
    }

    @Override
    public String toString() {
        return "start: " + start + ", end: " + end ;
    }

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
