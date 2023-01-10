package com.example.Test.exception;

import com.example.Test.model.User;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        System.out.println("User with id: " + id + ", not found!");
    }
}
