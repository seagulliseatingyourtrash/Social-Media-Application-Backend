FROM openjdk:11-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

ARG REDIS_HOST
ARG RDS_HOST
ARG KAFKA_BROKERS

ENV REDIS_HOST=$REDIS_HOST
ENV RDS_HOST=$RDS_HOST
ENV KAFKA_BROKERS=$KAFKA_BROKERS

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]