# Etapa 1: Build
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .

# LIMITAMOS LA MEMORIA DE GRADLE PARA QUE NO CRASHEE RENDER
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.parallel=false -Xmx256m"

# Damos permisos y ejecutamos el build
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --no-daemon

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos solo el JAR ejecutable (excluyendo el plain)
COPY --from=build /app/build/libs/*[!plain].jar app.jar

# Limitamos la RAM también al ejecutar la app
ENV JAVA_OPTS="-Xmx300m"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]