FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/servicioEstadisticas-0.0.1.jar
COPY ${JAR_FILE} app_servicioEstadisticas.jar
LABEL authors="nicol"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_servicioEstadisticas.jar"]