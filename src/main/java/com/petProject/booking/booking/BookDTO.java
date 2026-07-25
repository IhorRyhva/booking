package com.petProject.booking.booking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookDTO {
        @Email
        private String email;
        @NotBlank
        private String userName;
        @NotBlank
        private String nameOfHotel;
        private int number;
        private LocalDate start;
        private LocalDate end;
        private long roomId;

        public BookDTO() {
        }

        public BookDTO(String email, String userName) {
            this.email = email;
            this.userName = userName;
        }
}
