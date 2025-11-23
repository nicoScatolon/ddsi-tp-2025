# ==========================
# Etapa 1: Build del JAR con Maven
# ==========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Configurar encoding
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

WORKDIR /build

# Copiar el POM padre desde el directorio superior
COPY ../pom.xml ./pom.xml

# Copiar el módulo actual
COPY pom.xml ./fuenteDinamica/pom.xml
COPY src ./fuenteDinamica/src

# Instalar el POM padre en el repositorio local de Maven
RUN mvn install -N -DskipTests

# Compilar solo este módulo
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