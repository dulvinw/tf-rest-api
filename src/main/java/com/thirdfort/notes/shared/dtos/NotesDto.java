package com.thirdfort.notes.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesDto {
    private String noteId;

    private String uid;

    private String body;

    private String createdDate;

    private boolean archived;
}
