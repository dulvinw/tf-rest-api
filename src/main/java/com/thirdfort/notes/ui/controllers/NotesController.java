package com.thirdfort.notes.ui.controllers;

import com.thirdfort.notes.services.NotesService;
import com.thirdfort.notes.shared.dtos.NotesDto;
import com.thirdfort.notes.shared.enums.OperationStatus;
import com.thirdfort.notes.shared.enums.RequestOperationName;
import com.thirdfort.notes.ui.models.requests.CreateUpdateNoteRequest;
import com.thirdfort.notes.ui.models.responses.NotesResponse;
import com.thirdfort.notes.ui.models.responses.OperationStatusModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    NotesService notesService;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping
    public List<NotesResponse> getNotes(Principal principal) {
        String uid = principal.getName();
        List<NotesDto> notes = notesService.getNotesForUser(uid);
        List<NotesResponse> notesResponses = new ArrayList<>();

        for (NotesDto note : notes) {
            notesResponses.add(modelMapper.map(note, NotesResponse.class));
        }

        return notesResponses;
    }

    @GetMapping(path = "/{noteId}")
    public NotesResponse getNote(Principal principal, @PathVariable String noteId) {
        String uid = principal.getName();
        NotesDto note = notesService.getNoteForUser(uid, noteId);

        NotesResponse response = new NotesResponse();
        if (note == null) {
            return response;
        }

        response = modelMapper.map(note, NotesResponse.class);
        return response;
    }

    @PostMapping
    public NotesResponse createNote(Principal principal, @Valid @RequestBody CreateUpdateNoteRequest request) {
        String uid = principal.getName();

        NotesDto requestDto = modelMapper.map(request, NotesDto.class);
        NotesDto note = notesService.createNote(requestDto, uid);

        return modelMapper.map(note, NotesResponse.class);
    }

    @PutMapping(path = "/{noteId}")
    public NotesResponse updateNote(Principal principal, @Valid @RequestBody CreateUpdateNoteRequest request,
                                    @PathVariable String noteId) {

        String uid = principal.getName();

        NotesDto requestDto = modelMapper.map(request, NotesDto.class);
        NotesDto note = notesService.updateNote(uid, noteId, requestDto);

        return modelMapper.map(note, NotesResponse.class);
    }

    @DeleteMapping(path = "/{noteId}")
    public OperationStatusModel deleteNote(Principal principal, @PathVariable String noteId) {
        String uid = principal.getName();

        notesService.deleteNote(uid, noteId);

        return getOperationSuccessModel(RequestOperationName.DELETE);
    }

    private OperationStatusModel getOperationSuccessModel(RequestOperationName operationName) {
        String operationStatus = OperationStatus.SUCCESS.name();

        return new OperationStatusModel(operationStatus, operationName.name());
    }

    @GetMapping(path = "/{noteId}/toggleArchive")
    public OperationStatusModel toggleArchiveStatus(Principal principal, @PathVariable String noteId) {
        String uid = principal.getName();

        notesService.toggleArchiveStatus(uid, noteId);

        return getOperationSuccessModel(RequestOperationName.TOGGLE_ARCHIVE);
    }

    @GetMapping(path = "/archived")
    public List<NotesResponse> getArchivedNotes(Principal principal) {
        String uid = principal.getName();

        List<NotesDto> notesDtos = notesService.getArchivedNotes(uid);

        List<NotesResponse> notesResponses = new ArrayList<>();
        for (NotesDto notesDto : notesDtos) {
            notesResponses.add(modelMapper.map(notesDto, NotesResponse.class));
        }

        return notesResponses;
    }

    @GetMapping(path = "/live")
    public List<NotesResponse> getLiveNotes(Principal principal) {
        String uid = principal.getName();

        List<NotesDto> notesDtos = notesService.getLiveNotes(uid);

        List<NotesResponse> notesResponses = new ArrayList<>();
        for (NotesDto notesDto : notesDtos) {
            notesResponses.add(modelMapper.map(notesDto, NotesResponse.class));
        }

        return notesResponses;
    }
}
