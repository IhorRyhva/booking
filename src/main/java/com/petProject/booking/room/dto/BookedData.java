package com.petProject.booking.room.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.petProject.booking.common.exception.IncorrectBookTimeException;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Data
@JsonAutoDetect
public class BookedData {

    private LocalDate start;
    private LocalDate end;

    public BookedData(LocalDate start, LocalDate end) throws IncorrectBookTimeException {
        if ((start.isBefore(LocalDate.now()) || start.isAfter(end)) || start.equals(end)) {
            throw new IncorrectBookTimeException("Check date what you wrote");
        }
        this.start = start;
        this.end = end;
    }

    public BookedData() {
    }

    @Override
    public String toString() {
        return "start: " + start + ", end: " + end ;
    }

    public boolean timeCrossing (BookedData bookedData) {
        if (this.start.equals(bookedData.start) && this.end.equals(bookedData.end)) {
            return true;
        }
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
