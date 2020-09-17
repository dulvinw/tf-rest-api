package com.thirdfort.notes.io.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Notes")
public class NotesDocument {

    @Id
    @NotNull
    private String noteId;

    private String body;

    private boolean archived = false;

    @NotNull
    private String createdDate;

    @NotNull
    private String uid;
}
