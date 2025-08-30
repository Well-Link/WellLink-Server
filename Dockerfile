# Build stage: use Gradle with JDK 17 to compile the Spring Boot app
FROM gradle:8.10.2-jdk17-alpine AS build

WORKDIR /app

# Copy full source
COPY . .

# Ensure wrapper is Unix-formatted and executable (fixes CRLF & permission issues)
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Build the application (skip tests)
RUN ./gradlew clean build -x test --no-daemon


# Runtime stage: slim JRE image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Render provides PORT env var; expose for local use
ENV PORT=8080
EXPOSE 8080

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/hackathon_v2-0.0.1-SNAPSHOT.jar app.jar

# Run with server.port bound to PORT for Render
CMD ["sh","-c","java -Dserver.port=$PORT -jar app.jar"]


