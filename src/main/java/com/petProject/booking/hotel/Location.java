package com.petProject.booking.hotel;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Builder
@Embeddable
public record Location (
        String country,
        String town,
        String address
) {
}
