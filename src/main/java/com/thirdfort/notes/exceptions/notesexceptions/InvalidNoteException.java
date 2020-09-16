package com.thirdfort.notes.exceptions.notesexceptions;

import com.thirdfort.notes.shared.enums.RequestOperationName;

public class InvalidNoteException extends RuntimeException {
    private RequestOperationName operationName;

    public InvalidNoteException(String message, RequestOperationName operationName) {
        super(message);
        this.operationName = operationName;
    }

    public RequestOperationName getOperationName() {
        return operationName;
    }
}
