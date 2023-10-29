# Stage 1: Build the Spring Boot application
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot project source code to the container
COPY . /app

# Build the application using Maven
RUN ./mvnw install -"Dmaven".test.skip=true

# Copy the built JAR
COPY /app/target/service-1.0.jar /app/app.jar

# Expose the port that your Spring Boot app is listening on (change this to your app's port)
EXPOSE 8080

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]
