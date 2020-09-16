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

    @NotBlank
    private String body;

    @NotBlank
    private String createdDate;

    @NotBlank
    private boolean archived;
}
