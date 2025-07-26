package com.pesapulse.transactionservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

 @SpringBootApplication
public class TransactionServiceApplication {

public static void main(String[] args) {
// Explicitly load the .env file from the project's root directory ("../").
// This ensures all environment variables are loaded before Spring starts.
Dotenv dotenv = Dotenv.configure()
.directory("../")
.ignoreIfMissing()
.load();

// Set each loaded entry as a system property so Spring's @Value can find them.
dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

SpringApplication.run(TransactionServiceApplication.class, args);
}

}