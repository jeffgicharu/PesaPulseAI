package com.pesapulse.userservice.repository;

import com.pesapulse.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the User entity.
 * This interface provides the mechanism for storage, retrieval,
 * and search behavior for User objects.
 */
 @Repository // Declares this interface as a Spring bean for component scanning
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by their email address.
     * Spring Data MongoDB will automatically implement this method based on its name.
     *
     * @param email the email address to search for.
     * @return an Optional containing the found user, or an empty Optional if no user is found.
     */
    Optional<User> findByEmail(String email);
}