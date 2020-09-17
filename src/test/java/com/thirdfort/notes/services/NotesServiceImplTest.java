package com.thirdfort.notes.services;

import com.thirdfort.notes.io.documents.NotesDocument;
import com.thirdfort.notes.io.repositories.NotesRepository;
import com.thirdfort.notes.services.impl.NotesServiceImpl;
import com.thirdfort.notes.shared.Utils;
import com.thirdfort.notes.shared.dtos.NotesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NotesServiceImplTest {

    private static final boolean UPDATED_ARCHIVED = true;

    public static final String UPDATED_BODY = "UPDATED BODY";

    private final String NOTE_ID = "abc132cjsd";

    private final String USER_ID = "vkahdkfh123";

    private final String BODY = "SAMPLE BODY";

    private final String CREATED_DATE = "16/09/2020";

    private final boolean ARCHIVED = false;

    @InjectMocks
    private NotesServiceImpl notesService;

    @Mock
    private NotesRepository notesRepository;

    @Mock
    private Utils utils;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetNotesForUser() {
        when(notesRepository.findByUserId(anyString())).thenReturn(getMockNotesDocuments());

        List<NotesDto> dtos = notesService.getNotesForUser(USER_ID);
        List<NotesDto> expectedDtos = getMockNotesDtoList();

        assertThat(dtos.size()).isEqualTo(expectedDtos.size());
        for (int i = 0; i < dtos.size(); i++) {
            assertThat(dtos.get(i).getUid()).isEqualTo(expectedDtos.get(i).getUid());
            assertThat(dtos.get(i).getCreatedDate()).isEqualTo(expectedDtos.get(i).getCreatedDate());
            assertThat(dtos.get(i).getBody()).isEqualTo(expectedDtos.get(i).getBody());
            assertThat(dtos.get(i).getNoteId()).isEqualTo(expectedDtos.get(i).getNoteId());
            assertThat(dtos.get(i).isArchived()).isEqualTo(expectedDtos.get(i).isArchived());
        }
    }

    @Test
    public void testGetNoteForUser() {
        when(notesRepository.findNote(anyString(), anyString())).thenReturn(getMockNoteDocument());

        NotesDto response = notesService.getNoteForUser(USER_ID, NOTE_ID);

        assertThat(response).isNotNull();
        assertThat(response.getCreatedDate()).isEqualTo(CREATED_DATE);
        assertThat(response.getBody()).isEqualTo(BODY);
        assertThat(response.getUid()).isEqualTo(USER_ID);
        assertThat(response.getNoteId()).isEqualTo(NOTE_ID);
    }

    @Test
    public void testGetInvalidNoteForUser() {
        when(notesRepository.findNote(anyString(), anyString())).thenReturn(null);

        NotesDto response = notesService.getNoteForUser(USER_ID, NOTE_ID);
        assertThat(response).isNull();
    }

    @Test
    public void testCreateNote() {
        when(utils.generateUserId(anyInt())).thenReturn(NOTE_ID);
        when(notesRepository.save(any(NotesDocument.class))).thenReturn(getMockNoteDocument());

        NotesDto response = notesService.createNote(getMockNotesDto(), USER_ID);
        assertThat(response).isNotNull();
        assertThat(response.getUid()).isEqualTo(USER_ID);
        assertThat(response.getNoteId()).isEqualTo(NOTE_ID);
        assertThat(response.getCreatedDate()).isEqualTo(CREATED_DATE);
        assertThat(response.isArchived()).isEqualTo(ARCHIVED);
        assertThat(response.getBody()).isEqualTo(BODY);
    }

    @Test
    public void testUpdateNote() {
        when(notesRepository.save(any(NotesDocument.class))).thenReturn(getUpdatedNoteDocument());
        when(notesRepository.findNote(anyString(), anyString())).thenReturn(getMockNoteDocument());

        NotesDto initialDto = getMockNotesDto();
        NotesDto response = notesService.updateNote(USER_ID, NOTE_ID, initialDto);

        assertThat(response).isNotNull();
        assertThat(response.getUid()).isEqualTo(USER_ID);
        assertThat(response.getNoteId()).isEqualTo(NOTE_ID);
        assertThat(response.getCreatedDate()).isEqualTo(CREATED_DATE);
        assertThat(response.isArchived()).isEqualTo(UPDATED_ARCHIVED);
        assertThat(response.getBody()).isEqualTo(UPDATED_BODY);
    }

    @Test
    public void testGetArchivedNotes() {
        assertArchiveStatus(true);
    }

    @Test
    public void testGetLiveNotes() {
        assertArchiveStatus(false);
    }

    private void assertArchiveStatus(boolean archiveStatus) {
        when(notesRepository.findByUserIdAndArchiveStatus(anyString(), anyBoolean()))
                .thenReturn(getNotesWithArchiveStatus(archiveStatus));

        List<NotesDto> response = notesService.getArchivedNotes(USER_ID);
        List<NotesDto> expected = getDtosWithArchiveStatus(archiveStatus);
        assertThat(response.size()).isEqualTo(expected.size());

        for (int i = 0; i < response.size(); i++) {
            assertThat(response.get(i).isArchived()).isEqualTo(expected.get(i).isArchived());
            assertThat(response.get(i).getUid()).isEqualTo(expected.get(i).getUid());
            assertThat(response.get(i).getNoteId()).isEqualTo(expected.get(i).getNoteId());
            assertThat(response.get(i).getBody()).isEqualTo(expected.get(i).getBody());
            assertThat(response.get(i).getCreatedDate()).isEqualTo(expected.get(i).getCreatedDate());
        }
    }

    private NotesDocument getMockNoteDocument() {
        NotesDocument document = new NotesDocument();
        document.setArchived(ARCHIVED);
        document.setBody(BODY);
        document.setCreatedDate(CREATED_DATE);
        document.setNoteId(NOTE_ID);
        document.setUid(USER_ID);

        return document;
    }

    private NotesDto getMockNotesDto() {
        NotesDocument document = getMockNoteDocument();

        ModelMapper mapper = new ModelMapper();

        return mapper.map(document, NotesDto.class);
    }

    private List<NotesDto> getMockNotesDtoList() {
        NotesDto dto1 = new NotesDto();
        dto1.setNoteId("asdkfjhskjf");
        dto1.setUid("vkahdkfh123");
        dto1.setArchived(false);
        dto1.setBody("Test Body 1");
        dto1.setCreatedDate("16/09/2020");

        NotesDto dto2 = new NotesDto();
        dto2.setNoteId("cxkjhsakf");
        dto2.setUid("vkahdkfh123");
        dto2.setArchived(true);
        dto2.setBody("Test Body 2");
        dto2.setCreatedDate("15/09/2020");

        List<NotesDto> dtos = new ArrayList<>();
        dtos.add(dto1);
        dtos.add(dto2);

        return dtos;
    }

    private List<NotesDocument> getMockNotesDocuments() {
        List<NotesDto> dtos = getMockNotesDtoList();

        ModelMapper mapper = new ModelMapper();

        List<NotesDocument> documents = new ArrayList<>();
        for (NotesDto dto : dtos) {
            documents.add(mapper.map(dto, NotesDocument.class));
        }

        return documents;
    }

    private NotesDocument getUpdatedNoteDocument() {
        NotesDocument document = getMockNoteDocument();
        document.setArchived(UPDATED_ARCHIVED);
        document.setBody(UPDATED_BODY);

        return document;
    }

    private List<NotesDocument> getNotesWithArchiveStatus(boolean archiveStatus) {
        List<NotesDocument> allList = getMockNotesDocuments();
        for (NotesDocument document : allList) {
            document.setArchived(archiveStatus);
        }

        return allList;
    }

    private List<NotesDto> getDtosWithArchiveStatus(boolean archiveStatus) {
        ModelMapper modelMapper = new ModelMapper();
        List<NotesDto> dtos = new ArrayList<>();

        List<NotesDocument> documents = getNotesWithArchiveStatus(archiveStatus);
        for (NotesDocument document : documents) {
            dtos.add(modelMapper.map(document, NotesDto.class));
        }

        return dtos;
    }
}
