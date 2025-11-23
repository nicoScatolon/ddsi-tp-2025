# ==========================
# Etapa 1: Build del JAR con Maven
# ==========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Configurar encoding
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

WORKDIR /build

# PASO 1: Copiar el POM padre desde el directorio superior
# Nota: esto requiere que el contexto Docker incluya el directorio padre
COPY ../pom.xml ./pom.xml

# Instalar el POM padre
RUN mvn install -N -DskipTests

# PASO 2: Copiar el módulo actual (estamos EN fuenteDinamica)
COPY pom.xml ./fuenteDinamica/pom.xml
COPY src ./fuenteDinamica/src

# PASO 3: Compilar este módulo
WORKDIR /build/fuenteDinamica
RUN mvn clean package -DskipTests \
    -Dproject.build.sourceEncoding=UTF-8 \
    -Dproject.reporting.outputEncoding=UTF-8

# ==========================
# Etapa 2: Imagen final
# ==========================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR compilado
COPY --from=builder /build/fuenteDinamica/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]