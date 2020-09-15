package com.thirdfort.notes.exceptions;

import com.thirdfort.notes.exceptions.userexceptions.UserAuthenticationException;
import com.thirdfort.notes.exceptions.userexceptions.UserServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(value = { UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceExceptions(UserServiceException e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { UserAuthenticationException.class })
    public ResponseEntity<Object> handleUserAuthentictaionException(UserAuthenticationException e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                        WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
