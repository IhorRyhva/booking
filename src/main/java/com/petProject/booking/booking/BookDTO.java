package com.petProject.booking.booking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
        @Email
        private String email;
        @NotBlank
        private String userName;
        @NotBlank
        private String nameOfHotel;
        int number;

        public BookDTO() {
        }

        public BookDTO(String email, String userName) {
            this.email = email;
            this.userName = userName;
        }
}
