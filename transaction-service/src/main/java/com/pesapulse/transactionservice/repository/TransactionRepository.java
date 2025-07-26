package com.pesapulse.transactionservice.repository;

import com.pesapulse.transactionservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transaction entity.
 * This interface provides the mechanism for storage, retrieval,
 * and search behavior for Transaction objects.
 */
 @Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Spring Data JPA will automatically provide implementations for standard
    // CRUD operations (save, findById, findAll, delete, etc.).

    // We can add custom query methods here in the future if needed, for example:
    // List<Transaction> findByUserId(String userId);
}