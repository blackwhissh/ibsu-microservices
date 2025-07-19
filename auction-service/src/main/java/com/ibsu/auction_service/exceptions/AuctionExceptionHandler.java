package com.ibsu.auction_service.exceptions;

import com.ibsu.common.exceptions.ExceptionResponse;
import com.ibsu.common.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AuctionExceptionHandler {
    @ExceptionHandler(AuctionAlreadyExistsException.class)
    public ResponseEntity<Object> handleAuctionAlreadyExistsException(AuctionAlreadyExistsException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setDateTime(LocalDateTime.now());
        response.setMessage("Auction already exists!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuctionNotFoundException.class)
    public ResponseEntity<Object> handleAuctionNotFoundException(AuctionNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setDateTime(LocalDateTime.now());
        response.setMessage("Auction not found!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongTimesException.class)
    public ResponseEntity<Object> handleAWrongTimesException(WrongTimesException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setDateTime(LocalDateTime.now());
        response.setMessage("Wrong times provided!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}