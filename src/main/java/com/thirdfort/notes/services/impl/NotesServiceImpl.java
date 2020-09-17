package com.thirdfort.notes.services.impl;

import com.thirdfort.notes.exceptions.notesexceptions.InvalidNoteException;
import com.thirdfort.notes.io.documents.NotesDocument;
import com.thirdfort.notes.io.repositories.NotesRepository;
import com.thirdfort.notes.services.NotesService;
import com.thirdfort.notes.shared.Utils;
import com.thirdfort.notes.shared.dtos.NotesDto;
import com.thirdfort.notes.shared.enums.RequestOperationName;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotesServiceImpl implements NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private Utils utils;

    ModelMapper modelMapper = new ModelMapper();

    /**
     * Get all notes belongs to a user
     * @param uid Users public id
     * @return list of notes
     */
    @Override
    public List<NotesDto> getNotesForUser(String uid) {
        List<NotesDocument> notesDocuments = notesRepository.findByUserId(uid);
        return getNotesDtos(notesDocuments);
    }

    /**
     * Get a single note
     * @param uid user's id to which the note belongs to
     * @param noteId id of the note
     * @return single note if note is available `null` if not
     */
    @Override
    public NotesDto getNoteForUser(String uid, String noteId) {
        NotesDocument document = notesRepository.findNote(uid, noteId);

        if (document == null) {
            return null;
        }

        return modelMapper.map(document, NotesDto.class);
    }

    /**
     * Create a note
     * @param requestDto content of the note
     * @param uid the user to which this note belong
     * @return created note
     */
    @Override
    public NotesDto createNote(NotesDto requestDto, String uid) {
        NotesDocument document = modelMapper.map(requestDto, NotesDocument.class);

        String notesId = utils.generateUserId(30);
        document.setNoteId(notesId);

        document.setUid(uid);

        NotesDocument response = notesRepository.save(document);
        return modelMapper.map(response, NotesDto.class);
    }

    /**
     * Update a note
     * @param uid user's id to which the note belongs to
     * @param noteId id of the note
     * @param requestDto content to be updated
     * @return updated note
     */
    @Override
    public NotesDto updateNote(String uid, String noteId, NotesDto requestDto) {
        NotesDocument document = checkIfValidNoteForCurrentUser(uid, noteId, RequestOperationName.UPDATE);

        document.setBody(requestDto.getBody());
        document.setArchived(requestDto.isArchived());

        NotesDocument updatedNote = notesRepository.save(document);
        return modelMapper.map(updatedNote, NotesDto.class);
    }

    /**
     * Delete a note from store
     * @param uid user's id to which the note belongs to
     * @param noteId id of the note
     */
    @Override
    public void deleteNote(String uid, String noteId) {
        NotesDocument document = checkIfValidNoteForCurrentUser(uid, noteId, RequestOperationName.DELETE);
        notesRepository.delete(document);
    }

    /**
     * Toggle the archive status of a given note
     * @param uid user's id to which the note belongs to
     * @param noteId id of the note
     */
    @Override
    public void toggleArchiveStatus(String uid, String noteId) {
        NotesDocument document = checkIfValidNoteForCurrentUser(uid, noteId, RequestOperationName.TOGGLE_ARCHIVE);

        document.setArchived(!document.isArchived());
        notesRepository.save(document);
    }

    /**
     * Get all archived notes of a user
     * @param uid user's id to which the note belongs to
     * @return all notes which are archived
     */
    @Override
    public List<NotesDto> getArchivedNotes(String uid) {
        List<NotesDocument> notesDocuments = notesRepository.findByUserIdAndArchiveStatus(uid, true);

        return getNotesDtos(notesDocuments);
    }

    /**
     * Get all notes which aren't archived
     * @param uid user's id to which the note belongs to
     * @return all notes which are live
     */
    @Override
    public List<NotesDto> getLiveNotes(String uid) {
        List<NotesDocument> notesDocuments = notesRepository.findByUserIdAndArchiveStatus(uid, false);

        return getNotesDtos(notesDocuments);
    }

    private List<NotesDto> getNotesDtos(List<NotesDocument> notesDocuments) {
        List<NotesDto> notesDtos = new ArrayList<>();
        for (NotesDocument notesDocument : notesDocuments) {
            notesDtos.add(modelMapper.map(notesDocument, NotesDto.class));
        }

        return notesDtos;
    }

    private NotesDocument checkIfValidNoteForCurrentUser(String uid, String noteId, RequestOperationName operation) {
        NotesDocument document = notesRepository.findNote(uid, noteId);
        if (document == null) {
            throw new InvalidNoteException("Unauthorized note or invalid note.", operation);
        }

        return document;
    }
}
