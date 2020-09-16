package com.thirdfort.notes.io.repositories;

import com.thirdfort.notes.io.entities.NotesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends MongoRepository<NotesDocument, String> {

    @Query("{uid: '?0'}")
    List<NotesDocument> findByUserId(String userId);

    @Query("{'uid': ?0, 'noteId': ?1}")
    NotesDocument findNote(String uid, String noteId);

    @Query("{'uid': ?0, 'archived': ?1}")
    List<NotesDocument> findByUserIdAndArchiveStatus(String uid, boolean archiveStatus);
}
