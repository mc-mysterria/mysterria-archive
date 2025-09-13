FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew

COPY src src

RUN ./gradlew build -x test

RUN ls -la /app/build/libs/

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/MysterriaArchive-*-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]