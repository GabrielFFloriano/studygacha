package com.gacha.studygacha.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Unable to found user with id '" + id);
    }
}
