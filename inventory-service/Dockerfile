# Use a base image
FROM openjdk:17

# Set the working directory
WORKDIR /app

# Copy the service JAR file
COPY target/inventory-service-0.0.1-SNAPSHOT.jar .

# Set the command to run the service
CMD ["java", "-jar", "inventory-service-0.0.1-SNAPSHOT.jar"]
