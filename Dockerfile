FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build.gradle settings.gradle /app/

COPY gradle /app/gradle

RUN ./gradlew build || return 0

COPY . /app

RUN ./gradlew build

EXPOSE 8080

CMD ["java", "-jar", "build/libs/intcomex-api-0.0.1-SNAPSHOT.jar"]
