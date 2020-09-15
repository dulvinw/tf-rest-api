package com.thirdfort.notes.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required field"),
    RECORD_ALREADY_EXIST("User Record already exists");

    private String errorMessage;
}
