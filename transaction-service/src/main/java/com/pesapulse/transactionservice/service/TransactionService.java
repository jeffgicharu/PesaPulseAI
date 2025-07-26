package com.pesapulse.transactionservice.service;

import com.pesapulse.transactionservice.model.Transaction;
import com.pesapulse.transactionservice.repository.TransactionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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

/**
 * Service class containing the business logic for managing transactions.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // A formatter that matches the M-PESA statement date format (e.g., "2025-07-26 10:30:00")
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Processes an uploaded M-PESA statement file, parsing the CSV and saving the transactions.
     * The @Transactional annotation ensures that this entire operation is a single atomic transaction.
     * If any part fails, the entire operation will be rolled back.
     *
     * @param file The uploaded CSV file.
     * @param userId The ID of the user who uploaded the file.
     * @throws IOException if there is an error reading the file.
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

                // Parse and set the timestamp
                String completionTimeStr = csvRecord.get("Completion Time");
                transaction.setTimestamp(LocalDateTime.parse(completionTimeStr, DATE_TIME_FORMATTER));

                // Set transaction details
                transaction.setDetails(csvRecord.get("Details"));
                // For now, we'll just use the "Details" as the type. This can be refined later.
                transaction.setType(csvRecord.get("Details"));

                // Determine the amount. "Withdrawn" is negative, "Paid In" is positive.
                String withdrawnStr = csvRecord.get("Withdrawn").trim();
                String paidInStr = csvRecord.get("Paid In").trim();

                if (withdrawnStr != null && !withdrawnStr.isEmpty()) {
                    transaction.setAmount(new BigDecimal(withdrawnStr).negate());
                } else if (paidInStr != null && !paidInStr.isEmpty()) {
                    transaction.setAmount(new BigDecimal(paidInStr));
                } else {
                    transaction.setAmount(BigDecimal.ZERO);
                }

                // Category will be set later by our rules engine
                transaction.setCategory("Uncategorized");

                transactions.add(transaction);
            }

            if (!transactions.isEmpty()) {
                transactionRepository.saveAll(transactions);
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