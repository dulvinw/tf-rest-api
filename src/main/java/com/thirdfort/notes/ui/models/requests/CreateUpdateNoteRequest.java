package com.thirdfort.notes.ui.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CreateUpdateNoteRequest {

    @NotBlank(message = "Body cannot be empty")
    private String body;

    @NotBlank(message = "Date cannot be empty")
    private String createdDate;

    @NotBlank(message = "Archived status cannot be empty")
    private boolean archived;
}
