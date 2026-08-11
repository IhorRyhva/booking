package com.petProject.booking.room.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.petProject.booking.common.exception.IncorrectBookTimeException;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
@JsonAutoDetect
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookedData {
    private LocalDate startDate;

    private LocalDate endDate;

    public BookedData(LocalDate startDate, LocalDate endDate) throws IncorrectBookTimeException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date or end date is null");
        }

        if ((startDate.isBefore(LocalDate.now()) || startDate.isAfter(endDate)) || startDate.equals(endDate)) {
            throw new IncorrectBookTimeException("Check date what you wrote");
        }
        this.startDate = startDate;
        this.endDate = endDate;
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
