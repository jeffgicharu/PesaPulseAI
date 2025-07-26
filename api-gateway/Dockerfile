# --- Stage 1: The Build Stage ---
# Use a Maven image with JDK 17 to compile the application.
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application into a .jar file, skipping tests.
RUN mvn package -DskipTests


# --- Stage 2: The Final Runtime Stage ---
# Use a minimal Java 17 JRE image for the final container.
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the compiled .jar file from the build stage.
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the api-gateway runs on.
EXPOSE 8080

# The command to run when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]
