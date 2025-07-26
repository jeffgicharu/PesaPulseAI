package com.pesapulse.transactionservice.controller;

import com.pesapulse.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for handling transaction-related API requests.
 */
 @RestController @RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Endpoint for uploading an M-PESA statement for processing.
     *
     * @param file The uploaded .csv file.
     * @param userId The ID of the user, extracted from the request header.
     * @return A ResponseEntity with status 202 (Accepted) and a batch ID.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadStatement( @RequestParam("file") MultipartFile file,
                                             @RequestHeader("X-User-Id") String userId) {
        // --- TODO: Replace Header with JWT Authentication ---
        // In a real production system, the userId would be extracted from a validated
        // JWT passed in the Authorization header, not from a custom header.
        // This avoids clients being able to impersonate other users.

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File is empty. Please upload a valid CSV file."));
        }

        try {
            transactionService.processStatement(file, userId);
            String batchId = "bch-" + UUID.randomUUID().toString();
            return new ResponseEntity<>(Map.of("message", "Statement accepted for processing.", "batchId", batchId), HttpStatus.ACCEPTED);
        } catch (IOException e) {
            // Log the exception for debugging purposes
            // logger.error("Error processing file for user {}", userId, e);
            return new ResponseEntity<>(Map.of("message", "Failed to process file. Please ensure it is a valid format."), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Catch other potential parsing errors
            // logger.error("An unexpected error occurred during file processing for user {}", userId, e);
            return new ResponseEntity<>(Map.of("message", "An unexpected error occurred. " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}