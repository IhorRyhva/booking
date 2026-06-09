package com.petProject.booking.booking;

import jakarta.validation.constraints.Email;

public record BookDTO(
        @Email
         String email,
         String userName,
         String nameOfHotel,
         int number
) {
    
}
