package com.pesapulse.transactionservice.service;

import com.pesapulse.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service class containing the business logic for managing transactions.
 */
 @Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Processes an uploaded M-PESA statement file.
     * This method will parse the CSV, convert rows to Transaction objects,
     * and save them to the database.
     *
     * @param file The uploaded CSV file.
     * @param userId The ID of the user who uploaded the file.
     * @throws IOException if there is an error reading the file.
     */
    public void processStatement(MultipartFile file, String userId) throws IOException {
        // TODO: Implement CSV parsing logic here.
        // 1. Read the input stream from the MultipartFile.
        // 2. Use a CSV parsing library to read rows.
        // 3. For each row, create a Transaction entity.
        // 4. Set the userId on each transaction.
        // 5. Save the list of transactions to the database using transactionRepository.saveAll().

        System.out.println("Processing file for user: " + userId);
        System.out.println("File name: " + file.getOriginalFilename());
        System.out.println("File size: " + file.getSize() + " bytes");
    }
}