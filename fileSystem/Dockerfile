FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/fileSystem-0.0.1.jar
COPY ${JAR_FILE} app_fileSystem.jar
LABEL authors="nicol"
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "app_fileSystem.jar"]