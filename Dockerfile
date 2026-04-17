# Etapa 1: Construcción
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
# Copiamos archivos de configuración primero para aprovechar la caché de Docker
COPY build.gradle settings.gradle ./
COPY src ./src
# Construimos el proyecto saltando los tests para acelerar el despliegue
RUN gradle build -x test --no-daemon

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Buscamos el archivo .jar (evitando el que termina en -plain.jar)
COPY --from=build /app/build/libs/*[!plain].jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]