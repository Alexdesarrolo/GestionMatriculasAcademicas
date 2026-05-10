# Gestión de Matrículas Académicas (Backend)

Sistema de backend para la gestión de turnos de matrícula universitaria, desarrollado con Spring Boot.

## 🚀 Tecnologías

*   **Java 21** (Amazon Corretto)
*   **Spring Boot 3.5.11**
*   **Spring Security** con **JWT** para autenticación.
*   **Spring Data JPA** para persistencia.
*   **PostgreSQL** como base de datos principal.
*   **MapStruct** para el mapeo de DTOs.
*   **Lombok** para reducir código repetitivo.
*   **Spring Boot Starter Mail** para el envío de correos electrónicos.

## 🛠️ Requisitos Previos

*   **JDK 21** instalado.
*   **PostgreSQL** ejecutándose localmente o en un contenedor.
*   **Gradle** (incluido mediante el wrapper `./gradlew`).

## ⚙️ Configuración

El archivo de configuración principal se encuentra en `src/main/resources/application.yaml`.

### Base de Datos
Asegúrate de tener creada la base de datos `maticulas_academicas` en PostgreSQL.
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/maticulas_academicas
    username: postgres
    password: 1234
```

### Seguridad (JWT)
El secreto de JWT y su tiempo de expiración están configurados en:
```yaml
jwt:
  secret: YTI5YzgwZmE1NDVlY2ZjY2EyNWU5YThiYmMzYjhiYThiYTVlYmU1YWE2M2U4ODYy
  expiration: 86400000 # 24 horas
```

### Correo Electrónico
Configurado para usar Gmail SMTP para el restablecimiento de contraseñas.
```yaml
spring:
  mail:
    username: ddagmaaralejandra@gmail.com
    password: ${MAIL_PASSWORD} # Se recomienda usar variables de entorno
```

## 📂 Estructura del Proyecto

*   `config`: Configuraciones de seguridad, CORS y JWT.
*   `domain`: Entidades JPA (User, Student, Appointment, etc.).
*   `repository`: Interfaces de Spring Data Repository.
*   `service`: Lógica de negocio e interfaces de servicio.
*   `web.controller`: Controladores REST.
*   `service.dto`: Objetos de Transferencia de Datos.
*   `service.mapper`: Mapeadores de MapStruct.

## 🏃 Ejecución

Para ejecutar la aplicación localmente:

```bash
./gradlew bootRun
```

La API estará disponible en `http://localhost:8081/api`.

## 🧪 Pruebas

Para ejecutar los tests:

```bash
./gradlew test
```
