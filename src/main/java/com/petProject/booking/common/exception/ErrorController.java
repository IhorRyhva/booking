package com.petProject.booking.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorController {

    /**TODO*add message*/

//    @ExceptionHandler(IncorrectMaxMinPriceException.class)
//    public ResponseEntity<ErrorMessage> price(IncorrectMaxMinPriceException ex) {
//        ErrorMessage errorMessage = ErrorMessage.builder()
//                .localDateTime(LocalDateTime.now())
//                .message(ex.getMessage())
//                .code(HttpStatus.BAD_REQUEST.toString())
//                .build();
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//    }
//
//    @ExceptionHandler(IncorrectBookTimeException.class)
//    public ResponseEntity<ErrorMessage> price(IncorrectBookTimeException ex) {
//        ErrorMessage errorMessage = ErrorMessage.builder()
//                .localDateTime(LocalDateTime.now())
//                .message(ex.getMessage())
//                .code(HttpStatus.BAD_REQUEST.toString())
//                .build();
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//    }
}
