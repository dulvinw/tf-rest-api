package com.thirdfort.notes.services;

import com.thirdfort.notes.shared.dtos.NotesDto;

import java.util.List;

public interface NotesService {

    List<NotesDto> getNotesForUser(String uid);

    NotesDto getNoteForUser(String uid, String noteId);

    NotesDto createNote(NotesDto requestDto, String uid);

    NotesDto updateNote(String uid, String noteId, NotesDto requestDto);

    void deleteNote(String uid, String noteId);

    void toggleArchiveStatus(String uid, String noteId);

    List<NotesDto> getArchivedNotes(String uid);

    List<NotesDto> getLiveNotes(String uid);
}
