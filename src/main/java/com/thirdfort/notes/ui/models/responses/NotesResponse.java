package com.thirdfort.notes.ui.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotesResponse {

    private String notesId;
    private String body;
    private boolean archived;
    private String createdDate;
}
