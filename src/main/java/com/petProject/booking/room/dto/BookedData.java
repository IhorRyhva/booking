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

    private LocalDate startDate;
    private LocalDate endDate;

    public BookedData(LocalDate startDate, LocalDate endDate) throws IncorrectBookTimeException {
        if ((startDate.isBefore(LocalDate.now()) || startDate.isAfter(endDate)) || startDate.equals(endDate)) {
            throw new IncorrectBookTimeException("Check date what you wrote");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public BookedData() {
    }

    @Override
    public String toString() {
        return "start: " + startDate + ", end: " + endDate;
    }

//    public boolean timeCrossing (BookedData bookedData) {
//        if (this.startDate.equals(bookedData.startDate) && this.endDate.equals(bookedData.endDate)) {
//            return true;
//        }
//        return this.startDate.isBefore(bookedData.getEndDate()) && this.endDate.isAfter(bookedData.getStartDate());
//    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookedData that = (BookedData) o;
        return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}
