package com.pesapulse.userservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

 @SpringBootApplication
public class UserServiceApplication {

public static void main(String[] args) {
// Explicitly load the .env file at the very beginning of startup.
// This ensures all environment variables are available before Spring starts building beans.
Dotenv.load();

SpringApplication.run(UserServiceApplication.class, args);
}

}