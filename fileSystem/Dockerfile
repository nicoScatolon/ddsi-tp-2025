# ==========================
# Etapa 1: Build del JAR con Maven
# ==========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiamos primero SOLO el pom para aprovechar la caché de dependencias
COPY pom.xml .
# Descargamos dependencias (esto se cacheará si el pom no cambia)
RUN mvn dependency:go-offline

# Ahora copiamos el código fuente
COPY src ./src

# Compilamos (esto se ejecutará siempre que cambie el código en src)
RUN mvn clean package -DskipTests \
    -Dproject.build.sourceEncoding=UTF-8 \
    -Dproject.reporting.outputEncoding=UTF-8

# ==========================
# Etapa 2: Imagen final
# ==========================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Creamos directorios
RUN mkdir -p /app/fileSystem && \
    chmod -R 777 /app/fileSystem

COPY --from=builder /app/target/fileSystem-0.0.1.jar app.jar

EXPOSE 8091

# ESTO ES LO NUEVO:
# 1. Desactivamos el HealthCheck nativo de Docker por si acaso
HEALTHCHECK NONE

# 2. Comando "Try-Catch":
# Intenta correr Java. Si falla (||), imprime un aviso y se queda durmiendo (tail).
ENTRYPOINT ["/bin/sh", "-c", "java -jar app.jar || (echo '❌ LA APP CRASHEO - MANTENIENDO CONTENEDOR VIVO...' && tail -f /dev/null)"]