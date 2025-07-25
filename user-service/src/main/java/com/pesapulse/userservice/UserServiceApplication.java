package com.pesapulse.userservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

 @SpringBootApplication
public class UserServiceApplication {

public static void main(String[] args) {
// --- Definitive .env Loading Solution ---
// Explicitly configure, load, and apply the .env file variables as System Properties.
// This guarantees they are available to Spring's @Value annotation processor during startup.
Dotenv dotenv = Dotenv.configure()
.directory("../") // Look for .env in the parent directory (project root)
.ignoreIfMissing()
.load();

// Set each loaded entry as a system property
dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
// --- End of Solution ---

SpringApplication.run(UserServiceApplication.class, args);
}

}