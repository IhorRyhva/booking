package com.petProject.booking.room.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.petProject.booking.common.exception.IncorrectBookTimeException;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
@JsonAutoDetect
public class BookedData {
    @NotNull
    private LocalDate startDate;

    @NotNull
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
