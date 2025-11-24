# ==========================
# Etapa 1: Build del JAR con Maven
# ==========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests \
    -Dproject.build.sourceEncoding=UTF-8 \
    -Dproject.reporting.outputEncoding=UTF-8

# ==========================
# Etapa 2: Imagen final
# ==========================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN mkdir -p /app/fileSystem && chmod -R 777 /app/fileSystem

COPY --from=builder /app/target/*.jar app.jar

# Verificar que el JAR existe
RUN ls -lh app.jar

EXPOSE 8091

# Agregar script de inicio con logs
RUN echo '#!/bin/sh' > /app/start.sh && \
    echo 'echo "=== Starting fileSystem service ===" ' >> /app/start.sh && \
    echo 'echo "SERVER_PORT: $SERVER_PORT"' >> /app/start.sh && \
    echo 'echo "APP_DIRECTORIO_DESTINO: $APP_DIRECTORIO_DESTINO"' >> /app/start.sh && \
    echo 'java -jar app.jar' >> /app/start.sh && \
    chmod +x /app/start.sh

ENTRYPOINT ["/app/start.sh"]