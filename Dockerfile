# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./gradlew

RUN ./gradlew --refresh-dependencies compileJava --no-daemon

COPY src ./src

CMD ["./gradlew", "bootRun"]