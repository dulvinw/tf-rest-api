package com.thirdfort.notes.io.repositories;

import com.thirdfort.notes.io.documents.NotesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends MongoRepository<NotesDocument, String> {

    // List all documents with userId
    @Query("{uid: '?0'}")
    List<NotesDocument> findByUserId(String userId);

    // Find document with noteId and uid. Only one could be present
    @Query("{'uid': ?0, 'noteId': ?1}")
    NotesDocument findNote(String uid, String noteId);

    // Find documents with uid and archive status
    @Query("{'uid': ?0, 'archived': ?1}")
    List<NotesDocument> findByUserIdAndArchiveStatus(String uid, boolean archiveStatus);
}
