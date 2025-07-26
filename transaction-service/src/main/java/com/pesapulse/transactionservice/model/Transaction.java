package com.pesapulse.transactionservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a single financial transaction entity.
 * This class is mapped to the "transactions" table in the PostgreSQL database.
 */
 @Data @Entity @Table(name = "transactions")
public class Transaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // Use database auto-incrementing for the ID
    private Long id;

    @Column(nullable = false)
    private String userId; // To associate the transaction with a user from the user-service

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @NotBlank(message = "Transaction type cannot be blank")
    private String type; // e.g., "Pay Bill", "Buy Goods"

    @NotBlank(message = "Transaction details cannot be blank")
    private String details; // e.g., "NAIVAS LTD", "JAVA HOUSE"

    @NotNull(message = "Amount cannot be null")
    @Column(precision = 19, scale = 2) // Suitable for financial calculations
    private BigDecimal amount;

    private String category; // e.g., "Groceries", "Dining Out"
}