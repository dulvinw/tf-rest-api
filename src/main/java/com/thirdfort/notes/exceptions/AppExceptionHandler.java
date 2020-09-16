package com.thirdfort.notes.exceptions;

import com.thirdfort.notes.exceptions.notesexceptions.InvalidNoteException;
import com.thirdfort.notes.exceptions.userexceptions.UserAuthenticationException;
import com.thirdfort.notes.exceptions.userexceptions.UserServiceException;
import com.thirdfort.notes.shared.enums.OperationStatus;
import com.thirdfort.notes.shared.enums.RequestOperationName;
import com.thirdfort.notes.ui.models.responses.OperationStatusModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = { UserServiceException.class })
    public ResponseEntity<Object> handleUserServiceExceptions(UserServiceException e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { UserAuthenticationException.class })
    public ResponseEntity<Object> handleUserAuthentictaionException(UserAuthenticationException e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleOtherExceptions(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { InvalidNoteException.class })
    public ResponseEntity<Object> handleInvalidNoteException(InvalidNoteException e) {
        String status = OperationStatus.FAILURE.name();
        String operationName = e.getOperationName().name();
        OperationStatusModel statusModel = new OperationStatusModel(status, operationName);

        return new ResponseEntity<>(statusModel, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
