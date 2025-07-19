package com.ibsu.auction_service.exceptions;

public class WrongTimesException extends RuntimeException {
    public WrongTimesException(String message) {
        super(message);
    }
}
