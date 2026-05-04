# Etapa 1: Build
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .

# Limites de memoria para el plan gratuito de Render
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.parallel=false -Xmx256m"

RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test --no-daemon

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Ahora copiamos el archivo con el nombre exacto que definimos en build.gradle
COPY --from=build /app/build/libs/app.jar app.jar

ENV JAVA_OPTS="-Xmx300m"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]