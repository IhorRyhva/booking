package com.petProject.booking.exeption;

public class IncorrectBookTimeException extends RuntimeException {
    public IncorrectBookTimeException(String message) {
        super(message);
    }
}
