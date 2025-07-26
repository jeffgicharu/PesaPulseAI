package com.pesapulse.transactionservice.service;

import com.pesapulse.transactionservice.model.Transaction;
import com.pesapulse.transactionservice.repository.TransactionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class containing the business logic for managing transactions.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;

    // Define the queue name as a constant
    private static final String QUEUE_NAME = "transaction_events";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, RabbitTemplate rabbitTemplate) {
        this.transactionRepository = transactionRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Processes an uploaded M-PESA statement file, saves the transactions,
     * and publishes an event to RabbitMQ.
     */
    @Transactional
    public void processStatement(MultipartFile file, String userId) throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                Transaction transaction = new Transaction();
                transaction.setUserId(userId);
                transaction.setTimestamp(LocalDateTime.parse(csvRecord.get("Completion Time"), DATE_TIME_FORMATTER));
                transaction.setDetails(csvRecord.get("Details"));
                transaction.setType(csvRecord.get("Details"));

                String withdrawnStr = csvRecord.get("Withdrawn").trim();
                String paidInStr = csvRecord.get("Paid In").trim();

                if (withdrawnStr != null && !withdrawnStr.isEmpty()) {
                    transaction.setAmount(new BigDecimal(withdrawnStr).negate());
                } else if (paidInStr != null && !paidInStr.isEmpty()) {
                    transaction.setAmount(new BigDecimal(paidInStr));
                } else {
                    transaction.setAmount(BigDecimal.ZERO);
                }
                transaction.setCategory("Uncategorized");
                transactions.add(transaction);
            }

            if (!transactions.isEmpty()) {
                // Save all transactions to the database
                transactionRepository.saveAll(transactions);

                // --- Publish Event to RabbitMQ ---
                // Create a simple message payload
                Map<String, String> message = Map.of("userId", userId, "status", "completed");
                // Send the message to our queue
                rabbitTemplate.convertAndSend(QUEUE_NAME, message);
                System.out.println("Successfully published event for userId: " + userId + " to queue: " + QUEUE_NAME);
            }
        } catch (IOException e) {
            // Re-throw as IOException to be handled by the controller
            throw e;
        } catch (Exception e) {
            // Catch any other parsing or data conversion errors
            throw new IOException("Error parsing CSV record: " + e.getMessage(), e);
        }
    }
}