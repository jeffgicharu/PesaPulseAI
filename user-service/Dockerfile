# --- Stage 1: The Build Stage ---
# We use a Maven image that includes JDK 17 to compile our Java code.
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file first to leverage Docker's layer caching.
# This way, Maven dependencies are only re-downloaded if pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the application's source code
COPY src ./src

# Compile the application and package it into a .jar file.
# We skip running tests during the Docker build process.
RUN mvn package -DskipTests


# --- Stage 2: The Final Runtime Stage ---
# We use a minimal Java 17 runtime image for a smaller and more secure final image.
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the compiled .jar file from the 'build' stage into our final image.
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the user-service runs on.
EXPOSE 8081

# The command to run when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]
