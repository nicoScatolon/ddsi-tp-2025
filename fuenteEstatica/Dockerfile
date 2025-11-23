FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/fuenteEstatica-0.0.1.jar
COPY ${JAR_FILE} app_fuenteEstatica.jar
LABEL authors="nicol"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_fuenteEstatica.jar"]