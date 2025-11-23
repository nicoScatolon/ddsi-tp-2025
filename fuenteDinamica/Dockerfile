FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/fuenteDinamica-0.0.1.jar
COPY ${JAR_FILE} app_fuenteDinamica.jar
LABEL authors="nicol"
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app_fuenteDinamica.jar"]