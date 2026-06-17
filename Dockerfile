# Multi-stage Dockerfile for Spring Boot (Java 21)
# Builder: uses project wrapper to produce a fat jar
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /work

# Copy gradle wrapper and project files
COPY gradlew .
COPY gradle gradle
COPY settings.gradle build.gradle ./
COPY src src

# Make wrapper executable and build jar (skip tests for faster local builds)
RUN chmod +x ./gradlew && \
    ./gradlew bootJar --no-daemon -x test

# Runtime image: slim JRE
FROM eclipse-temurin:21-jre
ARG JAR_FILE=/work/build/libs/*.jar
COPY --from=builder /work/build/libs/*.jar /app/app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS
ENV SPRING_PROFILES_ACTIVE=default

EXPOSE 8080
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]
