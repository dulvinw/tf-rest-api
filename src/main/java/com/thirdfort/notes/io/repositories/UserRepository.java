package com.thirdfort.notes.io.repositories;

import com.thirdfort.notes.io.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> {

    // List all users with email
    @Query("{email: '?0'}")
    List<UserDocument> findByEmail(String email);
}
