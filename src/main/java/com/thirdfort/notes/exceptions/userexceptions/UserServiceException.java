package com.thirdfort.notes.exceptions.userexceptions;

public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = 5219066035600211559L;

    public UserServiceException(String message) {
        super(message);
    }
}

