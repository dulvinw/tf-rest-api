package com.thirdfort.notes.exceptions.userexceptions;

public class UserAuthenticationException extends RuntimeException{

    public UserAuthenticationException(String message) {
        super(message);
    }
}
