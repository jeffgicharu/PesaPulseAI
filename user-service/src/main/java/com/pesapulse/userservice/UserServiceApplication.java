package com.pesapulse.userservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

 @SpringBootApplication
public class UserServiceApplication {

public static void main(String[] args) {
// Explicitly load the .env file from the project's root directory.
// We configure it to look one directory up ("../") from the service's execution path.
Dotenv dotenv = Dotenv.configure()
.directory("../") // <-- THE FIX IS HERE
.ignoreIfMissing()
.load();

SpringApplication.run(UserServiceApplication.class, args);
}

}
