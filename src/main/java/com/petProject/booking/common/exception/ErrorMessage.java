package com.petProject.booking.common.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorMessage(
        String message,
        LocalDateTime localDateTime,
        String code
) {
}
