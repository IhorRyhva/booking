package com.petProject.booking.exeption;

public class IncorrectMaxMinPriceException extends RuntimeException {
    public IncorrectMaxMinPriceException(String message) {
        super(message);
    }
}
