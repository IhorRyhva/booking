package com.petProject.booking;

import com.petProject.booking.book.Book;
import com.petProject.booking.hotel.Star;
import com.petProject.booking.user.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingApplication {

	public static void main(String[] args) {
        System.out.println(Star.FIVE.name());
		SpringApplication.run(BookingApplication.class, args);
	}

}
